package org.cyclops.cyclopscore.client.particle;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.ParticleRenderType;
import net.minecraft.client.particle.TextureSheetParticle;
import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;


/**
 * Particle that appears underneath blocks for simulating drops.
 */
@OnlyIn(Dist.CLIENT)
public class ParticleDropColored extends TextureSheetParticle {

    /**
     * The height of the current bob
     */
    private int bobTimer;

    public ParticleDropColored(ParticleDropColoredData data, ClientLevel world, double x, double y, double z) {
        super(world, x, y, z);
        this.xd = this.yd = this.zd= 0.0D;

        this.rCol = data.getRed();
        this.gCol = data.getGreen();
        this.bCol = data.getBlue();

        this.setSize(0.01F, 0.01F);
        this.gravity = 0.06F;
        this.bobTimer = 40;
        this.lifetime = (int) (64.0D / (Math.random() * 0.8D + 0.2D));
        this.xd = this.yd = this.zd = 0.0D;
    }

    @Override
    public ParticleRenderType getRenderType() {
        return ParticleRenderType.PARTICLE_SHEET_TRANSLUCENT;
    }

    @Override
    public void tick() {
        this.xo = this.x;
        this.yo = this.y;
        this.zo = this.z;

        this.yd -= (double) this.gravity;

        if (this.bobTimer-- > 0) {
            this.xd *= 0.02D;
            this.yd *= 0.02D;
            this.zd *= 0.02D;
        }

        this.move(this.xd, this.yd, this.zd);
        this.xd *= 0.9800000190734863D;
        this.yd *= 0.9800000190734863D;
        this.zd *= 0.9800000190734863D;

        if (this.lifetime-- <= 0) {
            this.remove();
        }

        if (this.onGround) {
            this.xd *= 0.699999988079071D;
            this.zd *= 0.699999988079071D;
        }

        BlockPos blockPos = new BlockPos(Mth.floor(this.x), Mth.floor(this.y), Mth.floor(this.z));
        BlockState blockState = this.level.getBlockState(blockPos);

        if (blockState.liquid() || blockState.isSolid()) {
            float h = 1;
            FluidState fluidState = level.getFluidState(blockPos);
            if(!fluidState.isEmpty()) {
                h = ((float) fluidState.getAmount()) / 8;
            }
            double d0 = (double) ((float) (Mth.floor(this.y) + 1) - h);

            if (this.y < d0) {
                this.remove();
            }
        }
    }
}
