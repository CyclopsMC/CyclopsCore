package org.cyclops.cyclopscore.network.packet;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.inventory.container.button.IButtonClickAcceptor;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet for notifying the server of a button click.
 * @author rubensworks
 *
 */
public class ButtonClickPacket extends PacketCodec {

	@CodecField
	private int buttonId;

    public ButtonClickPacket() {

    }

    public ButtonClickPacket(int buttonId) {
		this.buttonId = buttonId;
    }

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void actionClient(World world, EntityPlayer player) {

	}

	@Override
	public void actionServer(World world, EntityPlayerMP player) {
		if(player.openContainer instanceof IButtonClickAcceptor) {
			((IButtonClickAcceptor<?>) player.openContainer).onButtonClick(buttonId);
		}
	}
	
}