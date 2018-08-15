package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.util.Strings;
import org.cyclops.cyclopscore.helper.CraftingHelpers;
import org.cyclops.cyclopscore.infobook.pageelement.*;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.init.RecipeHandler;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipe;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;
import org.cyclops.cyclopscore.recipe.custom.component.DummyPropertiesComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientRecipeComponent;
import org.cyclops.cyclopscore.recipe.custom.component.IngredientsRecipeComponent;
import org.cyclops.cyclopscore.recipe.xml.ConfigRecipeConditionHandler;
import org.cyclops.cyclopscore.recipe.xml.IRecipeConditionHandler;
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
import java.util.Collection;
import java.util.List;
import java.util.Map;
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

    static {
        // Infosection factories
        registerFactory("", new IInfoSectionFactory() {

            @Override
            public InfoSection create(IInfoBook infoBook, InfoSection parent, int childIndex, String unlocalizedName,
                                      ArrayList<String> paragraphs, List<SectionAppendix> appendixes,
                                      ArrayList<String> tagList) {
                return new InfoSection(infoBook, parent, childIndex, unlocalizedName, paragraphs, appendixes, tagList);
            }

        });

        // Appendix factories
        registerFactory("image", new IAppendixFactory() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, Element node) throws InvalidAppendixException {
                return new ImageAppendix(infoBook, new ResourceLocation(node.getTextContent()),
                        Integer.parseInt(node.getAttribute("width")), Integer.parseInt(node.getAttribute("height")));
            }

        });
        registerFactory("crafting_recipe", new IAppendixFactory() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, Element node) throws InvalidAppendixException {
                try {
                    return new CraftingRecipeAppendix(infoBook,
                            CraftingHelpers.findCraftingRecipe(createStack(node, infoBook.getMod().getRecipeHandler()), getIndex(node)));
                } catch (IllegalArgumentException e) {
                    throw new InvalidAppendixException(e.getMessage());
                }
            }

        });
        registerFactory("furnace_recipe", new IAppendixFactory() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, Element node) throws InvalidAppendixException {
                try {
                    return new FurnaceRecipeAppendix(infoBook,
                        CraftingHelpers.findFurnaceRecipe(createStack(node, infoBook.getMod().getRecipeHandler()), getIndex(node)));
                } catch (IllegalArgumentException e) {
                    throw new InvalidAppendixException(e.getMessage());
                }
            }

        });
        registerFactory("advancement_rewards", new InfoBookParser.IAppendixFactory() {

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

        InfoBookParser.registerFactory("keybinding", new InfoBookParser.IAppendixFactory() {
            @Override
            public SectionAppendix create(IInfoBook infoBook, Element node) throws InfoBookParser.InvalidAppendixException {
                return new KeyBindingAppendix(infoBook, node.getTextContent());
            }
        });

        // Appendix list factories
        registerFactory("default", new IAppendixListFactory() {
            @Override
            public List<SectionAppendix> create(IInfoBook infoBook, Element node) throws InvalidAppendixException {
                List<SectionAppendix> appendixList = Lists.newArrayList();
                String type = node.getAttribute("type");
                Collection<IRecipe> recipes = infoBook.getMod().getRecipeHandler().getTaggedRecipes().get(type + ":" + node.getTextContent());
                for (IRecipe recipe : recipes) {
                    try {
                        appendixList.add(createAppendix(infoBook, type, recipe));
                    } catch (InvalidAppendixException e) {
                        // Skip this appendix.
                        e.setState(infoBook, null);
                        infoBook.getMod().log(Level.WARN, e.toString());
                    }
                }
                return appendixList;
            }
        });

        // Appendix item factories
        registerFactory("crafting_recipe", new IAppendixItemFactory<IngredientsRecipeComponent, IngredientRecipeComponent, DummyPropertiesComponent>() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, IRecipe<IngredientsRecipeComponent, IngredientRecipeComponent, DummyPropertiesComponent> recipe) throws InvalidAppendixException {
                try {
                    return new CraftingRecipeAppendix(infoBook, CraftingHelpers.findCraftingRecipe(recipe.getOutput().getFirstItemStack(), 0));
                } catch (IllegalArgumentException e) {
                    throw new InvalidAppendixException(e.getMessage());
                }
            }

        });
        registerFactory("furnace_recipe", new IAppendixItemFactory<IngredientRecipeComponent, IngredientRecipeComponent, DummyPropertiesComponent>() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, IRecipe<IngredientRecipeComponent, IngredientRecipeComponent, DummyPropertiesComponent> recipe) throws InvalidAppendixException {
                try {
                    return new FurnaceRecipeAppendix(infoBook, CraftingHelpers.findFurnaceRecipe(recipe.getOutput().getFirstItemStack(), 0));
                } catch (IllegalArgumentException e) {
                    throw new InvalidAppendixException(e.getMessage());
                }
            }

        });

        // Reward factories
        registerFactory("item", new IRewardFactory() {
            @Override
            public IReward create(IInfoBook infoBook, Element node) throws InvalidAppendixException {
                ItemStack itemStack = InfoBookParser.createStack(node, infoBook.getMod().getRecipeHandler());
                if (itemStack.getMetadata() == OreDictionary.WILDCARD_VALUE) {
                    itemStack.setItemDamage(0);
                }
                return new RewardItem(itemStack);
            }
        });
    }

    /**
     * Register a new info section factory.
     * @param name The unique name for this factory, make sure to namespace this to your mod to avoid collisions.
     * @param factory The factory
     */
    public static void registerFactory(String name, IInfoSectionFactory factory) {
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
    public static void registerFactory(String name, IAppendixFactory factory) {
        if(APPENDIX_FACTORIES.put(name, factory) != null) {
            throw new RuntimeException(String.format("An appendix factory with name %s was registered while another one already existed!", name));
        }
    }

    /**
     * Register a new appendix item factory.
     * @param name The unique name for this factory, make sure to namespace this to your mod to avoid collisions.
     * @param factory The factory
     */
    public static void registerFactory(String name, IAppendixItemFactory factory) {
        if(APPENDIX_RECIPELIST_FACTORIES.put(name, factory) != null) {
            throw new RuntimeException(String.format("An appendix item factory with name %s was registered while another one already existed!", name));
        }
    }

    /**
     * Register a new appendix list factory.
     * @param name The unique name for this factory, make sure to namespace this to your mod to avoid collisions.
     * @param factory The factory
     */
    public static void registerFactory(String name, IAppendixListFactory factory) {
        if(APPENDIX_LIST_FACTORIES.put(name, factory) != null) {
            throw new RuntimeException(String.format("An appendix item factory with name %s was registered while another one already existed!", name));
        }
    }

    /**
     * Register a new reward factory.
     * @param name The unique name for this factory, make sure to namespace this to your mod to avoid collisions.
     * @param factory The factory
     */
    public static void registerFactory(String name, IRewardFactory factory) {
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
     * meta: The stack metadata value or wildcard '*', defaults to 0.
     * amount: the stacksize, defaults to 1.
     * @param node A node
     * @param recipeHandler A recipe handler.
     * @return An itemstack.
     * @throws InvalidAppendixException If the node was incorrectly structured.
     */
    public static ItemStack createStack(Element node, RecipeHandler recipeHandler) throws InvalidAppendixException {
        int meta = OreDictionary.WILDCARD_VALUE;
        int amount = 1;
        if(!node.getAttribute("meta").isEmpty()) {
            meta = Integer.parseInt(node.getAttribute("meta"));
        }
        if(!node.getAttribute("amount").isEmpty()) {
            amount = Integer.parseInt(node.getAttribute("amount"));
        }
        if("true".equals(node.getAttribute("predefined"))) {
            ItemStack itemStack = recipeHandler.getPredefinedItem(node.getTextContent());
            if(itemStack == null) {
                throw new InvalidAppendixException("Could not find predefined item " + node.getTextContent());
            }
            return itemStack;
        }
        Item item = Item.REGISTRY.getObject(new ResourceLocation(node.getTextContent()));
        if(item == null) {
            throw new InvalidAppendixException("Invalid item " + node.getTextContent());
        }
        return new ItemStack(item, amount, meta);
    }

    /**
     * Get a list of stacks from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @param recipeHandler A recipe handler.
     * @return The list of stacks.
     * @throws InvalidAppendixException If one of the item nodes was invalid
     */
    public static NonNullList<ItemStack> createStacksFromIngredient(Element ingredientNode, RecipeHandler recipeHandler)
            throws InvalidAppendixException {
        NonNullList<ItemStack> stacks = NonNullList.create();
        NodeList nodes = ingredientNode.getElementsByTagName("item");
        for (int i = 0; i < nodes.getLength(); i++) {
            stacks.add(InfoBookParser.createStack((Element) nodes.item(i), recipeHandler));
        }
        return stacks;
    }

    /**
     * Get the item from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @param recipeHandler A recipe handler.
     * @return The stack.
     * @throws InvalidAppendixException If not exactly one item node was found or if the item node was invalid.
     */
    public static ItemStack createStackFromIngredient(Element ingredientNode, RecipeHandler recipeHandler)
            throws InvalidAppendixException {
        NonNullList<ItemStack> stacks = createStacksFromIngredient(ingredientNode, recipeHandler);
        if (stacks.size() != 1) {
            throw new InvalidAppendixException("At least one item node is required");
        }
        return stacks.get(0);
    }

    /**
     * Get the item from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @param recipeHandler A recipe handler.
     * @return The valid stack.
     */
    public static ItemStack createOptionalStackFromIngredient(Element ingredientNode, RecipeHandler recipeHandler) {
        return invalidAppendixExceptionThrowableOr(() -> createStackFromIngredient(ingredientNode, recipeHandler), ItemStack.EMPTY);
    }

    /**
     * Get a list of stacks from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @param recipeHandler A recipe handler.
     * @return The list of valid stacks.
     */
    public static NonNullList<ItemStack> createOptionalStacksFromIngredient(Element ingredientNode, RecipeHandler recipeHandler) {
        NonNullList<ItemStack> stacks = NonNullList.create();
        NodeList nodes = ingredientNode.getElementsByTagName("item");
        for (int i = 0; i < nodes.getLength(); i++) {
            try {
                stacks.add(createStackFromIngredient((Element) nodes.item(i), recipeHandler));
            } catch (InvalidAppendixException e) {
                // Ignore element
            }
        }
        return stacks;
    }

    /**
     * Get the ingredient from the given node.
     * @param node A node
     * @param recipeHandler A recipe handler
     * @return An ingredient
     * @throws InvalidAppendixException If the node was incorrectly structured.
     */
    public static Ingredient createIngredient(Element node, RecipeHandler recipeHandler) throws InvalidAppendixException {
        return Ingredient.fromStacks(createStack(node, recipeHandler));
    }

    /**
     * Get a list of ingredients from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @param recipeHandler A recipe handler.
     * @return The list of ingredients.
     * @throws InvalidAppendixException If one of the item nodes was invalid
     */
    public static NonNullList<Ingredient> createIngredientsFromIngredient(Element ingredientNode, RecipeHandler recipeHandler)
            throws InvalidAppendixException {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        NodeList nodes = ingredientNode.getElementsByTagName("item");
        for (int i = 0; i < nodes.getLength(); i++) {
            ingredients.add(InfoBookParser.createIngredient((Element) nodes.item(i), recipeHandler));
        }
        return ingredients;
    }

    /**
     * Get the ingredient from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @param recipeHandler A recipe handler.
     * @return The valid ingredient.
     */
    public static Ingredient createOptionalIngredientFromIngredient(Element ingredientNode, RecipeHandler recipeHandler) {
        return invalidAppendixExceptionThrowableOr(() -> createIngredientFromIngredient(ingredientNode, recipeHandler), Ingredient.EMPTY);
    }

    /**
     * Get a list of ingredients from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @param recipeHandler A recipe handler.
     * @return The list of valid ingredients.
     */
    public static NonNullList<Ingredient> createOptionalIngredientsFromIngredient(Element ingredientNode, RecipeHandler recipeHandler) {
        NonNullList<Ingredient> ingredients = NonNullList.create();
        NodeList nodes = ingredientNode.getElementsByTagName("item");
        for (int i = 0; i < nodes.getLength(); i++) {
            try {
                ingredients.add(createIngredientFromIngredient((Element) nodes.item(i), recipeHandler));
            } catch (InvalidAppendixException e) {
                // Ignore element
            }
        }
        return ingredients;
    }

    /**
     * Get the ingredient from the given ingredient node.
     * @param ingredientNode A node that can contain 'item' nodes.
     * @param recipeHandler A recipe handler.
     * @return The ingredient.
     * @throws InvalidAppendixException If not exactly one item node was found or if the item node was invalid.
     */
    public static Ingredient createIngredientFromIngredient(Element ingredientNode, RecipeHandler recipeHandler)
            throws InvalidAppendixException {
        NonNullList<Ingredient> ingredients = createIngredientsFromIngredient(ingredientNode, recipeHandler);
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
     * @param recipeHandler A recipe handler.
     * @return A fluidstack.
     * @throws InvalidAppendixException If the node was incorrectly structured.
     */
    public static FluidStack createFluidStack(Element node, RecipeHandler recipeHandler) throws InvalidAppendixException {
        int amount = Fluid.BUCKET_VOLUME;
        if(!node.getAttribute("amount").isEmpty()) {
            amount = Integer.parseInt(node.getAttribute("amount"));
        }
        Fluid fluid = FluidRegistry.getFluid(node.getTextContent());
        if(fluid == null) {
            throw new InvalidAppendixException("Invalid fluid " + node.getTextContent());
        }
        return new FluidStack(fluid, amount);
    }

    /**
     * Get a list of fluidstacks from the given ingredient node.
     * @param ingredientNode A node that can contain 'fluid' nodes.
     * @param recipeHandler A recipe handler.
     * @return The list of fluidstacks.
     * @throws InvalidAppendixException If one of the fluid nodes was invalid
     */
    public static List<FluidStack> createFluidStacksFromIngredient(Element ingredientNode, RecipeHandler recipeHandler)
            throws InvalidAppendixException {
        List<FluidStack> stacks = NonNullList.create();
        NodeList nodes = ingredientNode.getElementsByTagName("fluid");
        for (int i = 0; i < nodes.getLength(); i++) {
            stacks.add(InfoBookParser.createFluidStack((Element) nodes.item(i), recipeHandler));
        }
        return stacks;
    }

    /**
     * Get the fluidstack from the given ingredient node.
     * @param ingredientNode A node that can contain 'fluid' nodes.
     * @param recipeHandler A recipe handler.
     * @return The ingredient.
     * @throws InvalidAppendixException If not exactly one fluid node was found or if the fluid node was invalid.
     */
    @Nullable
    public static FluidStack createFluidStackFromIngredient(Element ingredientNode, RecipeHandler recipeHandler)
            throws InvalidAppendixException {
        List<FluidStack> stacks = createFluidStacksFromIngredient(ingredientNode, recipeHandler);
        if (stacks.size() != 1) {
            throw new InvalidAppendixException("At least one fluid node is required");
        }
        return stacks.get(0);
    }

    /**
     * Get the fluidstack from the given ingredient node.
     * @param ingredientNode A node that can contain 'fluid' nodes.
     * @param recipeHandler A recipe handler.
     * @return The valid fluidstack.
     */
    @Nullable
    public static FluidStack createOptionalFluidStackFromIngredient(Element ingredientNode, RecipeHandler recipeHandler) {
        return invalidAppendixExceptionThrowableOr(() -> createFluidStackFromIngredient(ingredientNode, recipeHandler), null);
    }

    /**
     * Get a list of fluidstacks from the given ingredient node.
     * @param ingredientNode A node that can contain 'fluid' nodes.
     * @param recipeHandler A recipe handler.
     * @return The list of valid fluidstacks.
     */
    public static List<FluidStack> createOptionalFluidStacksFromIngredient(Element ingredientNode, RecipeHandler recipeHandler) {
        List<FluidStack> stacks = NonNullList.create();
        NodeList nodes = ingredientNode.getElementsByTagName("fluid");
        for (int i = 0; i < nodes.getLength(); i++) {
            try {
                stacks.add(createFluidStackFromIngredient((Element) nodes.item(i), recipeHandler));
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
                infoBook.setTagIndex(tagIndex);
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

                IRecipeConditionHandler conditionHandler = modRecipe.getRecipeHandler().getRecipeConditionHandlers().get(type);
                if(!conditionHandler.isSatisfied(modRecipe.getRecipeHandler(), tagString)) {
                    return null;
                }
                // Yes, I know this isn't very clean, I am currently more interested in eating grapes than abstracting
                // this whole conditional system.
                if(conditionHandler instanceof ConfigRecipeConditionHandler) {
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

    protected static InfoSection createSection(IInfoBook infoBook, InfoSection parent, int childIndex, String type, String unlocalizedName,
                                               ArrayList<String> paragraphs, List<SectionAppendix> appendixes,
                                               ArrayList<String> tagList) {
        if(type == null) type = "";
        IInfoSectionFactory factory = SECTION_FACTORIES.get(type);
        if(factory == null) {
            throw new InfoBookException("No section of type '" + type + "' was found.");
        }
        return factory.create(infoBook, parent, childIndex, unlocalizedName, paragraphs, appendixes, tagList);
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

    protected static SectionAppendix createAppendix(IInfoBook infoBook, String type, IRecipe recipe) throws InvalidAppendixException {
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

        public InfoSection create(IInfoBook infoBook, InfoSection parent, int childIndex, String unlocalizedName,
                                  ArrayList<String> paragraphs, List<SectionAppendix> appendixes,
                                  ArrayList<String> tagList);

    }

    public static interface IAppendixFactory {

        public SectionAppendix create(IInfoBook infoBook, Element node) throws InvalidAppendixException;

    }

    public static interface IAppendixListFactory {

        public List<SectionAppendix> create(IInfoBook infoBook, Element node) throws InvalidAppendixException;

    }

    public static interface IAppendixItemFactory<I extends IRecipeInput, O extends IRecipeOutput, P extends IRecipeProperties> {

        public SectionAppendix create(IInfoBook infoBook, IRecipe<I, O, P> recipe) throws InvalidAppendixException;

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
                    section != null ? section.getUnlocalizedName() : "<root>",
                    infoBook.getMod(), getLocalizedMessage());
        }

        public static interface IThrower<R> {
            public R run() throws InvalidAppendixException;
        }
    }

}
