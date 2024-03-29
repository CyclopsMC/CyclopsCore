package org.cyclops.cyclopscore.advancement.criterion;

import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfig;

/**
 * @author rubensworks
 *
 */
public class ItemCraftedTriggerConfig extends CriterionTriggerConfig<ItemCraftedTrigger.Instance> {

    /**
     * The unique instance.
     */
    public static ItemCraftedTriggerConfig _instance;

    /**
     * Make a new instance.
     */
    public ItemCraftedTriggerConfig() {
        super(
                CyclopsCore._instance,
                "item_crafted",
                new ItemCraftedTrigger()
        );
    }

}
