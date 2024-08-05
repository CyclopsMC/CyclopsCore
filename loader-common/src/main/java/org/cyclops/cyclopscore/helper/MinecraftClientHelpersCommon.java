package org.cyclops.cyclopscore.helper;

import net.minecraft.client.gui.screens.Screen;

/**
 * @author rubensworks
 */
public class MinecraftClientHelpersCommon implements IMinecraftClientHelpers {
    @Override
    public boolean isShifted() {
        return Screen.hasShiftDown();
    }
}
