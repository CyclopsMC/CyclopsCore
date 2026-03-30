package org.cyclops.cyclopscore.mixin;

import net.minecraft.core.Registry;
import net.minecraft.core.WritableRegistry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.RegistryLoadTask;
import net.minecraft.resources.ResourceKey;
import org.cyclops.cyclopscore.event.RegisterGameTestsForgeEvent;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.Map;

/**
 * Injects into {@link RegistryLoadTask#freezeRegistry(Map)} to fire {@link RegisterGameTestsForgeEvent}
 * just before the TEST_INSTANCE registry is frozen.
 *
 * <p>Because the freeze step runs sequentially and {@code TEST_ENVIRONMENT} precedes
 * {@code TEST_INSTANCE} in {@code WORLDGEN_REGISTRIES}, a {@link ThreadLocal} is used to
 * capture the environment registry on the first call so it is available on the second.</p>
 *
 * @author rubensworks
 */
@Mixin(RegistryLoadTask.class)
public class MixinRegistryDataLoader {

    @Shadow(remap = false)
    public WritableRegistry<?> registry;

    private static final ThreadLocal<WritableRegistry<?>> PENDING_ENV_REGISTRY = new ThreadLocal<>();

    @Inject(method = "freezeRegistry", at = @At("HEAD"), remap = false)
    private void onFreezeRegistry(
            Map<ResourceKey<?>, Exception> loadingErrors,
            CallbackInfoReturnable<Boolean> cir) {
        if (this.registry.key() == Registries.TEST_ENVIRONMENT) {
            PENDING_ENV_REGISTRY.set(this.registry);
        } else if (this.registry.key() == Registries.TEST_INSTANCE) {
            WritableRegistry<?> envRegistry = PENDING_ENV_REGISTRY.get();
            if (envRegistry != null) {
                PENDING_ENV_REGISTRY.remove();
                @SuppressWarnings("unchecked")
                WritableRegistry<TestEnvironmentDefinition<?>> typedEnvRegistry =
                        (WritableRegistry<TestEnvironmentDefinition<?>>) envRegistry;
                @SuppressWarnings("unchecked")
                WritableRegistry<GameTestInstance> testRegistry =
                        (WritableRegistry<GameTestInstance>) this.registry;
                RegisterGameTestsForgeEvent event = new RegisterGameTestsForgeEvent(
                        typedEnvRegistry,
                        (id, test) -> Registry.register(testRegistry, id, test));
                RegisterGameTestsForgeEvent.BUS.post(event);
            }
        }
    }
}
