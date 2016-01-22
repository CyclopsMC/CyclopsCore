package org.cyclops.cyclopscore.network.packet.debug;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Debug ping pong packet
 * @author rubensworks
 *
 */
public class PingPongPacketAsync extends PacketCodec {

	@CodecField
	protected int remaining;

    /**
     * Empty packet.
     */
    public PingPongPacketAsync() {

    }

	public PingPongPacketAsync(int remaining) {
		this.remaining = remaining;
	}

	@Override
	public boolean isAsync() {
		return true;
	}

	protected void log(EntityPlayer player, String message) {
		player.addChatComponentMessage(new ChatComponentText(message));
	}

	protected PingPongPacketAsync newPacket() {
		return new PingPongPacketSync(remaining - 1);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {
		if(remaining > 0) {
			CyclopsCore._instance.getPacketHandler().sendToServer(newPacket());
		}
		log(player, String.format("[PING %s] Fields: %s", remaining, toString()));
	}

	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		if(remaining > 0) {
			CyclopsCore._instance.getPacketHandler().sendToPlayer(newPacket(), player);
		}
		log(player, String.format("[PONG %s] Fields: %s", remaining, toString()));
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();
		List<Field> fields = fieldCache.get(null);
		for(Field field : fields) {
			sb.append(" ");
			sb.append(field);
			sb.append("=");
			try {
				sb.append(field.get(this));
			} catch (IllegalAccessException e) {
				e.printStackTrace();
				sb.append("ERROR(" + e.getMessage() + ")");
			}
		}
		return sb.toString();
	}
	
}