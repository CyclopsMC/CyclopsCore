package org.cyclops.cyclopscore.helper;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.RegistryDataLoader;

import java.util.List;

/**
 * @author rubensworks
 */
public class MixinHelpers {

    public static Registry<GameTestInstance> getGameTestRegistry(List<RegistryDataLoader.Loader<?>> registriesList) {
        Registry<GameTestInstance> testRegistry = null;
        for (RegistryDataLoader.Loader<?> loader : registriesList) {
            if (loader.registry().key() == Registries.TEST_INSTANCE) {
                testRegistry = (Registry) loader.registry();
            }
        }
        return testRegistry;
    }

    public static Registry<TestEnvironmentDefinition> getGameTestEnvironmentRegistry(List<RegistryDataLoader.Loader<?>> registriesList) {
        Registry<TestEnvironmentDefinition> testEnvironmentRegistry = null;
        for (RegistryDataLoader.Loader<?> loader : registriesList) {
            if (loader.registry().key() == Registries.TEST_ENVIRONMENT) {
                testEnvironmentRegistry = (Registry) loader.registry();
            }
        }
        return testEnvironmentRegistry;
    }

}
