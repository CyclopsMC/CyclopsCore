package org.cyclops.cyclopscore.mixin;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import org.cyclops.cyclopscore.events.IMenuOpenEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.OptionalInt;

/**
 * @author rubensworks
 */
@Mixin(ServerPlayer.class)
public class MixinServerPlayer {

    @Inject(method = "openMenu", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void openMenu(MenuProvider pMenu, CallbackInfoReturnable<OptionalInt> callback) {
        ServerPlayer serverPlayer = (ServerPlayer) (Object) this;
        IMenuOpenEvent.EVENT.invoker().onMenuOpened(serverPlayer, serverPlayer.containerMenu);
    }

}
