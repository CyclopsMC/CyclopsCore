package org.cyclops.cyclopscore.advancement.criterion;

import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * @author rubensworks
 *
 */
public class ItemCraftedTriggerConfig<M extends IModBase> extends CriterionTriggerConfigCommon<ItemCraftedTrigger.Instance, M> {

    /**
     * The unique instance.
     */
    public static ItemCraftedTriggerConfig _instance;

    /**
     * Make a new instance.
     */
    public ItemCraftedTriggerConfig(M mod) {
        super(
                mod,
                "item_crafted",
                new ItemCraftedTrigger()
        );
    }

}
