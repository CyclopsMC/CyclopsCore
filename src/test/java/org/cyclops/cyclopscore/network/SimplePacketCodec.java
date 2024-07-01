package org.cyclops.cyclopscore.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.Reference;

/**
 * Does nothing.
 * @author rubensworks
 */
public class SimplePacketCodec extends PacketCodec {
    public SimplePacketCodec() {
        super(new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "simple_packet")));
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public void actionClient(Level level, Player player) {

    }

    @Override
    public void actionServer(Level level, ServerPlayer player) {

    }
}
