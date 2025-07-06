package org.cyclops.cyclopscore.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ItemDecoratorHandler;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.jetbrains.annotations.Nullable;

/**
 * Additional helper functions related to {@link GuiGraphics}.
 * @author rubensworks
 */
public class GuiGraphicsExtended {

    private final GuiGraphics guiGraphics;

    public GuiGraphicsExtended(GuiGraphics guiGraphics) {
        this.guiGraphics = guiGraphics;
    }

    public void drawSlotText(Font font, @javax.annotation.Nullable String text, int x, int y) { // Abstracted for reuse
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().translate(0.0F, 0.0F);
        float scale = 0.5f; // This part was added
        guiGraphics.pose().scale(scale, scale); // This part was added
        guiGraphics.drawString(font, text, (int) ((x + 19 - 2) / scale - font.width(text)), (int) ((y + 6 + 6) / scale), -1, true); // Scale was added here
        guiGraphics.pose().popMatrix();
    }

    private void renderItemCount(Font font, ItemStack stack, int x, int y, @javax.annotation.Nullable String text) {
        if (stack.getCount() != 1 || text != null) {
            String s = text == null ? IModHelpers.get().getGuiHelpers().quantityToScaledString(stack.getCount()) : text; // This part was changed
            drawSlotText(font, s, x, y);
        }
    }

    public void renderItemDecorations(Font font, ItemStack stack, int x, int y) {
        this.renderItemDecorations(font, stack, x, y, (String)null);
    }

    public void renderItemDecorations(Font font, ItemStack stack, int x, int y, @Nullable String text) {
        // ----- Copied and adjusted from GuiGraphics#renderItemDecorations -----
        if (!stack.isEmpty()) {
            this.guiGraphics.pose().pushMatrix();
            this.guiGraphics.renderItemBar(stack, x, y);
            this.guiGraphics.renderItemCooldown(stack, x, y);
            this.renderItemCount(font, stack, x, y, text); // Changed line
            this.guiGraphics.pose().popMatrix();
            ItemDecoratorHandler.of(stack).render(this.guiGraphics, font, stack, x, y);
        }
    }
}
