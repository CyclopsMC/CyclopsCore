package org.cyclops.cyclopscore.config.configurable;

import lombok.experimental.Delegate;
import net.minecraft.block.Block;
import net.minecraft.block.BlockSapling;
import net.minecraft.block.SoundType;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.property.BlockProperty;
import org.cyclops.cyclopscore.block.property.BlockPropertyManagerComponent;
import org.cyclops.cyclopscore.block.property.IBlockPropertyManager;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.BlockHelpers;
import org.cyclops.cyclopscore.world.gen.WorldGeneratorTree;

import javax.annotation.Nullable;
import java.util.Random;

/**
 * Block extending from a sapling that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public class ConfigurableBlockSapling extends BlockSapling implements IConfigurableBlock {

    @Delegate private IBlockPropertyManager propertyManager;
    @Override protected BlockStateContainer createBlockState() {
        return (propertyManager = new BlockPropertyManagerComponent(this)).createDelegatedBlockState();
    }

    // This is to make sure that the MC properties are also loaded.
    @BlockProperty
    public static final IProperty[] _COMPAT = {STAGE};
    @BlockProperty(ignore = true)
    public static final IProperty[] _COMPAT_IGNORED = {TYPE};

    protected ExtendedConfig<BlockConfig, Block> eConfig = null;
    protected boolean hasGui = false;

    private WorldGeneratorTree treeGenerator;

    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param material Material of this blockState.
     * @param treeGenerator The world generator of the tree.
     */
    public ConfigurableBlockSapling(ExtendedConfig<BlockConfig, Block> eConfig, Material material, WorldGeneratorTree treeGenerator) {
        this.setConfig(eConfig);
        this.setUnlocalizedName(eConfig.getUnlocalizedName());
        this.treeGenerator = treeGenerator;
        setSoundType(SoundType.GROUND);
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

    private void setConfig(ExtendedConfig<BlockConfig, Block> eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public ExtendedConfig<BlockConfig, Block> getConfig() {
        return eConfig;
    }

    @Override
    public void getSubBlocks(CreativeTabs creativeTabs, NonNullList<ItemStack> list) {
        if (!BlockHelpers.isValidCreativeTab(this, creativeTabs)) return;
        list.add(new ItemStack(this, 1, 0));
    }

    @Override
    public void generateTree(World world, BlockPos pos, IBlockState state, Random rand) {
        if (!net.minecraftforge.event.terraingen.TerrainGen.saplingGrowTree(world, rand, pos)) return;
        if (world.isRemote) {
            return;
        }

        world.setBlockToAir(pos);

        if(!treeGenerator.growTree(world, rand, pos)) {
            world.setBlockState(pos, getDefaultState(), 4);
        }
    }

}
