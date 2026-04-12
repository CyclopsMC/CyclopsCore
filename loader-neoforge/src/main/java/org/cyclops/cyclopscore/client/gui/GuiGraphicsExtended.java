package org.cyclops.cyclopscore.client.gui;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.client.ItemDecoratorHandler;
import org.jetbrains.annotations.Nullable;

/**
 * Additional helper functions related to {@link GuiGraphicsExtractor}.
 * @author rubensworks
 */
public class GuiGraphicsExtended {

    private final GuiGraphicsExtractor guiGraphics;

    public GuiGraphicsExtended(GuiGraphicsExtractor guiGraphics) {
        this.guiGraphics = guiGraphics;
    }

    private void itemCount(Font font, ItemStack itemStack, int x, int y, @org.jspecify.annotations.Nullable String countText) {
        if (itemStack.getCount() != 1 || countText != null) {
            String amount = countText == null ? String.valueOf(itemStack.getCount()) : countText;
            guiGraphics.pose().pushMatrix(); // This part was added
            float scale = 0.5f; // This part was added
            guiGraphics.pose().scale(scale, scale); // This part was added
            guiGraphics.text(font, amount, (int) ((x + 19 - 2) / scale - font.width(amount)), (int) ((y + 6 + 6) / scale), -1, true); // Scale was added here
            guiGraphics.pose().popMatrix(); // This part was added
        }
    }

    public void drawSlotText(Font font, String text, int x, int y) {
        float scale = 0.5f;
        guiGraphics.pose().pushMatrix();
        guiGraphics.pose().scale(scale, scale);
        guiGraphics.text(font, text, (int) ((x + 17) / scale - font.width(text)), (int) ((y + 12) / scale), -1, true);
        guiGraphics.pose().popMatrix();
    }

    public void itemDecorations(Font font, ItemStack stack, int x, int y) {
        this.itemDecorations(font, stack, x, y, (String)null);
    }

    public void itemDecorations(Font font, ItemStack itemStack, int x, int y, @Nullable String text) {
        // ----- Copied and adjusted from GuiGraphicsExtractor#itemDecorations -----
        if (!itemStack.isEmpty()) {
            this.guiGraphics.pose().pushMatrix();
            this.guiGraphics.itemBar(itemStack, x, y);
            this.guiGraphics.itemCooldown(itemStack, x, y);
            this.itemCount(font, itemStack, x, y, text); // Changed line
            this.guiGraphics.pose().popMatrix();
            ItemDecoratorHandler.of(itemStack).render(this.guiGraphics, font, itemStack, x, y);
        }
    }
}
