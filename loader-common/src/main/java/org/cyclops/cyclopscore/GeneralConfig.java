package org.cyclops.cyclopscore;

import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfigCommon;
import org.cyclops.cyclopscore.helper.ModBaseCommon;

import java.util.UUID;

/**
 * A config with general options for this mod.
 * @author rubensworks
 *
 */
public class GeneralConfig extends DummyConfigCommon<ModBaseCommon<?>> {

    @ConfigurablePropertyCommon(category = "core", comment = "If an anonymous mod startup analytics request may be sent to our analytics service.")
    public static boolean analytics = true;

    @ConfigurablePropertyCommon(category = "core", comment = "The anonymous id used by the analytics service.")
    public static String anonymousAnalyticsID = UUID.randomUUID().toString();

    @ConfigurablePropertyCommon(category = "core", comment = "If the version checker should be enabled.")
    public static boolean versionChecker = true;

    @ConfigurablePropertyCommon(category = "general", comment = "When in a dev environment, if a button should be added to the main menu to open a dev world (shift-click creates a new world).", configLocation = ModConfigLocation.CLIENT)
    public static boolean devWorldButton = true;

    @ConfigurablePropertyCommon(category = "general", comment = "When in a dev environment, if music should be disabled at startup.", configLocation = ModConfigLocation.CLIENT)
    public static boolean devDisableMusic = true;

    /**
     * Create a new instance.
     */
    public GeneralConfig(ModBaseCommon<?> mod) {
        super(mod, "general");
    }

}
