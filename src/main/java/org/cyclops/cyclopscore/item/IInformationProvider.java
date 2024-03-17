package org.cyclops.cyclopscore.item;

import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.network.chat.Component;
import net.minecraft.ChatFormatting;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;

import java.util.List;

/**
 * Interface that can be applied to {@link net.minecraft.world.level.block.Block} or {@link net.minecraft.world.item.Item} so that they can provide information
 * when the player hovers over the item in their inventory.
 * @see InformationProviderComponent
 * @author rubensworks
 *
 */
public interface IInformationProvider {

    /**
     * A prefix for blockState information.
     */
    public static ChatFormatting BLOCK_PREFIX = ChatFormatting.RED;
    /**
     * A prefix for item information.
     */
    public static ChatFormatting ITEM_PREFIX = BLOCK_PREFIX;
    /**
     * A prefix for additional info.
     */
    public static String INFO_PREFIX = ChatFormatting.DARK_PURPLE.toString() + ChatFormatting.ITALIC.toString();
    /**
     * Additional info prefix styles
     */
    public static ChatFormatting[] INFO_PREFIX_STYLES = new ChatFormatting[]{
            ChatFormatting.DARK_PURPLE,
            ChatFormatting.ITALIC
    };

    /**
     * Get info for a given itemStack.
     * @param itemStack The itemStack that must be given information.
     * @return Information for that itemStack.
     */
    public MutableComponent getInfo(ItemStack itemStack);
    /**
     * An extended way to provide additional information.
     * @param itemStack The itemStack that must be given information.
     * @param world The player that asks for information.
     * @param list The list of information.
     * @param flag The tooltip flag type.
     */
    @OnlyIn(Dist.CLIENT)
    public void provideInformation(ItemStack itemStack, Level world, List<Component> list, TooltipFlag flag);
}
