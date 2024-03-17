package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphics;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoBookParser;
import org.cyclops.cyclopscore.infobook.InfoSection;

/**
 * An appendix for key bindings.
 * @author rubensworks
 */
public class KeyBindingAppendix extends SectionAppendix {

    public static final int WIDTH = 100;
    public static final int HEIGHT = 30;

    @OnlyIn(Dist.CLIENT)
    private KeyMapping keyBinding;

    public KeyBindingAppendix(IInfoBook infoBook, String keybindingName) throws InfoBookParser.InvalidAppendixException {
        super(infoBook);
        if (MinecraftHelpers.isClientSide()) {
            this.keyBinding = KeyMapping.ALL.get(keybindingName);
            if (this.keyBinding == null) {
                throw new InfoBookParser.InvalidAppendixException("Could not find a keybinding by name " + keybindingName);
            }
        }
    }

    @Override
    protected int getOffsetY() {
        return 0;
    }

    @Override
    protected int getWidth() {
        return WIDTH;
    }

    @Override
    protected int getHeight() {
        return HEIGHT;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void drawElement(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        gui.drawOuterBorder(guiGraphics, x - 1, y - 1, getWidth() + 2, getHeight() + 2, 0.5F, 0.5F, 0.5F, 0.4f);
        gui.drawTextBanner(guiGraphics, x + width / 2, y - 2);
        gui.drawScaledCenteredString(guiGraphics, L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".keybinding"), x, y - 2, width, 0.9f, gui.getBannerWidth() - 6, Helpers.RGBToInt(30, 20, 120));

        gui.drawScaledCenteredString(guiGraphics, ChatFormatting.ITALIC.toString() + L10NHelpers.localize(keyBinding.getName()),
                x, y - 2 + 12, width, 0.9f, gui.getBannerWidth() + 8, Helpers.RGBToInt(30, 20, 120));

        String binding = L10NHelpers.localize(keyBinding.saveString());
        int bindingWidth = gui.getFont().width(binding) + 2;
        gui.drawOuterBorder(guiGraphics, x + width / 2 - bindingWidth / 2 - 1, y + 17, bindingWidth, 10, 1, 1, 1, 0.2f);
        gui.drawScaledCenteredString(guiGraphics, binding, x, y + 22, width, 0.9f, gui.getBannerWidth() - 6, Helpers.RGBToInt(30, 20, 120));
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void postDrawElement(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {

    }

    @Override
    public void preBakeElement(InfoSection infoSection) {

    }

    @Override
    public void bakeElement(InfoSection infoSection) {

    }

}
