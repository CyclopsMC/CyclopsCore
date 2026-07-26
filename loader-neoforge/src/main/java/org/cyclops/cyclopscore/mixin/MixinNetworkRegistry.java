package org.cyclops.cyclopscore.mixin;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.common.ServerCommonPacketListener;
import net.neoforged.neoforge.network.registration.NetworkRegistry;
import org.cyclops.cyclopscore.gametest.GameTestLoaderHelpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(NetworkRegistry.class)
public class MixinNetworkRegistry {

    @Inject(method = "checkPacket", at = @At("HEAD"), cancellable = true)
    private static void checkPacket(Packet<?> packet, ServerCommonPacketListener listener, CallbackInfo cir) {
        // Don't check packets when running game tests globally
        if (GameTestLoaderHelpers.areGameTestsGloballyEnabled()) {
            cir.cancel();
        }
    }
}
