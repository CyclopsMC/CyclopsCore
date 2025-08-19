package org.cyclops.cyclopscore.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Player;

/**
 * @author rubensworks
 */
public abstract class MinecraftClientHelpersCommon implements IMinecraftClientHelpers {
    @Override
    public Player getPlayer() {
        return Minecraft.getInstance().player;
    }

    @Override
    public boolean isShifted() {
        return Screen.hasShiftDown();
    }
}
