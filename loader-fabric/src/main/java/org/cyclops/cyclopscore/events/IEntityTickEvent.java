package org.cyclops.cyclopscore.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;

/**
 * @author rubensworks
 */
public interface IEntityTickEvent {
    Event<IEntityTickEvent> EVENT = EventFactory.createArrayBacked(IEntityTickEvent.class,
            (listeners) -> (entity) -> {
                for (IEntityTickEvent event : listeners) {
                    event.onTick(entity);
                }
            }
    );

    void onTick(Entity entity);
}
