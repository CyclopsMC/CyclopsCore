package org.cyclops.cyclopscore;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfig;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.tracking.Analytics;
import org.cyclops.cyclopscore.tracking.Versions;

import java.util.UUID;

/**
 * A config with general options for this mod.
 * @author rubensworks
 *
 */
public class GeneralConfig extends DummyConfig {
    
    /**
     * The version at which the config file was generated.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "The version at which the config file was generated.", showInGui = false)
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
     * If an anonymous mod startup analytics request may be sent to our analytics service.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "If an anonymous mod startup analytics request may be sent to our analytics service.")
    public static boolean analytics = true;

    /**
     * The anonymous id used by the analytics service.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "The anonymous id used by the analytics service.")
    public static String anonymousAnalyticsID = UUID.randomUUID().toString();

    /**
     * If the version checker should be enabled.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "If the version checker should be enabled.")
    public static boolean versionChecker = true;

    /**
     * The minimum array size of potion types, increase to allow for more potion types.
     */
    @ConfigurableProperty(category = ConfigurableTypeCategory.CORE, comment = "The minimum array size of potion types, increase to allow for more potion types.", minimalValue=256, maximalValue=2560, requiresMcRestart = true)
    public static int minimumPotionTypesArraySize = 256;

    /**
     * The type of this config.
     */
    public static ConfigurableType TYPE = ConfigurableType.DUMMY;
    
    /**
     * Create a new instance.
     */
    public GeneralConfig() {
        super(CyclopsCore._instance, true, "general", null, GeneralConfig.class);
    }
    
    @Override
    public void onRegistered() {
        getMod().putGenericReference(ModBase.REFKEY_CRASH_ON_INVALID_RECIPE, GeneralConfig.crashOnInvalidRecipe);
        getMod().putGenericReference(ModBase.REFKEY_DEBUGCONFIG, GeneralConfig.debug);
        getMod().putGenericReference(ModBase.REFKEY_CRASH_ON_MODCOMPAT_CRASH, GeneralConfig.crashOnModCompatCrash);

        if(analytics) {
            Analytics.registerMod(getMod(), Reference.GA_TRACKING_ID);
        }
        if(versionChecker) {
            Versions.registerMod(getMod(), CyclopsCore._instance, Reference.VERSION_URL);
        }
    }
    
    @Override
    public boolean isEnabled() {
        return true;
    }
}
