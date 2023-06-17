package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

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

    /**
     * @param infoBook The infobook instance.
     * @return Factory for a button for this reward.
     */
    @OnlyIn(Dist.CLIENT)
    public AdvancedButton createButton(IInfoBook infoBook);

    /**
     * Draw the reward.
     * @param gui The gui.
     * @param guiGraphics The gui graphics object.
     * @param x Start X.
     * @param y Start Y.
     * @param width Max width.
     * @param height Max height.
     * @param page Current page.
     * @param mx Mouse X.
     * @param my Mouse Y.
     * @param button The button of this reward.
     */
    @OnlyIn(Dist.CLIENT)
    public void drawElementInner(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my, AdvancedButton button);
}
