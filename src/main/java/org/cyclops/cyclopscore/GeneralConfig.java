package org.cyclops.cyclopscore;


import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfig;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * A config with general options for this mod.
 * @author rubensworks
 *
 */
public class GeneralConfig extends DummyConfig {
    
    /**
     * The current mod version, will be used to check if the player's config isn't out of date and
     * warn the player accordingly.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "Config version for " + Reference.MOD_NAME +".\nDO NOT EDIT MANUALLY!")
    public static String version = Reference.MOD_VERSION;
    
    /**
     * If the debug mode should be enabled. @see Debug
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "Set 'true' to enable development debug mode. This will result in a lower performance!", requiresMcRestart = true)
    public static boolean debug = false;

    /**
     * If the recipe loader should crash when finding invalid recipes.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "If the recipe loader should crash when finding invalid recipes.", requiresMcRestart = true)
    public static boolean crashOnInvalidRecipe = false;

    /**
     * If mod compatibility loader should crash hard if errors occur in that process.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "If mod compatibility loader should crash hard if errors occur in that process.", requiresMcRestart = true)
    public static boolean crashOnModCompatCrash = false;
    
    /**
     * The type of this config.
     */
    public static ConfigurableType TYPE = ConfigurableType.DUMMY;

    /**
     * The minimum array size of potion types, increase to allow for more potion types.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "The minimum array size of potion types, increase to allow for more potion types.", requiresMcRestart = true)
    public static int minimumPotionTypesArraySize = 256;
    
    /**
     * Create a new instance.
     */
    public GeneralConfig() {
        super(CyclopsCore._instance, true, "general", null, GeneralConfig.class);
    }
    
    @Override
    public void onRegistered() {
        // Check version of config file
        if(!version.equals(Reference.MOD_VERSION)) {
            System.err.println("The config file of " + Reference.MOD_NAME + " is out of date and might cause problems, please remove it so it can be regenerated.");
        }

        getMod().putGenericReference(ModBase.REFKEY_CRASH_ON_INVALID_RECIPE, GeneralConfig.crashOnInvalidRecipe);
        getMod().putGenericReference(ModBase.REFKEY_DEBUGCONFIG, GeneralConfig.debug);
        getMod().putGenericReference(ModBase.REFKEY_CRASH_ON_MODCOMPAT_CRASH, GeneralConfig.crashOnModCompatCrash);
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}
