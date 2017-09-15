package org.cyclops.cyclopscore.config.configurable;

import lombok.experimental.Delegate;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Block that extends from a clog that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public class ConfigurableBlockLog extends BlockLog implements IConfigurableBlock {

    @Delegate private IBlockPropertyManager propertyManager;
    @Override protected BlockStateContainer createBlockState() {
        return (propertyManager = new BlockPropertyManagerComponent(this)).createDelegatedBlockState();
    }

    // This is to make sure that the MC properties are also loaded.
    @BlockProperty
    public static final IProperty[] _COMPAT = {LOG_AXIS};

    protected BlockConfig eConfig = null;
    protected boolean hasGui = false;

    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     */
    public ConfigurableBlockLog(ExtendedConfig<BlockConfig> eConfig) {
        this.setConfig((BlockConfig)eConfig); // TODO change eConfig to just be a BlockConfig
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
    }

    @Override
    public boolean hasGui() {
        return hasGui;
    }

    @Nullable
    @Override
    @SideOnly(Side.CLIENT)
    public IBlockColor getBlockColorHandler() {
        return null;
    }

    private void setConfig(BlockConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public BlockConfig getConfig() {
        return eConfig;
    }

    @SuppressWarnings("deprecation")
    @Nullable
    @Override
    public ItemStack getItem(World worldIn, BlockPos pos, IBlockState state) {
        return new ItemStack(this, 1, 0);
    }

    @Override
    public List<ItemStack> getDrops(IBlockAccess world, BlockPos blockPos, IBlockState blockStatedata, int fortune) {
        List<ItemStack> drops = new ArrayList<ItemStack>();
        drops.add(new ItemStack(this, 1, 0));
        return drops;
    }

    @Override
    public boolean canSilkHarvest(World world, BlockPos pos, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (!BlockHelpers.isValidCreativeTab(this, creativeTabs)) return;
        list.add(new ItemStack(this, 1, 0));
    }

}
