package org.cyclops.cyclopscore.advancement.criterion;

import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfigCommon;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * @author rubensworks
 *
 */
public class ModItemObtainedTriggerConfig extends CriterionTriggerConfigCommon<ModItemObtainedTrigger.Instance, ModBase<?>> {

    /**
     * The unique instance.
     */
    public static ModItemObtainedTriggerConfig _instance;

    /**
     * Make a new instance.
     */
    public ModItemObtainedTriggerConfig() {
        super(
                CyclopsCore._instance,
                "mod_item_obtained",
                new ModItemObtainedTrigger()
        );
    }

}
