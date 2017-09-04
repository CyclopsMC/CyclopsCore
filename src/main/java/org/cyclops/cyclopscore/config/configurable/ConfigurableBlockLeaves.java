package org.cyclops.cyclopscore.config.configurable;

import com.google.common.collect.Lists;
import lombok.experimental.Delegate;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.block.property.BlockPropertyManagerComponent;
import org.cyclops.cyclopscore.block.property.IBlockPropertyManager;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Random;

/**
 * Block that extends from BlockLeaves that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockLeaves extends BlockLeaves implements IConfigurableBlock {

    @Delegate private IBlockPropertyManager propertyManager;
    @Override protected BlockStateContainer createBlockState() {
        return (propertyManager = new BlockPropertyManagerComponent(this)).createDelegatedBlockState();
    }

    // This is to make sure that the MC properties are also loaded.
    @BlockProperty(ignore = true)
    public static final IProperty[] _COMPAT = {DECAYABLE, CHECK_DECAY};

    protected ExtendedConfig<BlockConfig> eConfig = null;
    protected boolean hasGui = false;
    
    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     */
    public ConfigurableBlockLeaves(ExtendedConfig<BlockConfig> eConfig) {
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColorHandler() {
        return null;
    }

    @Override
    public boolean hasGui() {
        return hasGui;
    }

    private void setConfig(ExtendedConfig<BlockConfig> eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<BlockConfig> getConfig() {
        return eConfig;
    }

    @Override
    public abstract Item getItemDropped(IBlockState blockState, Random random, int zero);

    @Override
    public void dropBlockAsItemWithChance(World world, BlockPos blockPos, IBlockState blockState, float chance, int fortune) {
        if(!world.isRemote) {
            List<ItemStack> items = getDrops(world, blockPos, blockState, fortune);

            for (ItemStack item : items) {
                if (world.rand.nextFloat() <= chance) {
                    this.spawnAsEntity(world, blockPos, item);
                }
            }
        }
    }
    
    @Override
    public boolean isOpaqueCube(IBlockState blockState) {
        return false;
    }
    
    @Override
    public boolean isNormalCube(IBlockState blockState, IBlockAccess world, BlockPos blockPos) {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess world, BlockPos blockPos, EnumFacing side) {
        return !(blockState.isTranslucent()) || super.shouldSideBeRendered(blockState, world, blockPos, side);
    }

    @Override
    public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (!BlockHelpers.isValidCreativeTab(this, creativeTabs)) return;
        list.add(new ItemStack(this, 1, 0));
    }

    @Override
    public boolean isLeaves(IBlockState blockState, IBlockAccess world, BlockPos blockPos) {
        return true;
    }

    @Override
    public BlockPlanks.EnumType getWoodType(int meta) {
        return null;
    }

    @Override
    public BlockRenderLayer getBlockLayer() {
        return BlockRenderLayer.CUTOUT_MIPPED;
    }

    @Override
    public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune) {
        return Lists.newArrayList(new ItemStack(this));
    }
}
