package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.entity.player.EntityPlayer;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.GuiInfoBook;

/**
 * A reward instance.
 * @author rubensworks
 */
public interface IReward<B extends AdvancedButton> {
    /**
     * If the given player in its current state is able to obtain this reward.
     * @param player The player.
     * @return If it can obtain this.
     */
    public boolean canObtain(EntityPlayer player);

    /**
     * The logic for obtaining this reward.
     * Will only be called server-side.
     * @param player The player.
     */
    public void obtain(EntityPlayer player);

    /**
     * @return The gui width/
     */
    public int getWidth();

    /**
     * @return The gui height.
     */
    public int getHeight();

    /**
     * @return Factory for a button for this reward.
     */
    public B createButton();

    /**
     * Draw the reward.
     * @param gui The gui.
     * @param x Start X.
     * @param y Start Y.
     * @param width Max width.
     * @param height Max height.
     * @param page Current page.
     * @param mx Mouse X.
     * @param my Mouse Y.
     * @param button The button of this reward.
     */
    public void drawElementInner(GuiInfoBook gui, int x, int y, int width, int height, int page, int mx, int my, B button);
}
