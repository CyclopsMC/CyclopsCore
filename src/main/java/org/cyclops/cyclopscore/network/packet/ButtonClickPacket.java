package org.cyclops.cyclopscore.network.packet;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.inventory.container.button.IContainerButtonClickAcceptorServer;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet for notifying the server of a button click.
 * @author rubensworks
 *
 */
public class ButtonClickPacket extends PacketCodec {

    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "button_click");

    @CodecField
    private String buttonId;

    public ButtonClickPacket() {
        super(ID);
    }

    public ButtonClickPacket(String buttonId) {
        this();
        this.buttonId = buttonId;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void actionClient(Level level, Player player) {

    }

    @Override
    public void actionServer(Level level, ServerPlayer player) {
        if(player.containerMenu instanceof IContainerButtonClickAcceptorServer) {
            ((IContainerButtonClickAcceptorServer) player.containerMenu).onButtonClick(buttonId);
        }
    }

}
