package org.cyclops.cyclopscore.client.particle;

import net.minecraft.block.BlockState;
import net.minecraft.block.material.Material;
import net.minecraft.client.particle.IParticleRenderType;
import net.minecraft.client.particle.SpriteTexturedParticle;
import net.minecraft.fluid.IFluidState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;


/**
 * Particle that appears underneath blocks for simulating drops.
 */
@OnlyIn(Dist.CLIENT)
public class ParticleDropColored extends SpriteTexturedParticle {

    /**
     * The height of the current bob
     */
    private int bobTimer;

    public ParticleDropColored(ParticleDropColoredData data, World world, double x, double y, double z) {
        super(world, x, y, z);
        this.motionX = this.motionY = this.motionZ= 0.0D;

        this.particleRed = data.getRed();
        this.particleGreen = data.getGreen();
        this.particleBlue = data.getBlue();

        this.setSize(0.01F, 0.01F);
        this.particleGravity = 0.06F;
        this.bobTimer = 40;
        this.maxAge = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
        this.motionX = this.motionY = this.motionZ = 0.0D;
    }

    @Override
    public IParticleRenderType getRenderType() {
        return IParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;

        this.motionY -= (double) this.particleGravity;

        if (this.bobTimer-- > 0) {
            this.motionX *= 0.02D;
            this.motionY *= 0.02D;
            this.motionZ *= 0.02D;
        }

        this.move(this.motionX, this.motionY, this.motionZ);
        this.motionX *= 0.9800000190734863D;
        this.motionY *= 0.9800000190734863D;
        this.motionZ *= 0.9800000190734863D;

        if (this.maxAge-- <= 0) {
            this.setExpired();
        }

        if (this.onGround) {
            this.motionX *= 0.699999988079071D;
            this.motionZ *= 0.699999988079071D;
        }

        BlockPos blockPos = new BlockPos(MathHelper.floor(this.posX), MathHelper.floor(this.posY), MathHelper.floor(this.posZ));
        BlockState blockState = this.world.getBlockState(blockPos);
        Material material = blockState.getMaterial();

        if (material.isLiquid() || material.isSolid()) {
            float h = 1;
            IFluidState fluidState = world.getFluidState(blockPos);
            if(!fluidState.isEmpty()) {
                h = ((float) fluidState.getLevel()) / 8;
            }
            double d0 = (double) ((float) (MathHelper.floor(this.posY) + 1) - h);

            if (this.posY < d0) {
                this.setExpired();
            }
        }
    }
}
