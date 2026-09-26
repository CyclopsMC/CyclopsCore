package org.cyclops.cyclopscore.mixin;

import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.RegistryLoadTask;
import org.cyclops.cyclopscore.events.IRegisterGameTestsEvent;
import org.cyclops.cyclopscore.helper.MixinHelpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.List;

/**
 * Pseudo, as the Fabric game test API is not present in production.
 * @author rubensworks
 */
@Pseudo
@Mixin(targets = "net.fabricmc.fabric.impl.gametest.FabricGameTestModInitializer")
public class MixinFabricGameTestModInitializer {
    @Inject(method = "registerDynamicEntries", at = @At(value = "RETURN"), remap = false)
    private static void registerDynamicEntries(List<RegistryLoadTask<?>> registriesList, CallbackInfo callback) {
        Registry<GameTestInstance> testRegistry = MixinHelpers.getGameTestRegistry(registriesList);
        Registry<TestEnvironmentDefinition<?>> testEnvironmentRegistry = MixinHelpers.getGameTestEnvironmentRegistry(registriesList);
        IRegisterGameTestsEvent.EVENT.invoker().registerTest(testEnvironmentRegistry, (name, test) -> Registry.register(testRegistry, name, test));
    }
}
