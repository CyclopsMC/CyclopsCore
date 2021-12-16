package org.cyclops.cyclopscore.block.component;

import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.client.particle.ParticleDropColoredData;
import org.cyclops.cyclopscore.helper.BlockHelpers;

import java.util.Random;

/**
 * Component that can show drops of a certain color underneath blocks.
 * This has by default the same behaviour as lava and water drops underneath the blocks underneath them.
 * But this offset can be altered.
 * @author rubensworks
 *
 */
public class ParticleDropBlockComponent implements IEntityDropParticleFXBlock {

    protected float particleRed;
    protected float particleGreen;
    protected float particleBlue;

    protected int offset = 1;
    protected int chance = 10;

    /**
     * Make a new instance.
     * @param particleRed Red color.
     * @param particleGreen Green color.
     * @param particleBlue Blue color.
     */
    public ParticleDropBlockComponent(float particleRed, float particleGreen, float particleBlue) {
        this.particleRed = particleRed;
        this.particleGreen = particleGreen;
        this.particleBlue = particleBlue;
    }

    /**
     * Set the offset to a lower Y where the drops should appear.
     * @param offset Amount of blocks to a lower Y.
     */
    public void setOffset(int offset) {
        this.offset = offset;
    }

    /**
     * Sets the chance for drop particles.
     * @param chance Every tick there will be a 1/chance chance.
     */
    public void setChance(int chance) {
        this.chance = chance;
    }

    @Override
    public void randomDisplayTick(BlockState blockState, Level world, BlockPos blockPos, Random rand) {
        if (rand.nextInt(chance) == 0 &&
                (offset == 0 || BlockHelpers.doesBlockHaveSolidTopSurface(world, blockPos.offset(0, -offset, 0))) &&
                !world.getBlockState(blockPos.offset(0, - offset - 1, 0)).getMaterial().blocksMotion()) {
            double px = (double) ((float) blockPos.getX() + rand.nextFloat());
            double py = (double) blockPos.getY() - 0.05D - offset;
            double pz = (double) ((float) blockPos.getZ() + rand.nextFloat());

            Minecraft.getInstance().levelRenderer.addParticle(
                    new ParticleDropColoredData(particleRed, particleGreen, particleBlue), false,
                    px, py, pz, 0, 0, 0);
        }
    }

}
