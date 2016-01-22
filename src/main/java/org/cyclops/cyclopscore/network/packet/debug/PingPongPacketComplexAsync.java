package org.cyclops.cyclopscore.network.packet.debug;

import net.minecraft.entity.player.EntityPlayer;
import org.cyclops.cyclopscore.network.CodecField;

/**
 * Debug ping pong packet
 * @author rubensworks
 *
 */
public class PingPongPacketComplexAsync extends PingPongPacketAsync {

	@CodecField
	protected String string1;
	@CodecField
	protected String string2;

    /**
     * Empty packet.
     */
    public PingPongPacketComplexAsync() {
		super();
    }

	public PingPongPacketComplexAsync(int remaining, String string1, String string2) {
		super(remaining);
		this.string1 = string1;
		this.string2 = string2;
	}

	protected PingPongPacketAsync newPacket() {
		return new PingPongPacketComplexAsync(remaining - 1, string1, string2);
	}

	@Override
	protected void log(EntityPlayer player, String message) {
		super.log(player, message);
		super.log(player, String.format("[EXTRA] '%s' and '%s' ", string1, string2));
	}
}