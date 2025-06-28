package org.cyclops.cyclopscore.gametest;

import com.google.common.collect.Lists;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestInstance;
import net.minecraft.gametest.framework.StructureUtils;
import net.minecraft.gametest.framework.TestData;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.resources.ResourceLocation;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Utilities for loading game tests in a multi-loader environment.
 * For NeoForge, this can be hooked into RegisterGameTestsEvent.
 * @author rubensworks
 */
public class GameTestLoaderHelpers {

    public static Collection<GameTestInstance> generateCommonTests(String modId, Class<?>[] testClasses, Holder<TestEnvironmentDefinition> environment) throws InstantiationException, IllegalAccessException {
        List<GameTestInstance> testsList = Lists.newArrayList();

        for(Class<?> clazz : testClasses) {
            Object instance = clazz.newInstance();
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(GameTest.class)) {
                    GameTest gameTest = method.getAnnotation(GameTest.class);
                    testsList.add(new MethodGameTestInstance(
                            new TestData<>(
                                    environment,
                                    ResourceLocation.parse(gameTest.template()),
                                    gameTest.timeoutTicks(),
                                    gameTest.setupTicks(),
                                    gameTest.required(),
                                    StructureUtils.getRotationForRotationSteps(gameTest.rotationSteps()),
                                    gameTest.manualOnly(),
                                    gameTest.attempts(),
                                    gameTest.requiredSuccesses(),
                                    gameTest.skyAccess()
                            ),
                            clazz.getName(),
                            method.getName()
                    ));
                }
            }
        }

        return testsList;
    }

}
