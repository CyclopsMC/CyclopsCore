package org.cyclops.cyclopscore.item;

import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.ItemTooltipEvent;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

/**
 * This is responsible for adding "show more information" tooltips to registered items.
 * @author rubensworks
 */
public class ItemInformationProviderNeoForge extends ItemInformationProviderCommon {

    static {
        if (MinecraftHelpers.isClientSide()) {
            NeoForge.EVENT_BUS.addListener(ItemInformationProviderNeoForge::onTooltip);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (ITEMS_INFO.contains(itemStack.getItem())) {
            L10NHelpers.addOptionalInfo(event.getToolTip(), itemStack.getDescriptionId());
        }
    }

}
