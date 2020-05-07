package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.client.Minecraft;
import net.minecraft.fluid.Fluid;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.ICraftingRecipe;
import net.minecraft.item.crafting.IRecipe;
import net.minecraft.item.crafting.IRecipeType;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.ItemTags;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.registry.Registry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.registries.ForgeRegistries;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.util.Strings;
import org.cyclops.cyclopscore.RegistryEntries;
import org.cyclops.cyclopscore.helper.CraftingHelpers;
import org.cyclops.cyclopscore.helper.FluidHelpers;
import org.cyclops.cyclopscore.infobook.condition.ConfigSectionConditionHandler;
import org.cyclops.cyclopscore.infobook.condition.FluidSectionConditionHandler;
import org.cyclops.cyclopscore.infobook.condition.ISectionConditionHandler;
import org.cyclops.cyclopscore.infobook.condition.ItemSectionConditionHandler;
import org.cyclops.cyclopscore.infobook.condition.ModSectionConditionHandler;
import org.cyclops.cyclopscore.infobook.condition.PredefinedSectionConditionHandler;
import org.cyclops.cyclopscore.infobook.condition.TagSectionConditionHandler;
import org.cyclops.cyclopscore.infobook.pageelement.*;
import org.cyclops.cyclopscore.init.ModBase;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.annotation.Nullable;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

/**
 * XML parser which will generate the infobook.
 * @author rubensworks
 */
public class InfoBookParser {

    private static final Map<String, IInfoSectionFactory> SECTION_FACTORIES = Maps.newHashMap();
    private static final Map<String, IAppendixFactory> APPENDIX_FACTORIES = Maps.newHashMap();
    private static final Set<String> IGNORED_APPENDIX_FACTORIES = Sets.newHashSet();
    private static final Map<String, IAppendixListFactory> APPENDIX_LIST_FACTORIES = Maps.newHashMap();
    private static final Map<String, IAppendixItemFactory> APPENDIX_RECIPELIST_FACTORIES = Maps.newHashMap();
    private static final Map<String, IRewardFactory> REWARD_FACTORIES = Maps.newHashMap();
    public static final Map<String, ISectionConditionHandler> RECIPE_CONDITION_HANDLERS = Maps.newHashMap();

    static {
        // Infosection factories
        registerSectionFactory("", new IInfoSectionFactory() {

            @Override
            public InfoSection create(IInfoBook infoBook, InfoSection parent, int childIndex, String translationKey,
                                      ArrayList<String> paragraphs, List<SectionAppendix> appendixes,
                                      ArrayList<String> tagList) {
                return new InfoSection(infoBook, parent, childIndex, translationKey, paragraphs, appendixes, tagList);
            }

        });

        // Appendix factories
        registerAppendixFactory("image", new IAppendixFactory() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, Element node) throws InvalidAppendixException {
                return new ImageAppendix(infoBook, getNodeResourceLocation(node),
                        Integer.parseInt(node.getAttribute("width")), Integer.parseInt(node.getAttribute("height")));
            }

        });
        InfoBookParser.registerAppendixRecipeFactories(IRecipeType.CRAFTING, CraftingRecipeAppendix::new);
        InfoBookParser.registerAppendixRecipeFactories(IRecipeType.SMELTING, FurnaceRecipeAppendix::new);
        registerAppendixFactory("advancement_rewards", new InfoBookParser.IAppendixFactory() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, Element node) throws InfoBookParser.InvalidAppendixException {
                if (infoBook.getMod().getReferenceValue(ModBase.REFKEY_INFOBOOK_REWARDS)) {
                    List<ResourceLocation> advancements = Lists.newArrayList();
                    List<IReward> rewards = Lists.newArrayList();
                    String achievementRewardsId = node.getAttribute("id");

                    NodeList children = node.getChildNodes();
                    for (int i = 0; i < children.getLength(); i++) {
                        if (children.item(i) instanceof Element) {
                            Element child = (Element) children.item(i);
                            NodeList subChildren = child.getChildNodes();
                            if (child.getNodeName().equals("advancements")) {
                                for (int j = 0; j < subChildren.getLength(); j++) {
                                    if (subChildren.item(j) instanceof Element) {
                                        Element advancementNode = (Element) subChildren.item(j);
                                        String advancementId = advancementNode.getAttribute("id");
                                        if (!advancementId.isEmpty()) {
                                            advancements.add(new ResourceLocation(advancementId));
                                        }
                                    }
                                }
                            } else if (child.getNodeName().equals("rewards")) {
                                for (int j = 0; j < subChildren.getLength(); j++) {
                                    if (subChildren.item(j) instanceof Element) {
                                        Element rewardNode = (Element) subChildren.item(j);
                                        String rewardType = rewardNode.getAttribute("type");
                                        rewards.add(createReward(infoBook, rewardType, rewardNode));
                                    }
                                }
                            }
                        }
                    }
                    if (achievementRewardsId == null || achievementRewardsId.isEmpty()) {
                        throw new InfoBookParser.InvalidAppendixException("Every advancement rewards tag must have a unique id attribute");
                    }
                    AdvancementRewards advancementRewards = new AdvancementRewards(achievementRewardsId, advancements, rewards);
                    return new AdvancementRewardsAppendix(infoBook, advancementRewards);
                } else {
                    return null;
                }
            }
        });

        InfoBookParser.registerAppendixFactory("keybinding", new InfoBookParser.IAppendixFactory() {
            @Override
            public SectionAppendix create(IInfoBook infoBook, Element node) throws InfoBookParser.InvalidAppendixException {
                return new KeyBindingAppendix(infoBook, node.getTextContent());
            }
        });

        // Appendix list factories
        registerAppendixListFactory("default", new IAppendixListFactory() {
            @Override
            public List<SectionAppendix> create(IInfoBook infoBook, Element node) throws InvalidAppendixException {
                List<SectionAppendix> appendixList = Lists.newArrayList();

                String type = node.getAttribute("type");
                Optional<IRecipeType<?>> recipeTypeOptional = Registry.RECIPE_TYPE.getValue(new ResourceLocation(type));
                if (!recipeTypeOptional.isPresent()) {
                    throw new InvalidAppendixException("Could not find a recipe type: " + type);
                }
                IRecipeType recipeType = recipeTypeOptional.get();
                Map<ResourceLocation, IRecipe<?>> recipes = Minecraft.getInstance().getConnection().getRecipeManager().getRecipes(recipeType);

                String idRegexString = node.getTextContent().trim();

                for (IRecipe<?> recipe : recipes.values()) {
                    try {
                        if (idRegexString.isEmpty() || recipe.getId().toString().matches(idRegexString)) {
                            appendixList.add(createAppendix(infoBook, type, recipe));
                        }
                    } catch (InvalidAppendixException e) {
                        // Skip this appendix.
                        e.setState(infoBook, null);
                        infoBook.getMod().log(Level.WARN, e.toString());
                    }
                }
                return appendixList;
            }
        });

        // Reward factories
        registerAppendixRewardFactory("item", new IRewardFactory() {
            @Override
            public IReward create(IInfoBook infoBook, Element node) throws InvalidAppendixException {
                ItemStack itemStack = InfoBookParser.createStack(node);
                return new RewardItem(itemStack);
            }
        });

        RECIPE_CONDITION_HANDLERS.put("config", new ConfigSectionConditionHandler());
        RECIPE_CONDITION_HANDLERS.put("predefined", new PredefinedSectionConditionHandler());
        RECIPE_CONDITION_HANDLERS.put("mod", new ModSectionConditionHandler());
        RECIPE_CONDITION_HANDLERS.put("itemtag", new TagSectionConditionHandler<>(ItemTags.getCollection()));
        RECIPE_CONDITION_HANDLERS.put("blocktag", new TagSectionConditionHandler<>(BlockTags.getCollection()));
        RECIPE_CONDITION_HANDLERS.put("fluid", new FluidSectionConditionHandler());
        RECIPE_CONDITION_HANDLERS.put("item", new ItemSectionConditionHandler());
    }

    protected static ResourceLocation getNodeResourceLocation(Element node) {
        return new ResourceLocation(node.getTextContent().trim());
    }

    /**
     * Register a new info section factory.
     * @param name The unique name for this factory, make sure to namespace this to your mod to avoid collisions.
     * @param factory The factory
     */
    public static void registerSectionFactory(String name, IInfoSectionFactory factory) {
        if(SECTION_FACTORIES.put(name, factory) != null) {
            throw new RuntimeException(String.format("A section factory with name %s was registered while another one already existed!", name));
        }
    }

    /**
     * Indicate that the given factory should be ignored, this is useful when a target is somehow being disabled and
     * no corresponding factory results should be shown.
     * @param name The unique name for this factory, make sure to namespace this to your mod to avoid collisions.
     */
    public static void registerIgnoredFactory(String name) {
        IGNORED_APPENDIX_FACTORIES.add(name);
    }

    /**
     * Register a new appendix factory.
     * @param name The unique name for this factory, make sure to namespace this to your mod to avoid collisions.
     * @param factory The factory
     */
    public static void registerAppendixFactory(String name, IAppendixFactory factory) {
        if(APPENDIX_FACTORIES.put(name, factory) != null) {
            throw new RuntimeException(String.format("An appendix factory with name %s was registered while another one already existed!", name));
        }
    }

    /**
     * Register a new appendix item factory.
     * @param name The unique name for this factory, make sure to namespace this to your mod to avoid collisions.
     * @param factory The factory
     */
    public static void registerAppendixItemFactory(String name, IAppendixItemFactory factory) {
        if(APPENDIX_RECIPELIST_FACTORIES.put(name, factory) != null) {
            throw new RuntimeException(String.format("An appendix item factory with name %s was registered while another one already existed!", name));
        }
    }

    /**
     * Register new appendix recipe factories.
     * @param recipeType The recipe type
     * @param factory The factory
     * @param <C> The recipe inventory type
     * @param <R> The recipe type
     */
    public static <C extends IInventory, R extends IRecipe<C>> void registerAppendixRecipeFactories(IRecipeType<R> recipeType, IAppendixItemFactory<C, R> factory) {
        String name = Registry.RECIPE_TYPE.getKey(recipeType).toString();
        registerAppendixFactory(name, (infoBook, node) -> {
            ResourceLocation recipeId = getNodeResourceLocation(node);
            Optional<R> recipe = CraftingHelpers.getClientRecipe(recipeType, recipeId);
            if (!recipe.isPresent()) {
                throw new InvalidAppendixException("Could not find " + name + " recipe for " + recipeId);
            }
            return factory.create(infoBook, recipe.get());
        });
        registerAppendixItemFactory(name, factory);
    }

    /**
     * Register a new appendix list factory.
     * @param name The unique name for this factory, make sure to namespace this to your mod to avoid collisions.
     * @param factory The factory
     */
    public static void registerAppendixListFactory(String name, IAppendixListFactory factory) {
        if(APPENDIX_LIST_FACTORIES.put(name, factory) != null) {
            throw new RuntimeException(String.format("An appendix item factory with name %s was registered while another one already existed!", name));
        }
    }

    /**
     * Register a new reward factory.
     * @param name The unique name for this factory, make sure to namespace this to your mod to avoid collisions.
     * @param factory The factory
     */
    public static void registerAppendixRewardFactory(String name, IRewardFactory factory) {
        if(REWARD_FACTORIES.put(name, factory) != null) {
            throw new RuntimeException(String.format("A reward factory with name %s was registered while another one already existed!", name));
        }
    }

    /**
     * Get the index attribute value of the given node.
     * @param node An element node.
     * @return The index attribute
     */
    public static int getIndex(Element node) {
        int index = 0;
        if(!node.getAttribute("index").isEmpty()) {
            index = Integer.parseInt(node.getAttribute("index"));
        }
        return index;
    }

    /**
     * Get the itemstack represented by the given node.
     * Interpreted attributes:
     * amount: the stacksize, defaults to 1.
     * @param node A node
     * @return An itemstack.
     * @throws InvalidAppendixException If the node was incorrectly structured.
     */
    public static ItemStack createStack(Element node) throws InvalidAppendixException {
        int amount = 1;
        if(!node.getAttribute("amount").isEmpty()) {
            amount = Integer.parseInt(node.getAttribute("amount"));
        }
        if("true".equals(node.getAttribute("predefined"))) {
            throw new UnsupportedOperationException("Could not find predefined item " + node.getTextContent());
        }
        ResourceLocation itemId = getNodeResourceLocation(node);
        Item item = ForgeRegistries.ITEMS.getValue(itemId);
        if(item == null) {
            throw new InvalidAppendixException("Invalid item " + itemId);
        }
        return new ItemStack(item, amount);
    }

    /**
     * Get a list of stacks from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @return The list of stacks.
     * @throws InvalidAppendixException If one of the item nodes was invalid
     */
    public static NonNullList<ItemStack> createStacksFromIngredient(Element ingredientNode)
            throws InvalidAppendixException {
        NonNullList<ItemStack> stacks = NonNullList.create();
        NodeList nodes = ingredientNode.getElementsByTagName("item");
        for (int i = 0; i < nodes.getLength(); i++) {
            stacks.add(InfoBookParser.createStack((Element) nodes.item(i)));
        }
        return stacks;
    }

    /**
     * Get the item from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @return The stack.
     * @throws InvalidAppendixException If not exactly one item node was found or if the item node was invalid.
     */
    public static ItemStack createStackFromIngredient(Element ingredientNode)
            throws InvalidAppendixException {
        NonNullList<ItemStack> stacks = createStacksFromIngredient(ingredientNode);
        if (stacks.size() != 1) {
            throw new InvalidAppendixException("At least one item node is required");
        }
        return stacks.get(0);
    }

    /**
     * Get the item from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @return The valid stack.
     */
    public static ItemStack createOptionalStackFromIngredient(Element ingredientNode) {
        return invalidAppendixExceptionThrowableOr(() -> createStackFromIngredient(ingredientNode), ItemStack.EMPTY);
    }

    /**
     * Get a list of stacks from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @return The list of valid stacks.
     */
    public static NonNullList<ItemStack> createOptionalStacksFromIngredient(Element ingredientNode) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        NodeList nodes = ingredientNode.getElementsByTagName("item");
        for (int i = 0; i < nodes.getLength(); i++) {
            try {
                stacks.add(createStackFromIngredient((Element) nodes.item(i)));
            } catch (InvalidAppendixException e) {
                // Ignore element
            }
        }
        return stacks;
    }

    /**
     * Get the ingredient from the given node.
     * @param node A node
     * @return An ingredient
     * @throws InvalidAppendixException If the node was incorrectly structured.
     */
    public static Ingredient createIngredient(Element node) throws InvalidAppendixException {
        return Ingredient.fromStacks(createStack(node));
    }

    /**
     * Get a list of ingredients from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @return The list of ingredients.
     * @throws InvalidAppendixException If one of the item nodes was invalid
     */
    public static NonNullList<Ingredient> createIngredientsFromIngredient(Element ingredientNode)
            throws InvalidAppendixException {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        NodeList nodes = ingredientNode.getElementsByTagName("item");
        for (int i = 0; i < nodes.getLength(); i++) {
            ingredients.add(InfoBookParser.createIngredient((Element) nodes.item(i)));
        }
        return ingredients;
    }

    /**
     * Get the ingredient from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @return The valid ingredient.
     */
    public static Ingredient createOptionalIngredientFromIngredient(Element ingredientNode) {
        return invalidAppendixExceptionThrowableOr(() -> createIngredientFromIngredient(ingredientNode), Ingredient.EMPTY);
    }

    /**
     * Get a list of ingredients from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @return The list of valid ingredients.
     */
    public static NonNullList<Ingredient> createOptionalIngredientsFromIngredient(Element ingredientNode) {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        NodeList nodes = ingredientNode.getElementsByTagName("item");
        for (int i = 0; i < nodes.getLength(); i++) {
            try {
                ingredients.add(createIngredientFromIngredient((Element) nodes.item(i)));
            } catch (InvalidAppendixException e) {
                // Ignore element
            }
        }
        return ingredients;
    }

    /**
     * Get the ingredient from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @return The ingredient.
     * @throws InvalidAppendixException If not exactly one item node was found or if the item node was invalid.
     */
    public static Ingredient createIngredientFromIngredient(Element ingredientNode)
            throws InvalidAppendixException {
        NonNullList<Ingredient> ingredients = createIngredientsFromIngredient(ingredientNode);
        if (ingredients.size() != 1) {
            throw new InvalidAppendixException("At least one item node is required");
        }
        return ingredients.get(0);
    }

    /**
     * Get the fluidstack represented by the given node.
     * Interpreted attributes:
     * amount: the fluid amount, defaults to 1000.
     * @param node A node
     * @return A fluidstack.
     * @throws InvalidAppendixException If the node was incorrectly structured.
     */
    public static FluidStack createFluidStack(Element node) throws InvalidAppendixException {
        int amount = FluidHelpers.BUCKET_VOLUME;
        if(!node.getAttribute("amount").isEmpty()) {
            amount = Integer.parseInt(node.getAttribute("amount"));
        }
        ResourceLocation fluidId = getNodeResourceLocation(node);
        Fluid fluid = ForgeRegistries.FLUIDS.getValue(fluidId);
        if(fluid == null) {
            throw new InvalidAppendixException("Invalid fluid " + fluidId);
        }
        return new FluidStack(fluid, amount);
    }

    /**
     * Get a list of fluidstacks from the given ingredient node.
     * @param ingredientNode A node that can contain 'fluid' nodes.
     * @return The list of fluidstacks.
     * @throws InvalidAppendixException If one of the fluid nodes was invalid
     */
    public static List<FluidStack> createFluidStacksFromIngredient(Element ingredientNode)
            throws InvalidAppendixException {
        List<FluidStack> stacks = NonNullList.create();
        NodeList nodes = ingredientNode.getElementsByTagName("fluid");
        for (int i = 0; i < nodes.getLength(); i++) {
            stacks.add(InfoBookParser.createFluidStack((Element) nodes.item(i)));
        }
        return stacks;
    }

    /**
     * Get the fluidstack from the given ingredient node.
     * @param ingredientNode A node that can contain 'fluid' nodes.
     * @return The ingredient.
     * @throws InvalidAppendixException If not exactly one fluid node was found or if the fluid node was invalid.
     */
    @Nullable
    public static FluidStack createFluidStackFromIngredient(Element ingredientNode)
            throws InvalidAppendixException {
        List<FluidStack> stacks = createFluidStacksFromIngredient(ingredientNode);
        if (stacks.size() != 1) {
            throw new InvalidAppendixException("At least one fluid node is required");
        }
        return stacks.get(0);
    }

    /**
     * Get the fluidstack from the given ingredient node.
     * @param ingredientNode A node that can contain 'fluid' nodes.
     * @return The valid fluidstack.
     */
    @Nullable
    public static FluidStack createOptionalFluidStackFromIngredient(Element ingredientNode) {
        return invalidAppendixExceptionThrowableOr(() -> createFluidStackFromIngredient(ingredientNode), null);
    }

    /**
     * Get a list of fluidstacks from the given ingredient node.
     * @param ingredientNode A node that can contain 'fluid' nodes.
     * @return The list of valid fluidstacks.
     */
    public static List<FluidStack> createOptionalFluidStacksFromIngredient(Element ingredientNode) {
        List<FluidStack> stacks = NonNullList.create();
        NodeList nodes = ingredientNode.getElementsByTagName("fluid");
        for (int i = 0; i < nodes.getLength(); i++) {
            try {
                stacks.add(createFluidStackFromIngredient((Element) nodes.item(i)));
            } catch (InvalidAppendixException e) {
                // Ignore element
            }
        }
        return stacks;
    }

    public static <R> R invalidAppendixExceptionThrowableOr(InvalidAppendixException.IThrower<R> thrower, R fallback) {
        try {
            return thrower.run();
        } catch (InvalidAppendixException e) {
            return fallback;
        }
    }

    /**
     * Initialize the given infobook from the given xml file.
     * @param infoBook The infobook to register.
     * @param path The path to the xml file of the book.
     * @param parent The parent section.
     * @return The root of the infobook.
     */
    public static InfoSection initializeInfoBook(IInfoBook infoBook, String path, @Nullable InfoSection parent) {
        try {
            InputStream is = InfoBookParser.class.getResourceAsStream(path);
            StreamSource stream = new StreamSource(is);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream.getInputStream());
            InfoSection root = buildSection(infoBook, parent, 0, doc.getDocumentElement());
            InfoSectionTagIndex tagIndex;
            if (parent == null) {
                tagIndex = new InfoSectionTagIndex(infoBook, root);
                root.registerSection(tagIndex);
                infoBook.putIndex(tagIndex);
            } else {
                tagIndex = infoBook.getTagIndex();
            }
            tagIndex.addSoftLinks(root);
            return root;
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }
        throw new InfoBookException("Info Book XML is invalid.");
    }

    protected static InfoSection buildSection(IInfoBook infoBook, InfoSection parent, int childIndex, Element sectionElement) {
        ModBase mod = infoBook.getMod();
        NodeList sections = sectionElement.getElementsByTagName("section");
        NodeList tags = sectionElement.getElementsByTagName("tag");
        NodeList paragraphs = sectionElement.getElementsByTagName("paragraph");
        NodeList appendixes = sectionElement.getElementsByTagName("appendix");
        NodeList appendixLists = sectionElement.getElementsByTagName("appendix_list");
        ArrayList<String> paragraphList = Lists.newArrayListWithCapacity(paragraphs.getLength());
        ArrayList<SectionAppendix> appendixList = Lists.newArrayListWithCapacity(appendixes.getLength());
        ArrayList<String> tagList = Lists.newArrayListWithCapacity(tags.getLength());
        String sectionName = sectionElement.getAttribute("name");
        InfoSection section = createSection(infoBook, parent, childIndex, sectionElement.getAttribute("type"),
                sectionName, paragraphList, appendixList, tagList);
        infoBook.addSection(sectionName, section);

        if(sections.getLength() > 0) {
            int subChildIndex = 0;
            for (int i = 0; i < sections.getLength(); i++) {
                Element subsection = (Element) sections.item(i);
                if(subsection.getParentNode() == sectionElement) {
                    InfoSection subsubsection = buildSection(infoBook, section, subChildIndex, subsection);
                    if(subsubsection != null) {
                        section.registerSection(subsubsection);
                        subChildIndex++;
                    }
                }
            }
        } else {
            for (int j = 0; j < tags.getLength(); j++) {
                Element tag = (Element) tags.item(j);
                String tagString = tag.getTextContent();
                String type = "config";
                if(tag.hasAttribute("type")) {
                    type = tag.getAttribute("type");
                }

                ModBase modRecipe = mod;
                if (tagString.contains(":")) {
                    String[] split = tagString.split(":");
                    modRecipe = ModBase.get(split[0]);
                    tagString = split[1];
                }

                ISectionConditionHandler conditionHandler = Objects.requireNonNull(RECIPE_CONDITION_HANDLERS.get(type), "Could not find a recipe condition handler by name " + type);
                if(!conditionHandler.isSatisfied(modRecipe, tagString)) {
                    return null;
                }
                // Yes, I know this isn't very clean, I am currently more interested in eating grapes than abstracting
                // this whole conditional system.
                if(conditionHandler instanceof ConfigSectionConditionHandler) {
                    tagList.add(tag.getTextContent());
                }
            }
            for (int j = 0; j < paragraphs.getLength(); j++) {
                Element paragraph = (Element) paragraphs.item(j);
                paragraphList.add(paragraph.getTextContent());
            }
            for (int j = 0; j < appendixes.getLength(); j++) {
                try {
                    Element appendix = (Element) appendixes.item(j);
                    SectionAppendix sectionAppendix = createAppendix(infoBook, appendix.getAttribute("type"), appendix);
                    if (sectionAppendix != null) {
                        appendixList.add(sectionAppendix);
                    }
                } catch (InvalidAppendixException e) {
                    // Skip this appendix.
                    e.setState(infoBook, parent);
                    infoBook.getMod().log(Level.WARN, e.toString());
                }
            }
            for (int j = 0; j < appendixLists.getLength(); j++) {
                Element appendixListNode = (Element) appendixLists.item(j);
                String factoryType = appendixListNode.getAttribute("factory");
                if (Strings.isEmpty(factoryType)) factoryType = "default";
                if (Strings.isNotEmpty(factoryType)) {
                    try {
                        appendixList.addAll(createAppendixes(infoBook, factoryType, appendixListNode));
                    } catch (InvalidAppendixException e) {
                        // Skip this appendix.
                        e.setState(infoBook, parent);
                        infoBook.getMod().log(Level.WARN, e.toString());
                    }
                }
            }
        }

        return section;
    }

    protected static InfoSection createSection(IInfoBook infoBook, InfoSection parent, int childIndex, String type, String translationKey,
                                               ArrayList<String> paragraphs, List<SectionAppendix> appendixes,
                                               ArrayList<String> tagList) {
        if(type == null) type = "";
        IInfoSectionFactory factory = SECTION_FACTORIES.get(type);
        if(factory == null) {
            throw new InfoBookException("No section of type '" + type + "' was found.");
        }
        return factory.create(infoBook, parent, childIndex, translationKey, paragraphs, appendixes, tagList);
    }

    protected static SectionAppendix createAppendix(IInfoBook infoBook, String type, Element node) throws InvalidAppendixException {
        if(type == null) type = "";
        IAppendixFactory factory = APPENDIX_FACTORIES.get(type);
        if(factory == null) {
            if(IGNORED_APPENDIX_FACTORIES.contains(type)) {
                throw new InvalidAppendixException("Ignore appendix of type '" + type + "'.");
            }
            throw new InfoBookException("No appendix of type '" + type + "' was found.");
        }
        return factory.create(infoBook, node);
    }

    protected static SectionAppendix createAppendix(IInfoBook infoBook, String type, IRecipe<?> recipe) throws InvalidAppendixException {
        if(type == null) type = "";
        IAppendixItemFactory factory = APPENDIX_RECIPELIST_FACTORIES.get(type);
        if(factory == null) {
            throw new InfoBookException("No appendix list of type '" + type + "' was found.");
        }
        return factory.create(infoBook, recipe);
    }

    protected static List<SectionAppendix> createAppendixes(IInfoBook infoBook, String factory, Element node) throws InvalidAppendixException {
        if(factory == null) factory = "";
        IAppendixListFactory factoryInstance = APPENDIX_LIST_FACTORIES.get(factory);
        if(factoryInstance == null) {
            throw new InfoBookException("No appendix list of factory '" + factory + "' was found.");
        }
        return factoryInstance.create(infoBook, node);
    }

    protected static IReward createReward(IInfoBook infoBook, String type, Element node) throws InvalidAppendixException {
        if(type == null) type = "";
        IRewardFactory factory = REWARD_FACTORIES.get(type);
        if(factory == null) {
            throw new InfoBookException("No reward factory of type '" + type + "' was found.");
        }
        return factory.create(infoBook, node);
    }

    public static interface IInfoSectionFactory {

        public InfoSection create(IInfoBook infoBook, InfoSection parent, int childIndex, String translationKey,
                                  ArrayList<String> paragraphs, List<SectionAppendix> appendixes,
                                  ArrayList<String> tagList);

    }

    public static interface IAppendixFactory {

        public SectionAppendix create(IInfoBook infoBook, Element node) throws InvalidAppendixException;

    }

    public static interface IAppendixListFactory {

        public List<SectionAppendix> create(IInfoBook infoBook, Element node) throws InvalidAppendixException;

    }

    public static interface IAppendixItemFactory<C extends IInventory, R extends IRecipe<C>> {

        public SectionAppendix create(IInfoBook infoBook, R recipe) throws InvalidAppendixException;

    }

    public static class InfoBookException extends RuntimeException {

        public InfoBookException(String message) {
            super(message);
        }

    }

    public static class InvalidAppendixException extends Exception {

        private IInfoBook infoBook;
        private InfoSection section;

        public InvalidAppendixException(String message) {
            super(message);
        }

        public void setState(IInfoBook infoBook, @Nullable InfoSection section) {
            this.infoBook = infoBook;
            this.section = section;
        }

        @Override
        public String toString() {
            return String.format("Invalid appendix %s from mod %s in an infobook: %s",
                    section != null ? section.getTranslationKey() : "<root>",
                    infoBook.getMod(), getLocalizedMessage());
        }

        public static interface IThrower<R> {
            public R run() throws InvalidAppendixException;
        }
    }

}
