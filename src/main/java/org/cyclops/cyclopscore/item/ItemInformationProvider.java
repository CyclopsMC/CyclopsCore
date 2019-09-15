package org.cyclops.cyclopscore.item;

import com.google.common.collect.Sets;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.helper.L10NHelpers;

import java.util.Set;

/**
 * This is responsible for adding "show more information" tooltips to registered items.
 * @author rubensworks
 */
public class ItemInformationProvider {

    private static final Set<Item> ITEMS_INFO = Sets.newIdentityHashSet();

    static {
        MinecraftForge.EVENT_BUS.register(ItemInformationProvider.class);
    }

    public static void registerItem(Item item) {
        ITEMS_INFO.add(item);
    }

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public static void onTooltip(ItemTooltipEvent event) {
        ItemStack itemStack = event.getItemStack();
        if (ITEMS_INFO.contains(itemStack.getItem())) {
            L10NHelpers.addOptionalInfo(event.getToolTip(), itemStack.getTranslationKey());
        }
    }

}
