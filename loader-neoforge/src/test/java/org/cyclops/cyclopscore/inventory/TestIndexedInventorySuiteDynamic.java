package org.cyclops.cyclopscore.inventory;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLLoader;
import net.neoforged.fml.startup.StartupArgs;
import org.junit.runner.RunWith;

import java.nio.file.Paths;
import java.util.HashSet;
import java.util.List;

/**
 * @author rubensworks
 */
//@RunWith(JunitTestSuiteFmlLoaded.class)
public class TestIndexedInventorySuiteDynamic {

    static {
//        FMLLoader.create(new StartupArgs( // TODO: call this in another class, to postpone Item classloading? That works! But Container is loaded before this as well. Add test suite dependencies?
//                Paths.get(""),
//                true,
//                Dist.DEDICATED_SERVER,
//                true,
//                new String[]{},
//                new HashSet<>(),
//                List.of(),
//                Thread.currentThread().getContextClassLoader()
//        ));

//                SharedConstants.setVersion(DetectedVersion.BUILT_IN);
//        LoadingModList.of(Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(), Lists.newArrayList(), Maps.newHashMap());
//        Bootstrap.bootStrap();
//        ((MappedRegistry) BuiltInRegistries.ITEM).unfreeze(true);
    }

}
