package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.helper.CraftingHelpers;
import org.cyclops.cyclopscore.infobook.pageelement.CraftingRecipeAppendix;
import org.cyclops.cyclopscore.infobook.pageelement.FurnaceRecipeAppendix;
import org.cyclops.cyclopscore.infobook.pageelement.ImageAppendix;
import org.cyclops.cyclopscore.infobook.pageelement.SectionAppendix;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.recipe.xml.ConfigRecipeConditionHandler;
import org.cyclops.cyclopscore.recipe.xml.IRecipeConditionHandler;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.stream.StreamSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

/**
 * XML parser which will generate the infobook.
 * @author rubensworks
 */
public class InfoBookParser {

    private static final Map<String, IInfoSectionFactory> SECTION_FACTORIES = Maps.newHashMap();
    private static final Map<String, IAppendixFactory> APPENDIX_FACTORIES = Maps.newHashMap();
    private static final Set<String> IGNORED_APPENDIX_FACTORIES = Sets.newHashSet();
    private static final Map<String, IAppendixItemFactory> APPENDIX_LIST_FACTORIES = Maps.newHashMap();

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
        registerFactory("craftingRecipe", new IAppendixFactory() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, Element node) throws InvalidAppendixException {
                return new CraftingRecipeAppendix(infoBook, CraftingHelpers.findCraftingRecipe(createStack(node), getIndex(node)));
            }

        });
        registerFactory("furnaceRecipe", new IAppendixFactory() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, Element node) throws InvalidAppendixException {
                return new FurnaceRecipeAppendix(infoBook, CraftingHelpers.findFurnaceRecipe(createStack(node), getIndex(node)));
            }

        });

        // Appendix item factories
        registerFactory("craftingRecipe", new IAppendixItemFactory() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, ItemStack itemStack) throws InvalidAppendixException {
                return new CraftingRecipeAppendix(infoBook, CraftingHelpers.findCraftingRecipe(itemStack, 0));
            }

        });
        registerFactory("furnaceRecipe", new IAppendixItemFactory() {

            @Override
            public SectionAppendix create(IInfoBook infoBook, ItemStack itemStack) throws InvalidAppendixException {
                return new FurnaceRecipeAppendix(infoBook, CraftingHelpers.findFurnaceRecipe(itemStack, 0));
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
        if(APPENDIX_LIST_FACTORIES.put(name, factory) != null) {
            throw new RuntimeException(String.format("An appendix item factory with name %s was registered while another one already existed!", name));
        }
    }

    public static Map<String, Pair<InfoSection, Integer>> configLinks;

    public static int getIndex(Element node) {
        int index = 0;
        if(!node.getAttribute("index").isEmpty()) {
            index = Integer.parseInt(node.getAttribute("index"));
        }
        return index;
    }

    public static ItemStack createStack(Element node) throws InvalidAppendixException {
        int meta = OreDictionary.WILDCARD_VALUE;
        if(!node.getAttribute("meta").isEmpty()) {
            meta = Integer.parseInt(node.getAttribute("meta"));
        }
        Item item = Item.itemRegistry.getObject(new ResourceLocation(node.getTextContent()));
        if(item == null) {
            throw new InvalidAppendixException("Invalid item " + node.getTextContent());
        }
        return new ItemStack(item, 1, meta);
    }

    /**
     * Initialize the given infobook from the given xml file.
     * @param infoBook The infobook to register.
     * @param path The path to the xml file of the book.
     * @return The root of the infobook.
     */
    public static InfoSection initializeInfoBook(IInfoBook infoBook, String path) {
        try {
            InputStream is = InfoBookParser.class.getResourceAsStream(path);
            StreamSource stream = new StreamSource(is);
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document doc = dBuilder.parse(stream.getInputStream());
            InfoSection root = buildSection(infoBook, null, 0, doc.getDocumentElement());
            root.registerSection(new InfoSectionTagIndex(infoBook, root));
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
        NodeList appendixLists = sectionElement.getElementsByTagName("appendixList");
        ArrayList<String> paragraphList = Lists.newArrayListWithCapacity(paragraphs.getLength());
        ArrayList<SectionAppendix> appendixList = Lists.newArrayListWithCapacity(appendixes.getLength());
        ArrayList<String> tagList = Lists.newArrayListWithCapacity(tags.getLength());
        InfoSection section = createSection(infoBook, parent, childIndex, sectionElement.getAttribute("type"),
                sectionElement.getAttribute("name"), paragraphList, appendixList, tagList);

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
                IRecipeConditionHandler conditionHandler = mod.getRecipeHandler().getRecipeConditionHandlers().get(type);
                if(!conditionHandler.isSatisfied(mod.getRecipeHandler(), tag.getTextContent())) {
                    return null;
                }
                // Yes, I know this isn't very clean, I am currently more interested in eating grapes than abstracting
                // this whole conditional system.
                if(conditionHandler instanceof ConfigRecipeConditionHandler) {
                    tagList.add(tagString);
                }
            }
            for (int j = 0; j < paragraphs.getLength(); j++) {
                Element paragraph = (Element) paragraphs.item(j);
                paragraphList.add(paragraph.getTextContent());
            }
            for (int j = 0; j < appendixes.getLength(); j++) {
                try {
                    Element appendix = (Element) appendixes.item(j);
                    appendixList.add(createAppendix(infoBook, appendix.getAttribute("type"), appendix));
                } catch (InvalidAppendixException e) {
                    // Skip this appendix.
                    infoBook.getMod().log(Level.WARN, e.getMessage());
                }
            }
            for (int j = 0; j < appendixLists.getLength(); j++) {
                Element appendixListNode = (Element) appendixLists.item(j);
                String type = appendixListNode.getAttribute("type");
                Collection<ItemStack> itemStacks = mod.getRecipeHandler().getTaggedOutput().get(appendixListNode.getTextContent());
                for(ItemStack itemStack : itemStacks) {
                    try {
                        appendixList.add(createAppendix(infoBook, type, itemStack));
                    } catch (InvalidAppendixException e) {
                        // Skip this appendix.
                        infoBook.getMod().log(Level.WARN, e.getMessage());
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

    protected static SectionAppendix createAppendix(IInfoBook infoBook, String type, ItemStack itemStack) throws InvalidAppendixException {
        if(type == null) type = "";
        IAppendixItemFactory factory = APPENDIX_LIST_FACTORIES.get(type);
        if(factory == null) {
            throw new InfoBookException("No appendix list of type '" + type + "' was found.");
        }
        return factory.create(infoBook, itemStack);
    }

    public static interface IInfoSectionFactory {

        public InfoSection create(IInfoBook infoBook, InfoSection parent, int childIndex, String unlocalizedName,
                                  ArrayList<String> paragraphs, List<SectionAppendix> appendixes,
                                  ArrayList<String> tagList);

    }

    public static interface IAppendixFactory {

        public SectionAppendix create(IInfoBook infoBook, Element node) throws InvalidAppendixException;

    }

    public static interface IAppendixItemFactory {

        public SectionAppendix create(IInfoBook infoBook, ItemStack itemStack) throws InvalidAppendixException;

    }

    public static class InfoBookException extends RuntimeException {

        public InfoBookException(String message) {
            super(message);
        }

    }

    public static class InvalidAppendixException extends Exception {

        public InvalidAppendixException(String message) {
            super(message);
        }

    }

}
