package org.cyclops.cyclopscore.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;
import net.minecraft.client.renderer.state.CameraRenderState;
import org.cyclops.cyclopscore.events.ILivingEntityRendererEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

/**
 * @author rubensworks
 */
@Mixin(LivingEntityRenderer.class)
public class MixinLivingEntityRenderer {

    @Inject(method = "submit", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void submit(LivingEntityRenderState livingEntityRenderState, PoseStack poseStack, SubmitNodeCollector submitNodeCollector, CameraRenderState cameraRenderState, CallbackInfo callback) {
        LivingEntityRenderer renderer = (LivingEntityRenderer) (Object) this;
        ILivingEntityRendererEvent.EVENT.invoker().onRender(renderer, livingEntityRenderState, poseStack, submitNodeCollector, cameraRenderState);
    }

}
