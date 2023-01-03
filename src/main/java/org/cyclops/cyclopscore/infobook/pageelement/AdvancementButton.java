package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
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
    public void renderTooltip(PoseStack matrixStack, int mx, int my) {
        super.renderTooltip(matrixStack, mx, my);
        matrixStack.pushPose();
        if(mx >= getX() && my >= getY() && mx <= getX() + AdvancementRewardsAppendix.SLOT_SIZE && my <= getY() + AdvancementRewardsAppendix.SLOT_SIZE) {
            List<FormattedCharSequence> lines = Lists.newArrayList();
            Advancement advancement = AdvancementHelpers.getAdvancement(Dist.CLIENT, advancementId);
            if (advancement != null) {
                lines.add(advancement.getDisplay().getTitle().getVisualOrderText());
                lines.add(advancement.getDisplay().getDescription().getVisualOrderText());
            }
            gui.renderTooltip(matrixStack, lines, mx, my);
        }
        matrixStack.popPose();

        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
}
