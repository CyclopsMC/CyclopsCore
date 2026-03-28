package org.cyclops.cyclopscore.helper;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.RegistryLoadTask;

import java.util.List;

/**
 * @author rubensworks
 */
public class MixinHelpers {

    public static Registry<GameTestInstance> getGameTestRegistry(List<RegistryLoadTask<?>> registriesList) {
        Registry<GameTestInstance> testRegistry = null;
        for (RegistryLoadTask<?> loader : registriesList) {
            if (loader.registry.key() == Registries.TEST_INSTANCE) {
                testRegistry = (Registry) loader.registry;
            }
        }
        return testRegistry;
    }

    public static Registry<TestEnvironmentDefinition<?>> getGameTestEnvironmentRegistry(List<RegistryLoadTask<?>> registriesList) {
        Registry<TestEnvironmentDefinition<?>> testEnvironmentRegistry = null;
        for (RegistryLoadTask<?> loader : registriesList) {
            if (loader.registry.key() == Registries.TEST_ENVIRONMENT) {
                testEnvironmentRegistry = (Registry) loader.registry;
            }
        }
        return testEnvironmentRegistry;
    }

}
