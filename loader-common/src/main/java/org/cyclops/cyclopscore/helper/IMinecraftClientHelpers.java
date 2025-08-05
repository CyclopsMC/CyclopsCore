package org.cyclops.cyclopscore.helper;

import net.minecraft.world.entity.player.Player;

/**
 * @author rubensworks
 */
public interface IMinecraftClientHelpers {

    /**
     * @return The player instance.
     */
    public Player getPlayer();

    /**
     * @return If the user is shifted.
     */
    public boolean isShifted();

}
