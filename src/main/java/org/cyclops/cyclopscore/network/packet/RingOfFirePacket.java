package org.cyclops.cyclopscore.network.packet;

import net.minecraft.world.entity.player.Player;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.level.Level;
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

    public RingOfFirePacket(Player player) {
        super(player);
    }
    
	@OnlyIn(Dist.CLIENT)
    private static void showFireRing(Level world, Vec3 pos) {
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

            double particleX = x + xOffset + world.random.nextFloat() / 5;
            double particleY = y + yOffset + world.random.nextFloat() / 5;
            double particleZ = z + zOffset + world.random.nextFloat() / 5;

            float particleMotionX = (float)xOffset / 50;
            float particleMotionY = 0.01F;
            float particleMotionZ = (float)zOffset / 50;

            if(world.random.nextInt(20) == 0) {
                world.addParticle(ParticleTypes.LAVA, particleX, particleY, particleZ, 0.0D, 0.0D, 0.0D);
            } else {
                world.addParticle(ParticleTypes.FLAME, particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ);
            }
        }
    }

    @Override
    protected PlayerPositionPacket create(Player player, int range) {
        return new RingOfFirePacket(player);
    }

    @Override
    protected ModBase getModInstance() {
        return CyclopsCore._instance;
    }

    @Override
    protected void performClientAction(Level level, Player player) {
        showFireRing(level, this.position);
    }
}
