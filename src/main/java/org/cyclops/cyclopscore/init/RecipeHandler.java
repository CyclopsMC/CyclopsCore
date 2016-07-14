package org.cyclops.cyclopscore.init;

import com.google.common.collect.*;
import lombok.Data;
import lombok.Getter;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.RecipeSorter;
import org.cyclops.cyclopscore.GeneralConfig;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.recipe.event.ObservableShapedRecipe;
import org.cyclops.cyclopscore.recipe.event.ObservableShapelessRecipe;
import org.cyclops.cyclopscore.recipe.xml.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.*;
import java.util.regex.Pattern;

/**
 * Holder class of all the recipes.
 * @author rubensworks
 *
 */
@Data
public class RecipeHandler {

    private final Multimap<String, ItemStack> taggedOutput = LinkedListMultimap.create();
    private final Multimap<String, ExtendedConfig<?>> taggedConfigurablesOutput = LinkedListMultimap.create();
    private final Map<String, IRecipeTypeHandler> recipeTypeHandlers = Maps.newHashMap();
    private final Map<String, IRecipeConditionHandler> recipeConditionHandlers = Maps.newHashMap();
    private final Map<String, ItemStack> predefinedItems = Maps.newHashMap();
    private final Set<String> predefinedValues = Sets.newHashSet();
    private final ModBase mod;
    private final List<String> recipeFiles;

    @Getter(lazy = true)
    private final Pattern externalRecipesPattern = Pattern.compile("^[^_].*\\.xml");
    @Getter(lazy = true)
    private final Pattern externalRecipesOverridesPattern = Pattern.compile("^_override$");

    public RecipeHandler(ModBase mod, String... fileNames) {
        this.mod = mod;
        this.recipeFiles = Lists.newArrayList(fileNames);
        registerHandlers(recipeTypeHandlers, recipeConditionHandlers);
    }

    protected void registerHandlers(Map<String, IRecipeTypeHandler> recipeTypeHandlers, Map<String, IRecipeConditionHandler> recipeConditionHandlers) {
        recipeTypeHandlers.put("shaped", new ShapedRecipeTypeHandler());
        recipeTypeHandlers.put("shapeless", new ShapelessRecipeTypeHandler());
        recipeTypeHandlers.put("smelting", new SmeltingRecipeTypeHandler());

        recipeConditionHandlers.put("config", new ConfigRecipeConditionHandler());
        recipeConditionHandlers.put("predefined", new PredefinedRecipeConditionHandler());
        recipeConditionHandlers.put("mod", new ModRecipeConditionHandler());
        recipeConditionHandlers.put("oredict", new OreDictConditionHandler());
    }

    protected XmlRecipeLoader constructXmlRecipeLoader(InputStream is, String fileName) {
        return new XmlRecipeLoader(getMod(), is, fileName, this);
    }

    protected XmlRecipeLoader registerRecipesForFile(InputStream is, String fileName, boolean canOverride) throws XmlRecipeLoader.XmlRecipeException {
        return constructXmlRecipeLoader(is, fileName);
    }

    protected List<XmlRecipeLoader> registerRecipesForFiles(File file, Map<String, XmlRecipeLoader> internalLoaders, boolean canOverride) throws XmlRecipeLoader.XmlRecipeException {
        if(file.isFile() && getExternalRecipesPattern().matcher(file.getName()).matches()) {
            try {
                XmlRecipeLoader loader = registerRecipesForFile(new FileInputStream(file), file.getName(), canOverride);
                if(internalLoaders.containsKey(file.getName()) && canOverride) {
                    // Override the internal recipes file.
                    internalLoaders.put(file.getName(), loader);
                } else {
                    return Lists.newArrayList(loader);
                }
            } catch (FileNotFoundException e) {
                // Very unlikely to happen...
            }
        } else if(file.isDirectory()) {
            File[] childFiles = file.listFiles();
            if(childFiles != null) {
                List<XmlRecipeLoader> loaders = Lists.newLinkedList();
                for (File childFile : childFiles) {
                    loaders.addAll(registerRecipesForFiles(
                            childFile, internalLoaders, getExternalRecipesOverridesPattern().matcher(file.getName()).matches()
                    ));
                }
                return loaders;
            }
        }
        return Collections.emptyList();
    }

    private void loadAllRecipes(Collection<XmlRecipeLoader> loaders) {
        for(XmlRecipeLoader loader : loaders) {
            InputStream xsdIs = RecipeHandler.class.getResourceAsStream(getRecipesXsdPath());
            loader.setValidator(xsdIs);
            loader.loadRecipes(GeneralConfig.crashOnInvalidRecipe);
        }
    }

    protected String getRecipesBasePath() {
        return "/assets/" + getMod().getModId() + "/recipes/";
    }

    protected String getRecipesXsdPath() {
        return "/assets/" + Reference.MOD_ID + "/recipes/" + "recipes.xsd";
    }

    /**
     * Register all the recipes of this mod.
     * @param rootConfigFolder The root config folder for this mod, containing any
     * specific configuration stuff.
     */
    public final void registerRecipes(File rootConfigFolder) throws XmlRecipeLoader.XmlRecipeException {
        registerRecipeSorters();
    	loadPredefineds(getPredefinedItems(), getPredefinedValues());

        // Load the recipes stored in XML.
        Map<String, XmlRecipeLoader> internalLoaders = Maps.newHashMapWithExpectedSize(getRecipeFiles().size());
        for(String file : getRecipeFiles()) {
            InputStream is = RecipeHandler.class.getResourceAsStream(getRecipesBasePath() + file);
            internalLoaders.put(file, registerRecipesForFile(is, file, false));
        }

        // Load all the externally defined recipes.
        List<XmlRecipeLoader> externalLoaders = registerRecipesForFiles(rootConfigFolder, internalLoaders, false);

        loadAllRecipes(internalLoaders.values());
        loadAllRecipes(externalLoaders);

    	// Register remaining recipes that are too complex to declare in xml files.
        registerCustomRecipes();
    }

    protected void registerRecipeSorters() {
        // Register custom recipe classes
        RecipeSorter.register(getMod().getModId() + "observableshapeless", ObservableShapelessRecipe.class,
                RecipeSorter.Category.SHAPELESS, "after:forge:shapelessore");
        RecipeSorter.register(getMod().getModId() + "observableshaped", ObservableShapedRecipe.class,
                RecipeSorter.Category.SHAPELESS, "after:forge:shapedore");
    }

    protected void loadPredefineds(Map<String, ItemStack> predefinedItems, Set<String> predefinedValues) {

    }

    protected void registerCustomRecipes() {

    }

    /**
     * Get a predefined item by key.
     * @param key The key of the item.
     * @return The item or null.
     */
    public ItemStack getPredefinedItem(String key) {
        return predefinedItems.get(key);
    }

    /**
     * Check if a value has been predefined.
     * @param value The key to check.
     * @return If it was predefined.
     */
    public boolean isPredefinedValue(String value) {
        return predefinedValues.contains(value);
    }
    
}
