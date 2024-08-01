package org.cyclops.cyclopscore.client.gui;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.Mth;
import net.minecraft.world.item.ItemStack;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.GuiHelpers;
import org.jetbrains.annotations.Nullable;

/**
 * Additional helper functions related to {@link GuiGraphics}.
 * @author rubensworks
 */
@OnlyIn(Dist.CLIENT)
public class GuiGraphicsExtended {

    private final GuiGraphics guiGraphics;

    public GuiGraphicsExtended(GuiGraphics guiGraphics) {
        this.guiGraphics = guiGraphics;
    }

    public void drawSlotText(Font font, @javax.annotation.Nullable String text, int x, int y) { // Abstracted for reuse
        guiGraphics.pose().pushPose();
        guiGraphics.pose().translate(0.0F, 0.0F, 200.0F);
        float scale = 0.5f; // This part was added
        guiGraphics.pose().scale(scale, scale, 1.0f); // This part was added
        guiGraphics.drawString(font, text, (x + 19 - 2) / scale - font.width(text), (float)(y + 6 + 6) / scale, 16777215, true); // Scale was added here
        guiGraphics.pose().popPose();
    }

    public void renderItemDecorations(Font font, ItemStack stack, int x, int y) {
        this.renderItemDecorations(font, stack, x, y, (String)null);
    }

    public void renderItemDecorations(Font font, ItemStack stack, int x, int y, @Nullable String text) {
        // ----- Copied and adjusted from GuiGraphics#renderItemDecorations -----
        if (!stack.isEmpty()) {
            guiGraphics.pose().pushPose();
            if (stack.getCount() != 1 || text != null) {
                String s = text == null ? GuiHelpers.quantityToScaledString(stack.getCount()) : text; // This part was changed
                drawSlotText(font, s, x, y); // New function
            }

            if (stack.isBarVisible()) {
                int l = stack.getBarWidth();
                int i = stack.getBarColor();
                int j = x + 2;
                int k = y + 13;
                guiGraphics.fill(RenderType.guiOverlay(), j, k, j + 13, k + 2, -16777216);
                guiGraphics.fill(RenderType.guiOverlay(), j, k, j + l, k + 1, i | -16777216);
            }

            Minecraft minecraft = Minecraft.getInstance();
            LocalPlayer localplayer = minecraft.player;
            float f = localplayer == null ? 0.0F : localplayer.getCooldowns().getCooldownPercent(stack.getItem(), minecraft.getTimer().getGameTimeDeltaPartialTick(true));
            if (f > 0.0F) {
                int i1 = y + Mth.floor(16.0F * (1.0F - f));
                int j1 = i1 + Mth.ceil(16.0F * f);
                guiGraphics.fill(RenderType.guiOverlay(), x, i1, x + 16, j1, Integer.MAX_VALUE);
            }

            guiGraphics.pose().popPose();
            net.neoforged.neoforge.client.ItemDecoratorHandler.of(stack).render(guiGraphics, font, stack, x, y);
        }
    }
}
