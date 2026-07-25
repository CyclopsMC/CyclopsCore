package org.cyclops.cyclopscore.mixin;

import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.PacketDistributor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PacketDistributor.class)
public class MixinPacketDistributor {

    @Inject(method = "sendToPlayer", at = @At("HEAD"), cancellable = true)
    private static void sendToPlayer(ServerPlayer player, CustomPacketPayload payload, CustomPacketPayload[] payloads, CallbackInfo cir) {
        // Don't send packets to the mock player, as it is not a real player and will throw exceptions.
        if (player.getProfile().name().orElse("missing").equals("test-mock-player")) {
            cir.cancel();
        }
    }
}
