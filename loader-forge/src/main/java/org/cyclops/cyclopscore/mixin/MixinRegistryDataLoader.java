package org.cyclops.cyclopscore.mixin;

import net.minecraft.core.Registry;
import net.minecraft.core.RegistryAccess;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.RegistryDataLoader;
import net.minecraft.resources.RegistryLoadTask;
import net.minecraft.resources.ResourceKey;
import org.cyclops.cyclopscore.event.RegisterGameTestsForgeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.List;
import java.util.Map;

/**
 * Injects into {@link RegistryDataLoader} to fire {@link RegisterGameTestsForgeEvent}
 * inside {@code RegistryDataLoader#load}, mimicking NeoForge's {@code RegisterGameTestsEvent}.
 *
 * <p>{@code RegistryDataLoader.load} is asynchronous: it runs each {@link RegistryLoadTask}
 * concurrently and then calls {@code lambda$load$2} via {@code thenApplyAsync} once all tasks
 * finish. At the very start of that callback every {@link WritableRegistry} is fully populated
 * with data-pack entries but has not yet been frozen (the
 * {@code stream().filter(task -> task.freezeRegistry(...))} call comes after). We inject at
 * HEAD so the event fires before any registry is frozen.</p>
 *
 * @author rubensworks
 */
@Mixin(RegistryDataLoader.class)
public class MixinRegistryDataLoader {

    private static boolean called = false;

    @SuppressWarnings("unchecked")
    @Inject(
            method = "lambda$load$2(Ljava/util/List;Ljava/util/Map;Ljava/lang/Void;)Lnet/minecraft/core/RegistryAccess$Frozen;",
            at = @At("HEAD"),
            remap = false
    )
    private static void onBeforeFreeze(
            List<RegistryLoadTask<?>> tasks,
            Map<ResourceKey<?>, Exception> errors,
            Void unused,
            CallbackInfoReturnable<RegistryAccess.Frozen> cir
    ) {
        // Hack to ensure that it's only called once for server-specific registry loading in client worlds,
        // otherwise it's called twice.
        if (called) {
            return;
        }

        WritableRegistry<TestEnvironmentDefinition<?>> envRegistry = null;
        WritableRegistry<GameTestInstance> testRegistry = null;

        for (RegistryLoadTask<?> task : tasks) {
            if (task.registry.key() == Registries.TEST_ENVIRONMENT) {
                envRegistry = (WritableRegistry<TestEnvironmentDefinition<?>>) task.registry;
            } else if (task.registry.key() == Registries.TEST_INSTANCE) {
                testRegistry = (WritableRegistry<GameTestInstance>) task.registry;
            }
        }

        if (envRegistry != null && testRegistry != null) {
            WritableRegistry<GameTestInstance> finalTestRegistry = testRegistry;
            RegisterGameTestsForgeEvent event = new RegisterGameTestsForgeEvent(
                    envRegistry,
                    (id, test) -> Registry.register(finalTestRegistry, id, test));
            RegisterGameTestsForgeEvent.BUS.post(event);
            called = true;
        }
    }
}
