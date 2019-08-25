package org.cyclops.cyclopscore.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Packet for sending and showing the ring of fire.
 * 
 * @author rubensworks
 *
 */
public class RingOfFirePacket extends PlayerPositionPacket {

    private static final double RING_AREA = 0.9F;

    public RingOfFirePacket() {
        super();
    }

    public RingOfFirePacket(PlayerEntity player) {
        super(player);
    }
    
	@OnlyIn(Dist.CLIENT)
    private static void showFireRing(World world, Vec3d pos) {
        double area = RING_AREA;
        int points = 40;
        for(double point = -points; point <= points; point++) {
            double u = 2 * Math.PI * (point / points);

            double xOffset = Math.cos(u) * area;
            double yOffset = 0F;
            double zOffset = Math.sin(u) * area;

            double x = pos.x;
            double y = pos.y;
            double z = pos.z;

            double particleX = x + xOffset + world.rand.nextFloat() / 5;
            double particleY = y + yOffset + world.rand.nextFloat() / 5;
            double particleZ = z + zOffset + world.rand.nextFloat() / 5;

            float particleMotionX = (float)xOffset / 50;
            float particleMotionY = 0.01F;
            float particleMotionZ = (float)zOffset / 50;

            if(world.rand.nextInt(20) == 0) {
                world.addParticle(ParticleTypes.LAVA, particleX, particleY, particleZ, 0.0D, 0.0D, 0.0D);
            } else {
                world.addParticle(ParticleTypes.FLAME, particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
            }
        }
    }

    @Override
    protected PlayerPositionPacket create(PlayerEntity player, int range) {
        return new RingOfFirePacket(player);
    }

    @Override
    protected ModBase getModInstance() {
        return CyclopsCore._instance;
    }

    @Override
    protected void performClientAction(World world, PlayerEntity player) {
        showFireRing(world, this.position);
    }
}
