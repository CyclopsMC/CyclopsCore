package org.cyclops.cyclopscore.gametest;

import net.minecraft.gametest.framework.GameTestHelper;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.events.IRegisterGameTestsEvent;

/**
 * @author rubensworks
 */
public class StartupTestFabric {

    public StartupTestFabric() {
        IRegisterGameTestsEvent.EVENT.register(registrar -> GameTestLoaderHelpers.registerCommonTests(Reference.MOD_ID, new Class[]{
            StartupTestFabric.class
        }, registrar));
    }

    @GameTest
    public void testDummy(GameTestHelper helper) {
        // A dummy test to ensure the server starts properly
        helper.succeed();
    }

}
