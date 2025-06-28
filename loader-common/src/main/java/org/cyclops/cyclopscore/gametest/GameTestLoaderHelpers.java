package org.cyclops.cyclopscore.gametest;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.registries.VanillaRegistries;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;

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

    public static void registerCommonTests(String modId, Class<?>[] testClasses, BiConsumer<ResourceLocation, GameTestInstance> registrar) {
        for (MethodGameTestInstance testInstance : generateCommonTests(modId, testClasses)) {
            registrar.accept(testInstance.getId(), testInstance);
        }
    }

    public static Collection<MethodGameTestInstance> generateCommonTests(String modId, Class<?>[] testClasses) {
        List<MethodGameTestInstance> testsList = Lists.newArrayList();

        for(Class<?> clazz : testClasses) {
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(GameTest.class)) {
                    GameTest gameTest = method.getAnnotation(GameTest.class);
                    Holder.Reference<TestEnvironmentDefinition> environment = VanillaRegistries.createLookup().getOrThrow(ResourceKey.create(
                            Registries.TEST_ENVIRONMENT,
                            ResourceLocation.parse(gameTest.environment())
                    ));
                    testsList.add(new MethodGameTestInstance(
                            new TestData<>(
                                    environment,
                                    ResourceLocation.parse(gameTest.template()),
                                    gameTest.timeoutTicks(),
                                    gameTest.setupTicks(),
                                    gameTest.required(),
                                    gameTest.rotation(),
                                    gameTest.manualOnly(),
                                    gameTest.attempts(),
                                    gameTest.requiredSuccesses(),
                                    gameTest.skyAccess()
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
