package org.cyclops.cyclopscore.network.packet;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.inventory.container.button.IContainerButtonClickAcceptorServer;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet for notifying the server of a button click.
 * @author rubensworks
 *
 */
public class ButtonClickPacket extends PacketCodec {

	@CodecField
	private String buttonId;

    public ButtonClickPacket() {

    }

    public ButtonClickPacket(String buttonId) {
		this.buttonId = buttonId;
    }

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void actionClient(World world, PlayerEntity player) {

	}

	@Override
	public void actionServer(World world, ServerPlayerEntity player) {
		if(player.openContainer instanceof IContainerButtonClickAcceptorServer) {
			((IContainerButtonClickAcceptorServer) player.openContainer).onButtonClick(buttonId);
		}
	}
	
}