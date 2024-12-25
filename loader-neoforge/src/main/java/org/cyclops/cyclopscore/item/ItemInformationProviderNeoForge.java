package org.cyclops.cyclopscore.item;

import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.cyclops.cyclopscore.helper.ModHelpersNeoForge;

/**
 * This is responsible for adding "show more information" tooltips to registered items.
 * @author rubensworks
 */
public class ItemInformationProviderNeoForge extends ItemInformationProviderCommon {

    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (ITEMS_INFO.contains(itemStack.getItem())) {
            ModHelpersNeoForge.INSTANCE.getL10NHelpers().addOptionalInfo(event.getToolTip(), itemStack.getItem().getDescriptionId());
        }
    }

}
