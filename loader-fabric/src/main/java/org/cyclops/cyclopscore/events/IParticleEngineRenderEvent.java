package org.cyclops.cyclopscore.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.client.Camera;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.LightTexture;

/**
 * @author rubensworks
 */
public interface IParticleEngineRenderEvent {
    Event<IParticleEngineRenderEvent> EVENT = EventFactory.createArrayBacked(IParticleEngineRenderEvent.class,
            (listeners) -> (particleEngine, lightTexture, camera, partialTick) -> {
                for (IParticleEngineRenderEvent event : listeners) {
                    event.onRender(particleEngine, lightTexture, camera, partialTick);
                }
            }
    );

    void onRender(ParticleEngine particleEngine, LightTexture lightTexture, Camera camera, float partialTick);
}
