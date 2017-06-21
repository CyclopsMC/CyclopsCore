package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.util.text.TextFormatting;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * A button for the advancements, so they can be hovered.
 */
public class AdvancementButton extends AdvancedButton {

    private final Advancement advancement;

    public AdvancementButton(Advancement advancement) {
        this.advancement = advancement;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void renderTooltip(int mx, int my) {
        super.renderTooltip(mx, my);
        GlStateManager.pushMatrix();
        if(mx >= x && my >= y && mx <= x + AdvancementRewardsAppendix.SLOT_SIZE && my <= y + AdvancementRewardsAppendix.SLOT_SIZE) {
            List<String> lines = Lists.newArrayList();
            lines.add(advancement.getDisplay().getTitle().getFormattedText());
            lines.add(TextFormatting.GRAY.toString() + advancement.getDisplay().getDescription().getFormattedText());
            gui.drawHoveringText(lines, mx, my);
        }
        GlStateManager.popMatrix();

        GlStateManager.disableLighting();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
}
