package org.cyclops.cyclopscore.inventory;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.startup.StartupArgs;
import org.junit.runners.Suite;
import org.junit.runners.model.InitializationError;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

/**
 * @author rubensworks
 */
public class JunitTestSuiteFmlLoaded extends Suite {

    public JunitTestSuiteFmlLoaded(Class<?> klass) throws InitializationError {
        super(klass, loadTestClasses());
    }

    private static Class<?>[] loadTestClasses() throws InitializationError {
        System.out.println("ABC");
        ClassLoader defaultClassLoader = ClassLoader.getSystemClassLoader();
        FMLLoader loader = FMLLoader.create(new StartupArgs( // TODO: call this in another class, to postpone Item classloading? That works! But Container is loaded before this as well. Add test suite dependencies?
                Paths.get(""),
                true,
                Dist.DEDICATED_SERVER,
                true,
                new String[]{},
                new HashSet<>(),
                List.of(),
                null
        ));
        Thread.currentThread().setContextClassLoader(defaultClassLoader);

        // Example: dynamically define class names as strings
        String[] classNames = {
                "org.cyclops.cyclopscore.inventory.TestIndexedInventory"
        };

        try {
            Class<?>[] classes = new Class<?>[classNames.length];
            for (int i = 0; i < classNames.length; i++) {
                classes[i] = Class.forName(classNames[i]);
            }
            return classes;

        } catch (ClassNotFoundException e) {
            throw new InitializationError(e);
        }
    }

}
