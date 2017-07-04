package org.cyclops.cyclopscore;

import org.cyclops.cyclopscore.advancement.criterion.ItemCraftedTrigger;
import org.cyclops.cyclopscore.advancement.criterion.ModItemObtainedTrigger;
import org.cyclops.cyclopscore.helper.AdvancementHelpers;

/**
 * Advancement-related logic.
 * @author rubensworks
 */
public class Advancements {

    public static final ItemCraftedTrigger ITEM_CRAFTED = AdvancementHelpers
            .registerCriteriaTrigger(new ItemCraftedTrigger());
    public static final ModItemObtainedTrigger MOD_ITEM_OBTAINED = AdvancementHelpers
            .registerCriteriaTrigger(new ModItemObtainedTrigger());

    public static void load() {}

}
