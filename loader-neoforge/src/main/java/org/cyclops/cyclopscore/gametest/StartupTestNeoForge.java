package org.cyclops.cyclopscore.gametest;

import net.minecraft.gametest.framework.GameTestHelper;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import org.cyclops.cyclopscore.Reference;

/**
 * @author rubensworks
 */
public class StartupTestNeoForge {

    public void register(RegisterGameTestsEvent event) {
        GameTestLoaderHelpers.registerCommonTests(Reference.MOD_ID, new Class[]{
                StartupTestNeoForge.class
        }, event::registerTest);
    }

    @GameTest
    public void testDummy(GameTestHelper helper) {
        // A dummy test to ensure the server starts properly
        helper.succeed();
    }
}
