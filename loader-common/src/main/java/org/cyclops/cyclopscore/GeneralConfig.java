package org.cyclops.cyclopscore;

import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
import org.cyclops.cyclopscore.config.ModConfigLocation;
import org.cyclops.cyclopscore.config.extendedconfig.DummyConfigCommon;
import org.cyclops.cyclopscore.helper.ModBaseCommon;

/**
 * A config with general options for this mod.
 * @author rubensworks
 *
 */
public class GeneralConfig extends DummyConfigCommon<ModBaseCommon<?>> {

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
