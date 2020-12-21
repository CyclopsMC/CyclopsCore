package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.advancements.Advancement;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
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
    public void renderTooltip(MatrixStack matrixStack, int mx, int my) {
        super.renderTooltip(matrixStack, mx, my);
        GlStateManager.pushMatrix();
        if(mx >= x && my >= y && mx <= x + AdvancementRewardsAppendix.SLOT_SIZE && my <= y + AdvancementRewardsAppendix.SLOT_SIZE) {
            List<IReorderingProcessor> lines = Lists.newArrayList();
            Advancement advancement = AdvancementHelpers.getAdvancement(Dist.CLIENT, advancementId);
            if (advancement != null) {
                lines.add(advancement.getDisplay().getTitle().func_241878_f());
                lines.add(advancement.getDisplay().getDescription().func_241878_f());
            }
            gui.renderTooltip(matrixStack, lines, mx, my);
        }
        GlStateManager.popMatrix();

        GlStateManager.disableLighting();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }
}
