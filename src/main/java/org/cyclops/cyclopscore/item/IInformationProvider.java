package org.cyclops.cyclopscore.item;

import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.TextFormatting;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

import java.util.List;

/**
 * Interface that can be applied to {@link net.minecraft.block.Block} or {@link net.minecraft.item.Item} so that they can provide information
 * when the player hovers over the item in their inventory.
 * @see InformationProviderComponent
 * @author rubensworks
 *
 */
public interface IInformationProvider {
    
    /**
     * A prefix for blockState information.
     */
    public static TextFormatting BLOCK_PREFIX = TextFormatting.RED;
    /**
     * A prefix for item information.
     */
    public static TextFormatting ITEM_PREFIX = BLOCK_PREFIX;
    /**
     * A prefix for additional info.
     */
    public static String INFO_PREFIX = TextFormatting.DARK_PURPLE.toString() + TextFormatting.ITALIC.toString();
    /**
     * Additional info prefix styles
     */
    public static TextFormatting[] INFO_PREFIX_STYLES = new TextFormatting[]{
            TextFormatting.DARK_PURPLE,
            TextFormatting.ITALIC
    };
    
    /**
     * Get info for a given itemStack.
     * @param itemStack The itemStack that must be given information.
     * @return Information for that itemStack.
     */
    public IFormattableTextComponent getInfo(ItemStack itemStack);
    /**
     * An extended way to provide additional information.
     * @param itemStack The itemStack that must be given information.
     * @param world The player that asks for information.
     * @param list The list of information.
     * @param flag The tooltip flag type.
     */
    @OnlyIn(Dist.CLIENT)
    public void provideInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flag);
}
