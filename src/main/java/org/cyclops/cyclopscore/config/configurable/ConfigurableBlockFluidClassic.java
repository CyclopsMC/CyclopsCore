package org.cyclops.cyclopscore.config.configurable;

import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fluids.BlockFluidClassic;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.block.component.IEntityDropParticleFXBlock;
import org.cyclops.cyclopscore.block.component.ParticleDropBlockComponent;
import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Random;

/**
 * Block that represents an in-world fluid that can hold ExtendedConfigs
 * @author rubensworks
 *
 */
public abstract class ConfigurableBlockFluidClassic extends BlockFluidClassic implements IConfigurableBlock, IEntityDropParticleFXBlock {
    
	private Fluid fluid;
	
    protected BlockConfig eConfig = null;
    protected boolean hasGui = false;
    
    @SideOnly(Side.CLIENT)
    protected ParticleDropBlockComponent particleDropBlockComponent;
    
    /**
     * Make a new blockState instance.
     * @param eConfig Config for this blockState.
     * @param fluid The fluid this blockState has to represent
     * @param material Material of this blockState.
     */
    public ConfigurableBlockFluidClassic(ExtendedConfig<BlockConfig> eConfig, Fluid fluid, Material material) {
        super(fluid, material);
        this.setConfig((BlockConfig)eConfig); // TODO change eConfig to just be a BlockConfig
        this.setTranslationKey(eConfig.getTranslationKey());
        fluid.setBlock(this);
        this.fluid = fluid;
    }

    @Nullable
    @Override
    public AxisAlignedBB getCollisionBoundingBox(@Nonnull IBlockState blockState, @Nonnull IBlockAccess worldIn, @Nonnull BlockPos pos) {
        return NULL_AABB;
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

    /**
     * @return The associated fluid.
     */
    public Fluid getFluid() {
        return this.fluid;
    }

    private void setConfig(BlockConfig eConfig) {
        this.eConfig = eConfig;
    }

    @Override
    public BlockConfig getConfig() {
        return eConfig;
    }
    
    /**
     * Set the drop particle color.
     * @param particleRed Red color.
     * @param particleGreen Green color.
     * @param particleBlue Blue color.
     * @return This instance of the blockState.
     */
    @SideOnly(Side.CLIENT)
    public ConfigurableBlockFluidClassic setParticleColor(float particleRed, float particleGreen, float particleBlue) {
        particleDropBlockComponent = new ParticleDropBlockComponent(particleRed, particleGreen, particleBlue);
        return this;
    }
    
    @Override
    @SideOnly(Side.CLIENT)
    /**
     * For the particle effects underneath a blockState that has the liquid on top.
     */
    public void randomDisplayTick(IBlockState blockState, World world, BlockPos blockPos, Random rand) {
        super.randomDisplayTick(blockState, world, blockPos, rand);
        if(particleDropBlockComponent != null)
            particleDropBlockComponent.randomDisplayTick(blockState, world, blockPos, rand);
    }

}
