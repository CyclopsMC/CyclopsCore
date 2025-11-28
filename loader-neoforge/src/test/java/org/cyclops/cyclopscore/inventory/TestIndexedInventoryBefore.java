package org.cyclops.cyclopscore.inventory;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.startup.StartupArgs;
import org.junit.BeforeClass;
import org.junit.Test;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

/**
 * @author rubensworks
 */
public class TestIndexedInventoryBefore {

//    @BeforeClass
    public static void setUpClass() {
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

//    @Test
    public void noop() {

    }

}
