package org.cyclops.cyclopscore.advancement.criterion;

import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * @author rubensworks
 *
 */
public class ModItemObtainedTriggerConfig<M extends IModBase> extends CriterionTriggerConfigCommon<ModItemObtainedTrigger.Instance, M> {

    /**
     * The unique instance.
     */
    public static ModItemObtainedTriggerConfig _instance;

    /**
     * Make a new instance.
     */
    public ModItemObtainedTriggerConfig(M mod) {
        super(
                mod,
                "mod_item_obtained",
                new ModItemObtainedTrigger()
        );
    }

}
