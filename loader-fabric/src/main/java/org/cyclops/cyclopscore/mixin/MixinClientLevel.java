package org.cyclops.cyclopscore.mixin;

import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.world.entity.Entity;
import org.cyclops.cyclopscore.events.IEntityJoinLevelEvent;
import org.cyclops.cyclopscore.events.IEntityTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author rubensworks
 */
@Mixin(ClientLevel.class)
public class MixinClientLevel {

    @Inject(method = "addEntity", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void placeNewPlayer(Entity pEntity, CallbackInfo callback) {
        ClientLevel clientLevel = (ClientLevel) (Object) this;
        IEntityJoinLevelEvent.EVENT.invoker().onEntityJoinLevel(pEntity, clientLevel);
    }

    @Inject(method = "tickNonPassenger", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void tickNonPassenger(Entity pEntity, CallbackInfo callback) {
        IEntityTickEvent.EVENT.invoker().onTick(pEntity);
    }

}
