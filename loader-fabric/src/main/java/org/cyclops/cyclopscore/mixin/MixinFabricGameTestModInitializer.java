package org.cyclops.cyclopscore.mixin;

import net.fabricmc.fabric.impl.gametest.FabricGameTestModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.RegistryLoadTask;
import org.cyclops.cyclopscore.events.IRegisterGameTestsEvent;
import org.cyclops.cyclopscore.helper.MixinHelpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * @author rubensworks
 */
@Mixin(FabricGameTestModInitializer.class)
public class MixinFabricGameTestModInitializer {
    @Inject(method = "registerDynamicEntries", at = @At(value = "RETURN"), remap = false)
    private static void registerDynamicEntries(List<RegistryLoadTask<?>> registriesList, CallbackInfo callback) {
        Registry<GameTestInstance> testRegistry = MixinHelpers.getGameTestRegistry(registriesList);
        Registry<TestEnvironmentDefinition<?>> testEnvironmentRegistry = MixinHelpers.getGameTestEnvironmentRegistry(registriesList);
        IRegisterGameTestsEvent.EVENT.invoker().registerTest(testEnvironmentRegistry, (name, test) -> Registry.register(testRegistry, name, test));
    }
}
