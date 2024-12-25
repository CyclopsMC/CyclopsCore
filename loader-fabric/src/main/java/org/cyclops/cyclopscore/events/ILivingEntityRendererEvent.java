package org.cyclops.cyclopscore.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.client.renderer.entity.state.LivingEntityRenderState;

/**
 * @author rubensworks
 */
public interface ILivingEntityRendererEvent {
    Event<ILivingEntityRendererEvent> EVENT = EventFactory.createArrayBacked(ILivingEntityRendererEvent.class,
            (listeners) -> (renderer, livingEntityRenderState, poseStack, buffer, packedLight) -> {
                for (ILivingEntityRendererEvent event : listeners) {
                    event.onRender(renderer, livingEntityRenderState, poseStack, buffer, packedLight);
                }
            }
    );

    void onRender(LivingEntityRenderer<?, ?, ?> renderer, LivingEntityRenderState livingEntityRenderState, PoseStack poseStack, MultiBufferSource buffer, int packedLight);
}
