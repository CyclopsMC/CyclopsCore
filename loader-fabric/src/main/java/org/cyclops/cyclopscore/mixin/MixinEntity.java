package org.cyclops.cyclopscore.mixin;

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
@Mixin(Entity.class)
public class MixinEntity {

    @Inject(method = "rideTick", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void rideTick(CallbackInfo callback) {
        IEntityTickEvent.EVENT.invoker().onTick((Entity) (Object) this);
    }

}
