package org.cyclops.cyclopscore.mixin;

import net.minecraftforge.gametest.ForgeGameTestHooks;
import org.cyclops.cyclopscore.gametest.GameTestLoaderHelpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ForgeGameTestHooks.class)
public class MixinForgeGameTestHooks {

    @Inject(method = "isGametestEnabled", at = @At("HEAD"), cancellable = true, remap = false)
    private static void isGametestEnabled(CallbackInfoReturnable<Boolean> cir) {
        if (GameTestLoaderHelpers.areGameTestsGloballyEnabled()) {
            cir.setReturnValue(true);
        }
    }
}
