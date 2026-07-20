package org.cyclops.cyclopscore.mixin;

import net.neoforged.neoforge.gametest.GameTestHooks;
import org.cyclops.cyclopscore.gametest.GameTestLoaderHelpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameTestHooks.class)
public class MixinGameTestHooks<T> {

    @SuppressWarnings("unchecked")
    @Inject(method = "isGametestEnabled", at = @At("HEAD"), cancellable = true)
    private static void isGametestEnabled(CallbackInfoReturnable<Boolean> cir) {
        if (GameTestLoaderHelpers.areGameTestsGloballyEnabled()) {
            cir.setReturnValue(true);
        }
    }
}
