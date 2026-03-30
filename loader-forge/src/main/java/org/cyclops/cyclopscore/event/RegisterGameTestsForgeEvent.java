package org.cyclops.cyclopscore.event;

import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraftforge.eventbus.api.bus.EventBus;
import net.minecraftforge.eventbus.api.event.RecordEvent;

import java.util.function.BiConsumer;

/**
 * Event fired just before the test registries are frozen, allowing mods to register their game tests.
 * The {@link #environmentsRegistry()} can be used to look up test environments,
 * and {@link #registerTest()} can be used to register new {@link GameTestInstance}s.
 *
 * @author rubensworks
 */
public record RegisterGameTestsForgeEvent(
        Registry<TestEnvironmentDefinition<?>> environmentsRegistry,
        BiConsumer<Identifier, GameTestInstance> registerTest) implements RecordEvent {

    public static final EventBus<RegisterGameTestsForgeEvent> BUS;

    static {
        BUS = EventBus.create(RegisterGameTestsForgeEvent.class);
    }
}
