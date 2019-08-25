package org.cyclops.cyclopscore.network;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.world.World;

/**
 * Does nothing.
 * @author rubensworks
 */
public class SimplePacketCodec extends PacketCodec {
    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public void actionClient(World world, PlayerEntity player) {

    }

    @Override
    public void actionServer(World world, ServerPlayerEntity player) {

    }
}
