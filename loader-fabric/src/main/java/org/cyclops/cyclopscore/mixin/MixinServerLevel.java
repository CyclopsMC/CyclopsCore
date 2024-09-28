package org.cyclops.cyclopscore.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import org.cyclops.cyclopscore.events.IEntityTickEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author rubensworks
 */
@Mixin(ServerLevel.class)
public class MixinServerLevel {

    @Inject(method = "tickNonPassenger", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void tickNonPassenger(Entity pEntity, CallbackInfo callback) {
        IEntityTickEvent.EVENT.invoker().onTick(pEntity);
    }

}
