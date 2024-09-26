package org.cyclops.cyclopscore.mixin;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;
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

    @Inject(method = "render", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD)
    private void openMenu(LivingEntity entity, float entityYaw, float partialTicks, PoseStack poseStack, MultiBufferSource buffer, int packedLight, CallbackInfo callback) {
        LivingEntityRenderer renderer = (LivingEntityRenderer) (Object) this;
        ILivingEntityRendererEvent.EVENT.invoker().onRender(entity, renderer, partialTicks, poseStack, buffer, packedLight);
    }

}
