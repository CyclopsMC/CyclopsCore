package org.cyclops.cyclopscore.infobook.pageelement;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
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
    public boolean canObtain(PlayerEntity player);

    /**
     * The logic for obtaining this reward.
     * Will only be called server-side.
     * @param player The player.
     */
    public void obtain(PlayerEntity player);

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
     * @param matrixStack The matrix stack.
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
    public void drawElementInner(ScreenInfoBook gui, MatrixStack matrixStack, int x, int y, int width, int height, int page, int mx, int my, AdvancedButton button);
}
