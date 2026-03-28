package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

/**
 * @author rubensworks
 */
public interface IRewardClient {
    /**
     * @param infoBook The infobook instance.
     * @return Factory for a button for this reward.
     */
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
    public void drawElementInner(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height, int page, int mx, int my, AdvancedButton button);
}
