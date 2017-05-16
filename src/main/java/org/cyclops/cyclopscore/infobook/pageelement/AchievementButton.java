package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.stats.Achievement;
import net.minecraft.util.text.TextFormatting;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * A button for the achievements, so they can be hovered.
 */
public class AchievementButton extends AdvancedButton {

    private final Achievement achievement;

    public AchievementButton(Achievement achievement) {
        this.achievement = achievement;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void renderTooltip(int mx, int my) {
        super.renderTooltip(mx, my);
        GlStateManager.pushMatrix();
        int x = xPosition;
        int y = yPosition;
        if(mx >= x && my >= y && mx <= x + AchievementRewardsAppendix.SLOT_SIZE && my <= y + AchievementRewardsAppendix.SLOT_SIZE) {
            List<String> lines = Lists.newArrayList();
            lines.add(achievement.getStatName().getFormattedText());
            lines.add(TextFormatting.GRAY.toString() + achievement.getDescription());
            gui.drawHoveringText(lines, mx, my);
        }
        GlStateManager.popMatrix();

        GlStateManager.disableLighting();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
}
