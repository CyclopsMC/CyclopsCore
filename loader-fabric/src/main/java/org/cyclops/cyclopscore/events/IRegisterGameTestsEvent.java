package org.cyclops.cyclopscore.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

/**
 * @author rubensworks
 */
public interface IRegisterGameTestsEvent {
    Event<IRegisterGameTestsEvent> EVENT = EventFactory.createArrayBacked(IRegisterGameTestsEvent.class,
            (listeners) -> (registrar) -> {
                for (IRegisterGameTestsEvent event : listeners) {
                    event.registerTest(registrar);
                }
            }
    );

    void registerTest(BiConsumer<ResourceLocation, GameTestInstance> registrar);
}
