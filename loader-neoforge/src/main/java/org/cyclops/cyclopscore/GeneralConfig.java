package org.cyclops.cyclopscore;

import net.neoforged.fml.config.ModConfig;
import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfigCommon;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.tracking.Analytics;
import org.cyclops.cyclopscore.tracking.Versions;

import java.util.UUID;

/**
 * A config with general options for this mod.
 * @author rubensworks
 *
 */
public class GeneralConfig extends DummyConfigCommon<ModBase<?>> {

    @ConfigurableProperty(category = "core", comment = "Set 'true' to enable development debug mode. This will result in a lower performance!", requiresMcRestart = true)
    public static boolean debug = false;

    @ConfigurableProperty(category = "core", comment = "If the recipe loader should crash when finding invalid recipes.", requiresMcRestart = true)
    public static boolean crashOnInvalidRecipe = false;

    @ConfigurableProperty(category = "core", comment = "If mod compatibility loader should crash hard if errors occur in that process.", requiresMcRestart = true)
    public static boolean crashOnModCompatCrash = false;

    @ConfigurableProperty(category = "core", comment = "If an anonymous mod startup analytics request may be sent to our analytics service.")
    public static boolean analytics = true;

    @ConfigurableProperty(category = "core", comment = "The anonymous id used by the analytics service.")
    public static String anonymousAnalyticsID = UUID.randomUUID().toString();

    @ConfigurableProperty(category = "core", comment = "If the version checker should be enabled.")
    public static boolean versionChecker = true;

    @ConfigurableProperty(category = "general", comment = "If a button should be added to the main menu to open a dev world (shift-click creates a new world).", configLocation = ModConfig.Type.CLIENT)
    public static boolean devWorldButton = MinecraftHelpers.isDevEnvironment();

    /**
     * Create a new instance.
     */
    public GeneralConfig() {
        super(CyclopsCore._instance, "general");
    }

    @Override
    public void onRegistered() {
        getMod().putGenericReference(ModBase.REFKEY_CRASH_ON_INVALID_RECIPE, GeneralConfig.crashOnInvalidRecipe);
        getMod().putGenericReference(ModBase.REFKEY_CRASH_ON_MODCOMPAT_CRASH, GeneralConfig.crashOnModCompatCrash);

        if(analytics) {
            Analytics.registerMod(getMod(), Reference.GA_TRACKING_ID);
        }
        if(versionChecker) {
            Versions.registerMod(getMod(), CyclopsCore._instance, "https://raw.githubusercontent.com/CyclopsMC/Versions/master/" + getMod().getModHelpers().getMinecraftHelpers().getMinecraftVersionMajorMinor() + "/CyclopsCore.txt");
        }
    }

}
