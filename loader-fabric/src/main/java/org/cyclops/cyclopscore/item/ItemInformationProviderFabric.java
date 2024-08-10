package org.cyclops.cyclopscore.item;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.cyclops.cyclopscore.CyclopsCoreMainFabric;

import java.util.List;

/**
 * This is responsible for adding "show more information" tooltips to registered items.
 * @author rubensworks
 */
public class ItemInformationProviderFabric extends ItemInformationProviderCommon {
    public static void onTooltip(ItemStack itemStack, Item.TooltipContext tooltipContext, TooltipFlag tooltipFlag, List<Component> lines) {
        if (ITEMS_INFO.contains(itemStack.getItem())) {
            CyclopsCoreMainFabric._instance.getModHelpers().getL10NHelpers().addOptionalInfo(lines, itemStack.getDescriptionId());
        }
    }
}
