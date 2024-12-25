package org.cyclops.cyclopscore.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.MultiBufferSource;

/**
 * @author rubensworks
 */
public interface IParticleEngineRenderEvent {
    Event<IParticleEngineRenderEvent> EVENT = EventFactory.createArrayBacked(IParticleEngineRenderEvent.class,
            (listeners) -> (particleEngine, camera, partialTick, bufferSource) -> {
                for (IParticleEngineRenderEvent event : listeners) {
                    event.onRender(particleEngine, camera, partialTick, bufferSource);
                }
            }
    );

    void onRender(ParticleEngine particleEngine, Camera camera, float partialTick, MultiBufferSource.BufferSource bufferSource);
}
