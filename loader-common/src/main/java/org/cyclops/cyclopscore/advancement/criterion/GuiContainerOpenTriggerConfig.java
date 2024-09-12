package org.cyclops.cyclopscore.advancement.criterion;

import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * @author rubensworks
 *
 */
public class GuiContainerOpenTriggerConfig<M extends IModBase> extends CriterionTriggerConfigCommon<GuiContainerOpenTrigger.Instance, M> {

    /**
     * The unique instance.
     */
    public static GuiContainerOpenTriggerConfig _instance;

    /**
     * Make a new instance.
     */
    public GuiContainerOpenTriggerConfig(M mod) {
        super(
                mod,
                "container_gui_open",
                new GuiContainerOpenTrigger()
        );
    }

}
