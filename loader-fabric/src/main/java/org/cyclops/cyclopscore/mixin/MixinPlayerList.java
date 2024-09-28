package org.cyclops.cyclopscore.mixin;

import net.minecraft.network.Connection;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.CommonListenerCookie;
import net.minecraft.server.players.PlayerList;
import org.cyclops.cyclopscore.events.IPlayerLoggedInEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author rubensworks
 */
@Mixin(PlayerList.class)
public class MixinPlayerList {

    @Inject(method = "placeNewPlayer", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void placeNewPlayer(Connection pConnection, ServerPlayer pPlayer, CommonListenerCookie pCookie, CallbackInfo callback) {
        PlayerList playerList = (PlayerList) (Object) this;
        IPlayerLoggedInEvent.EVENT.invoker().onPlayerLoggedIn(pPlayer);
    }

}
