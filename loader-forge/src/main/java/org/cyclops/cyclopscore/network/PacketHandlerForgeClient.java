package org.cyclops.cyclopscore.network;

import net.minecraft.client.Minecraft;
import net.minecraftforge.event.network.CustomPayloadEvent;

/**
 * @author rubensworks
 */
public class PacketHandlerForgeClient {

    public static void handlePacketClient(CustomPayloadEvent.Context context, PacketBase<?> packet) {
        packet.actionClient(
                Minecraft.getInstance().player != null ? Minecraft.getInstance().player.level() : null,
                Minecraft.getInstance().player
        );
    }

}
