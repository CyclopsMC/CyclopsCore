package org.cyclops.cyclopscore.advancement.criterion;

import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfigCommon;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * @author rubensworks
 *
 */
public class GuiContainerOpenTriggerConfig extends CriterionTriggerConfigCommon<GuiContainerOpenTrigger.Instance, ModBase<?>> {

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
