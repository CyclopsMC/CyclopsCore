package org.cyclops.cyclopscore.item;

import net.minecraft.block.Block;
import net.minecraft.client.util.ITooltipFlag;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.IBlockRarityProvider;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockDoor;
import org.cyclops.cyclopscore.helper.L10NHelpers;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A hybrid of {@link org.cyclops.cyclopscore.item.ItemBlockMetadata} and {@link net.minecraft.item.ItemDoor}.
 * @author josephcsible
 *
 */
public class ItemDoorMetadata extends ItemDoor {
    protected InformationProviderComponent informationProvider;
    protected IBlockRarityProvider rarityProvider = null;

    protected final ConfigurableBlockDoor block;
    public ItemDoorMetadata(Block block) {
        super(block);
        this.block = (ConfigurableBlockDoor) block;
        this.block.item = this;
        informationProvider = new InformationProviderComponent(block);
        if(block instanceof IBlockRarityProvider) {
            rarityProvider = (IBlockRarityProvider) block;
        }
    }

    @Override
    public String getTranslationKey(ItemStack stack)
    {
        return block.getTranslationKey();
    }

    @Override
    public String getTranslationKey()
    {
        return block.getTranslationKey();
    }

    @Override
    public CreativeTabs getCreativeTab()
    {
        return block.getCreativeTab();
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack itemStack, @Nullable World world, List<String> list, ITooltipFlag flag)
    {
        super.addInformation(itemStack, world, list, flag);
        block.addInformation(itemStack, world, list, flag);
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
}
