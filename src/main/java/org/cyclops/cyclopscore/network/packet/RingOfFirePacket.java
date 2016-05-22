package org.cyclops.cyclopscore.network.packet;

import net.minecraft.client.particle.ParticleFlame;
import net.minecraft.client.particle.ParticleLava;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.EnumParticleTypes;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.client.FMLClientHandler;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
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

    public RingOfFirePacket(EntityPlayer player) {
        super(player);
    }
    
	@SideOnly(Side.CLIENT)
    private static void showFireRing(World world, Vec3d pos) {
        double area = RING_AREA;
        int points = 40;
        for(double point = -points; point <= points; point++) {
            double u = 2 * Math.PI * (point / points);

            double xOffset = Math.cos(u) * area;
            double yOffset = 0F;
            double zOffset = Math.sin(u) * area;

            double xCoord = pos.xCoord;
            double yCoord = pos.yCoord;
            double zCoord = pos.zCoord;

            double particleX = xCoord + xOffset + world.rand.nextFloat() / 5;
            double particleY = yCoord + yOffset + world.rand.nextFloat() / 5;
            double particleZ = zCoord + zOffset + world.rand.nextFloat() / 5;

            float particleMotionX = (float)xOffset / 50;
            float particleMotionY = 0.01F;
            float particleMotionZ = (float)zOffset / 50;

            if(world.rand.nextInt(20) == 0) {
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                        new ParticleLava.Factory().getEntityFX(EnumParticleTypes.LAVA.getParticleID(), world, particleX, particleY, particleZ,
                                0, 0, 0, 0)
                        );
            } else {
                FMLClientHandler.instance().getClient().effectRenderer.addEffect(
                        new ParticleFlame.Factory().getEntityFX(0, world, particleX, particleY, particleZ, particleMotionX, particleMotionY, particleMotionZ)
                        );
            }
        }
    }

    @Override
    protected PlayerPositionPacket create(EntityPlayer player, int range) {
        return new RingOfFirePacket(player);
    }

    @Override
    protected ModBase getModInstance() {
        return CyclopsCore._instance;
    }

    @Override
    protected void performClientAction(World world, EntityPlayer player) {
        showFireRing(world, this.position);
    }
}
