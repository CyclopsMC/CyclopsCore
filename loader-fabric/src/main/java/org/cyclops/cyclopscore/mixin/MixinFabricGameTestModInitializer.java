package org.cyclops.cyclopscore.mixin;

import net.fabricmc.fabric.impl.gametest.FabricGameTestModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.RegistryDataLoader;
import org.cyclops.cyclopscore.events.IRegisterGameTestsEvent;
import org.cyclops.cyclopscore.helper.MixinHelpers;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.List;

/**
 * @author rubensworks
 */
@Mixin(FabricGameTestModInitializer.class)
public class MixinFabricGameTestModInitializer {
    @Inject(method = "registerDynamicEntries", at = @At(value = "RETURN"), locals = LocalCapture.CAPTURE_FAILHARD, remap = false)
    private static void registerDynamicEntries(List<RegistryDataLoader.Loader<?>> registriesList, CallbackInfo callback) {
        Registry<GameTestInstance> testRegistry = MixinHelpers.getGameTestRegistry(registriesList);
        Registry<TestEnvironmentDefinition> testEnvironmentRegistry = MixinHelpers.getGameTestEnvironmentRegistry(registriesList);
        IRegisterGameTestsEvent.EVENT.invoker().registerTest(testEnvironmentRegistry, (name, test) -> Registry.register(testRegistry, name, test));
    }
}
