package org.cyclops.cyclopscore.mixin;

import net.minecraft.client.particle.ParticleEngine;
import org.spongepowered.asm.mixin.Mixin;

/**
 * @author rubensworks
 */
@Mixin(ParticleEngine.class)
public class MixinParticleEngine {

    // TODO: restore
//    @Inject(method = "render", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
//    private void render(Camera camera, float partialTick, MultiBufferSource.BufferSource bufferSource, CallbackInfo callback) {
//        ParticleEngine particleEngine = (ParticleEngine) (Object) this;
//        IParticleEngineRenderEvent.EVENT.invoker().onRender(particleEngine, camera, partialTick, bufferSource);
//    }

}
