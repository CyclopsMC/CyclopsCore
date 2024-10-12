package org.cyclops.cyclopscore.gametest;

import net.minecraft.gametest.framework.GameTest;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraftforge.gametest.GameTestHolder;
import org.cyclops.cyclopscore.Reference;

/**
 * @author rubensworks
 */
@GameTestHolder(Reference.MOD_ID)
public class StartupTestForge {

    @GameTest(template = Reference.MOD_ID + ":empty")
    public void testDummy(GameTestHelper helper) {
        // A dummy test to ensure the server starts properly
        helper.succeed();
    }

}
