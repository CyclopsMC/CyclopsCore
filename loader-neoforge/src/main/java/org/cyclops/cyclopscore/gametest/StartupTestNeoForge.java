package org.cyclops.cyclopscore.gametest;

import net.minecraft.gametest.framework.GameTestHelper;

/**
 * @author rubensworks
 */
public class StartupTestNeoForge {

    @GameTest
    public void testDummy(GameTestHelper helper) {
        // A dummy test to ensure the server starts properly
        helper.succeed();
    }
}
