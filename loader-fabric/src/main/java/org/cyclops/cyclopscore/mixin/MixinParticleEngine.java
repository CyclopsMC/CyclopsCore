package org.cyclops.cyclopscore.mixin;

import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.LightTexture;
import org.cyclops.cyclopscore.events.IParticleEngineRenderEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author rubensworks
 */
@Mixin(ParticleEngine.class)
public class MixinParticleEngine {

    @Inject(method = "render", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void render(LightTexture pLightTexture, Camera pCamera, float pPartialTick, CallbackInfo callback) {
        ParticleEngine particleEngine = (ParticleEngine) (Object) this;
        IParticleEngineRenderEvent.EVENT.invoker().onRender(particleEngine, pLightTexture, pCamera, pPartialTick);
    }

}
