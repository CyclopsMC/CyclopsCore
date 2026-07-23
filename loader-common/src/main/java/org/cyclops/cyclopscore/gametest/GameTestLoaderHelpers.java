package org.cyclops.cyclopscore.gametest;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;
import java.util.function.BiConsumer;

/**
 * Utilities for loading game tests in a multi-loader environment.
 * For NeoForge, this can be hooked into RegisterGameTestsEvent.
 * @author rubensworks
 */
public class GameTestLoaderHelpers {

    public static boolean areGameTestsGloballyEnabled() {
        return Boolean.getBoolean("cyclopsmc.enabledGameTestsGlobal")
                || System.getenv().getOrDefault("CYCLOPSMC_ENABLEDGAMETESTSGLOBAL", "false").equalsIgnoreCase("true");
    }

    public static boolean areGameTestsEnabled(String modId) {
        return areGameTestsGloballyEnabled()
                || System.getProperty("neoforge.enabledGameTestNamespaces", "").contains(modId)
                || System.getProperty("forge.enabledGameTestNamespaces", "").contains(modId)
                || System.getProperty("cyclopsmc.enabledGameTestNamespaces", "").contains(modId);
    }

    public static void registerCommonTests(String modId, Class<?>[] testClasses, BiConsumer<Identifier, GameTestInstance> registrar, Registry<TestEnvironmentDefinition<?>> testEnvironmentRegistry) {
        for (MethodGameTestInstance testInstance : generateCommonTests(modId, testClasses, testEnvironmentRegistry)) {
            registrar.accept(testInstance.getId(), testInstance);
        }
    }

    public static Collection<MethodGameTestInstance> generateCommonTests(String modId, Class<?>[] testClasses, Registry<TestEnvironmentDefinition<?>> testEnvironmentRegistry) {
        List<MethodGameTestInstance> testsList = Lists.newArrayList();

        for(Class<?> clazz : testClasses) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(GameTest.class)) {
                    GameTest gameTest = method.getAnnotation(GameTest.class);
                    Identifier envId = Identifier.parse(gameTest.environment());
                    @SuppressWarnings({"unchecked", "rawtypes"})
                    Holder.Reference<TestEnvironmentDefinition<?>> environment =
                        (Holder.Reference<TestEnvironmentDefinition<?>>) ((net.minecraft.core.HolderGetter) testEnvironmentRegistry)
                            .getOrThrow(ResourceKey.create(Registries.TEST_ENVIRONMENT, envId));
                    testsList.add(new MethodGameTestInstance(
                            new TestData<>(
                                    environment,
                                    Identifier.parse(gameTest.template()),
                                    gameTest.timeoutTicks(),
                                    gameTest.setupTicks(),
                                    gameTest.required(),
                                    gameTest.rotation(),
                                    gameTest.manualOnly(),
                                    gameTest.attempts(),
                                    gameTest.requiredSuccesses(),
                                    gameTest.skyAccess(),
                                    gameTest.padding()
                            ),
                            modId,
                            clazz.getName(),
                            method.getName()));
                }
            }
        }

        return testsList;
    }

}
