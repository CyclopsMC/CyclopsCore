package org.cyclops.cyclopscore.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.helper.LocationHelpers;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * An abstract superclass for all packets that contain the position and name
 * of a player and execute a specific action on the client side that is visible by
 * all players in a specific range around the given player.
 *
 * @author immortaleeb
 */
public abstract class PlayerPositionPacket extends PacketCodec {

    private static final int DEFAULT_RANGE = 3000;

    @CodecField
    protected String displayName;
    @CodecField
    protected Vec3d position = new Vec3d(0, 0, 0);
    @CodecField
    private int range = DEFAULT_RANGE;

    /**
     * Creates a packet with no content
     */
    public PlayerPositionPacket() {
    }

    /**
     * Creates a PlayerPositionPacket which contains the player data.
     * @param player The player data.
     */
    public PlayerPositionPacket(EntityPlayer player) {
        this(player, DEFAULT_RANGE);
    }

    /**
     * Creates a PlayerPositionPacket which contains the player data
     * and will be sent to all players in the specified range of the
     * player.
     * @param player The player data.
     * @param range The range around the player.
     */
    public PlayerPositionPacket(EntityPlayer player, int range) {
        this.displayName = player.getDisplayNameString();
        this.position = player.getPositionVector();
        this.range = range;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void actionClient(World world, EntityPlayer player) {
        if (displayName == null) {
            getModInstance().log(Level.WARN, "Got PlayerPositionPacket with empty displayName");
            return;
        }

        if(player != null && !player.getDisplayNameString().equals(displayName))
            player = world.getPlayerEntityByName(displayName);

        if(player == null)
            getModInstance().log(Level.WARN, "Received PlayerPositionPacket for player with displayName '" + displayName + "', but player doesn't exist");
        else
            performClientAction(world, player);
    }

    @Override
    public void actionServer(World world, EntityPlayerMP player) {
        getModInstance().getPacketHandler().sendToAllAround(create(player, range),
                LocationHelpers.createTargetPointFromEntity(player, range));
    }

    protected abstract PlayerPositionPacket create(EntityPlayer player, int range);

    protected abstract ModBase getModInstance();

    protected abstract void performClientAction(World world, EntityPlayer player);
}
