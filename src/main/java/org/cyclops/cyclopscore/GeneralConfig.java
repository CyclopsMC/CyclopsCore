package org.cyclops.cyclopscore;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurableType;
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
    @ConfigurableProperty(category = "core", comment = "The version at which the config file was generated.", showInGui = false)
    public static String version = Reference.MOD_VERSION;
    
    /**
     * If the debug mode should be enabled. @see Debug
     */
    @ConfigurableProperty(category = "core", comment = "Set 'true' to enable development debug mode. This will result in a lower performance!", requiresMcRestart = true)
    public static boolean debug = false;

    /**
     * If the recipe loader should crash when finding invalid recipes.
     */
    @ConfigurableProperty(category = "core", comment = "If the recipe loader should crash when finding invalid recipes.", requiresMcRestart = true)
    public static boolean crashOnInvalidRecipe = false;

    /**
     * If mod compatibility loader should crash hard if errors occur in that process.
     */
    @ConfigurableProperty(category = "core", comment = "If mod compatibility loader should crash hard if errors occur in that process.", requiresMcRestart = true)
    public static boolean crashOnModCompatCrash = false;

    /**
     * If an anonymous mod startup analytics request may be sent to our analytics service.
     */
    @ConfigurableProperty(category = "core", comment = "If an anonymous mod startup analytics request may be sent to our analytics service.")
    public static boolean analytics = true;

    /**
     * The anonymous id used by the analytics service.
     */
    @ConfigurableProperty(category = "core", comment = "The anonymous id used by the analytics service.")
    public static String anonymousAnalyticsID = UUID.randomUUID().toString();

    /**
     * If the version checker should be enabled.
     */
    @ConfigurableProperty(category = "core", comment = "If the version checker should be enabled.")
    public static boolean versionChecker = true;

    /**
     * The minimum array size of potion types, increase to allow for more potion types.
     */
    @ConfigurableProperty(category = "core", comment = "The minimum array size of potion types, increase to allow for more potion types.", minimalValue=256, maximalValue=2560, requiresMcRestart = true)
    public static int minimumPotionTypesArraySize = 256;

    /**
     * If a button should be added to the main menu to open a dev world (shift-click creates a new world).
     */
    @ConfigurableProperty(category = "general", comment = "If a button should be added to the main menu to open a dev world (shift-click creates a new world).")
    public static boolean devWorldButton = true; // TODO: (Boolean) Launcher.INSTANCE.blackboard().get("fml.deobfuscatedEnvironment");

    /**
     * The type of this config.
     */
    public static ConfigurableType TYPE = ConfigurableType.DUMMY;
    
    /**
     * Create a new instance.
     */
    public GeneralConfig() {
        super(CyclopsCore._instance, true, "general", null);
    }
    
    @Override
    public void onRegistered() {
        getMod().putGenericReference(ModBase.REFKEY_CRASH_ON_INVALID_RECIPE, GeneralConfig.crashOnInvalidRecipe);
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
