package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.AdvancementHelpers;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * A button for the advancements, so they can be hovered.
 */
@OnlyIn(Dist.CLIENT)
public class AdvancementButton extends AdvancedButton {

    private final ResourceLocation advancementId;

    public AdvancementButton(ResourceLocation advancementId) {
        this.advancementId = advancementId;
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void renderTooltip(GuiGraphics guiGraphics, Font font, int mx, int my) {
        super.renderTooltip(guiGraphics, font, mx, my);
        guiGraphics.pose().pushPose();
        if(mx >= getX() && my >= getY() && mx <= getX() + AdvancementRewardsAppendix.SLOT_SIZE && my <= getY() + AdvancementRewardsAppendix.SLOT_SIZE) {
            List<FormattedCharSequence> lines = Lists.newArrayList();
            AdvancementHolder advancement = AdvancementHelpers.getAdvancement(Dist.CLIENT, advancementId);
            if (advancement != null) {
                advancement.value().display().ifPresent(display -> {
                    lines.add(display.getTitle().getVisualOrderText());
                    lines.add(display.getDescription().getVisualOrderText());
                });
            }
            guiGraphics.renderTooltip(font, lines, mx, my);
        }
        guiGraphics.pose().popPose();

        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
}
