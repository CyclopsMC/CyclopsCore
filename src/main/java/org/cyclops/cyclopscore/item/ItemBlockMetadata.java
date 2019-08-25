package org.cyclops.cyclopscore.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Rarity;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.block.IBlockRarityProvider;
import org.cyclops.cyclopscore.helper.L10NHelpers;

import java.util.List;

/**
 * An extended {@link BlockItem} that will automatically add information to the blockState
 * item if that blockState implements {@link IInformationProvider}.
 * @author rubensworks
 *
 */
public class ItemBlockMetadata extends BlockItem {
    
    protected InformationProviderComponent informationProvider;
    protected IBlockRarityProvider rarityProvider = null;

    /**
     * Make a new instance.
     * @param block The blockState instance.
     * @param builder Item properties builder.
     */
    public ItemBlockMetadata(Block block, Item.Properties builder) {
        super(block, builder);
        informationProvider = new InformationProviderComponent(block);
        if(block instanceof IBlockRarityProvider) {
            rarityProvider = (IBlockRarityProvider) block;
        }
    }

    @OnlyIn(Dist.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<ITextComponent> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        L10NHelpers.addOptionalInfo(list, getTranslationKey());
    	informationProvider.addInformation(itemStack, world, list, flag);
    }

    @Override
    public Rarity getRarity(ItemStack itemStack) {
        if(rarityProvider != null) {
            return rarityProvider.getRarity(itemStack);
        }
        return super.getRarity(itemStack);
    }

}
