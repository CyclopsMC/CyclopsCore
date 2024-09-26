package org.cyclops.cyclopscore.events;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.world.entity.LivingEntity;

/**
 * @author rubensworks
 */
public interface ILivingEntityRendererEvent {
    Event<ILivingEntityRendererEvent> EVENT = EventFactory.createArrayBacked(ILivingEntityRendererEvent.class,
            (listeners) -> (entity, renderer, partialTick, poseStack, multiBufferSource, packetLight) -> {
                for (ILivingEntityRendererEvent event : listeners) {
                    event.onRender(entity, renderer, partialTick, poseStack, multiBufferSource, packetLight);
                }
            }
    );

    void onRender(LivingEntity entity, LivingEntityRenderer<?, ?> renderer, float partialTick, PoseStack poseStack, MultiBufferSource multiBufferSource, int packedLight);
}
