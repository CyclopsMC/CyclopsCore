package org.cyclops.cyclopscore.item;

import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.cyclops.cyclopscore.helper.ModHelpersForge;

/**
 * This is responsible for adding "show more information" tooltips to registered items.
 * @author rubensworks
 */
public class ItemInformationProviderForge extends ItemInformationProviderCommon {

    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (ITEMS_INFO.contains(itemStack.getItem())) {
            ModHelpersForge.INSTANCE.getL10NHelpers().addOptionalInfo(event.getToolTip(), itemStack.getDescriptionId());
        }
    }

}
