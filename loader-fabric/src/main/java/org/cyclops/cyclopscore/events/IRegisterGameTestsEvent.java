package org.cyclops.cyclopscore.events;

import net.fabricmc.fabric.api.event.Event;
import net.fabricmc.fabric.api.event.EventFactory;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceLocation;

import java.util.function.BiConsumer;

/**
 * @author rubensworks
 */
public interface IRegisterGameTestsEvent {
    Event<IRegisterGameTestsEvent> EVENT = EventFactory.createArrayBacked(IRegisterGameTestsEvent.class,
            (listeners) -> (testEnvironmentRegistry, registrar) -> {
                for (IRegisterGameTestsEvent event : listeners) {
                    event.registerTest(testEnvironmentRegistry, registrar);
                }
            }
    );

    void registerTest(Registry<TestEnvironmentDefinition> testEnvironmentRegistry, BiConsumer<ResourceLocation, GameTestInstance> registrar);
}
