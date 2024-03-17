package org.cyclops.cyclopscore.advancement.criterion;

import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfig;

/**
 * @author rubensworks
 *
 */
public class GuiContainerOpenTriggerConfig extends CriterionTriggerConfig<GuiContainerOpenTrigger.Instance> {

    /**
     * The unique instance.
     */
    public static GuiContainerOpenTriggerConfig _instance;

    /**
     * Make a new instance.
     */
    public GuiContainerOpenTriggerConfig() {
        super(
                CyclopsCore._instance,
                "container_gui_open",
                new GuiContainerOpenTrigger()
        );
    }

}
