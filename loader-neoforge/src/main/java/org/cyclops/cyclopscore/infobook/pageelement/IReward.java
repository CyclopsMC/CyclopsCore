package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.world.entity.player.Player;

/**
 * A reward instance.
 * @author rubensworks
 */
public interface IReward {
    /**
     * If the given player in its current state is able to obtain this reward.
     * @param player The player.
     * @return If it can obtain this.
     */
    public boolean canObtain(Player player);

    /**
     * The logic for obtaining this reward.
     * Will only be called server-side.
     * @param player The player.
     */
    public void obtain(Player player);

    /**
     * @return The gui width/
     */
    public int getWidth();

    /**
     * @return The gui height.
     */
    public int getHeight();

    public IRewardClient constructRewardClient();
}
