package org.cyclops.cyclopscore.gametest;

import com.google.common.collect.Lists;
import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestAssertException;
import net.minecraft.gametest.framework.StructureUtils;
import net.minecraft.gametest.framework.TestFunction;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.List;

/**
 * Utilities for loading game tests in a multi-loader environment.
 * @author rubensworks
 */
public class GameTestLoaderHelpers {

    public static boolean areGameTestsGloballyEnabled() {
        return Boolean.getBoolean("cyclopsmc.enabledGameTestsGlobal");
    }

    public static boolean areGameTestsEnabled(String modId) {
        return areGameTestsGloballyEnabled()
                || System.getProperty("neoforge.enabledGameTestNamespaces", "").contains(modId)
                || System.getProperty("forge.enabledGameTestNamespaces", "").contains(modId)
                || System.getProperty("cyclopsmc.enabledGameTestNamespaces", "").contains(modId);
    }

    public static Collection<TestFunction> generateCommonTests(String modId, Class<?>[] testClasses) throws InstantiationException, IllegalAccessException {
        List<TestFunction> testsList = Lists.newArrayList();

        for(Class<?> clazz : testClasses) {
            Object instance = clazz.newInstance();
            for (Method method : clazz.getDeclaredMethods()) {
                if (method.isAnnotationPresent(GameTest.class)) {
                    GameTest gameTest = method.getAnnotation(GameTest.class);
                    testsList.add(new TestFunction(
                            modId,
                            modId + "." + method.getName(),
                            gameTest.template(),
                            StructureUtils.getRotationForRotationSteps(gameTest.rotationSteps()),
                            gameTest.timeoutTicks(),
                            gameTest.setupTicks(),
                            gameTest.required(),
                            gameTest.manualOnly(),
                            gameTest.attempts(),
                            gameTest.requiredSuccesses(),
                            gameTest.skyAccess(),
                            (gameTestHelpers) -> {
                                try {
                                    method.invoke(instance, gameTestHelpers);
                                } catch (InvocationTargetException | IllegalAccessException e) {
                                    e.printStackTrace();
                                    throw new GameTestAssertException(e.getMessage());
                                }
                            }
                    ));
                }
            }
        }

        return testsList;
    }

}
