package org.cyclops.cyclopscore.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.IBlockRarityProvider;
import org.cyclops.cyclopscore.helper.L10NHelpers;

import java.util.List;

/**
 * An extended {@link net.minecraft.item.ItemBlock} that will automatically add information to the blockState
 * item if that blockState implements {@link IInformationProvider}.
 * @author rubensworks
 *
 */
public class ItemBlockMetadata extends ItemBlock {
    
    protected InformationProviderComponent informationProvider;
    protected IBlockRarityProvider rarityProvider = null;

    /**
     * Make a new instance.
     * @param block The blockState instance.
     */
    public ItemBlockMetadata(Block block) {
        super(block);
        this.setHasSubtypes(block.getBlockState().getValidStates().size() > 1);
        informationProvider = new InformationProviderComponent(block);
        if(block instanceof IBlockRarityProvider) {
            rarityProvider = (IBlockRarityProvider) block;
        }
    }
    
    @SideOnly(Side.CLIENT)
    @Override
    public void addInformation(ItemStack itemStack, World world, List<String> list, ITooltipFlag flag) {
        super.addInformation(itemStack, world, list, flag);
        L10NHelpers.addOptionalInfo(list, getTranslationKey());
    	informationProvider.addInformation(itemStack, world, list, flag);
    }

    @Override
    public EnumRarity getRarity(ItemStack itemStack) {
        if(rarityProvider != null) {
            return rarityProvider.getRarity(itemStack);
        }
        return super.getRarity(itemStack);
    }

    @Override
    public int getMetadata(int damage) {
        return damage;
    }

}
