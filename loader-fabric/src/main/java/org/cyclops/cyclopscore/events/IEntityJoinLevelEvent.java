package org.cyclops.cyclopscore.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.level.Level;

/**
 * @author rubensworks
 */
public interface IEntityJoinLevelEvent {
    Event<IEntityJoinLevelEvent> EVENT = EventFactory.createArrayBacked(IEntityJoinLevelEvent.class,
            (listeners) -> (entity, level) -> {
                for (IEntityJoinLevelEvent event : listeners) {
                    event.onEntityJoinLevel(entity, level);
                }
            }
    );

    void onEntityJoinLevel(Entity entity, Level level);
}
