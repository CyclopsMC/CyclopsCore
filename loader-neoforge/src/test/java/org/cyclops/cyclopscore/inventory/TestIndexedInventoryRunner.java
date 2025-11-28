package org.cyclops.cyclopscore.inventory;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.startup.StartupArgs;
import org.junit.runner.Description;
import org.junit.runner.Runner;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.Suite;
import org.junit.runners.model.RunnerBuilder;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

/**
 * @author rubensworks
 */
public class TestIndexedInventoryRunner extends Suite {
    static {
        // This runs when the runner class loads — before test classes are instantiated.
        System.out.println("Runner static block");

                System.out.println("ABC");
        FMLLoader.create(new StartupArgs( // TODO: call this in another class, to postpone Item classloading? That works! But Container is loaded before this as well. Add test suite dependencies?
                Paths.get(""),
                true,
                Dist.DEDICATED_SERVER,
                true,
                new String[]{},
                new HashSet<>(),
                List.of(),
                Thread.currentThread().getContextClassLoader()
        ));
    }

    public TestIndexedInventoryRunner(Class<?> clazz, RunnerBuilder builder) throws Exception {
        super(clazz, builder);
        System.out.println("Runner constructor");
    }
}
