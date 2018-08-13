package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.client.settings.KeyBinding;
import net.minecraft.util.text.TextFormatting;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.infobook.GuiInfoBook;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;

/**
 * An appendix for key bindings.
 * @author rubensworks
 */
public class KeyBindingAppendix extends SectionAppendix {

    public static final int WIDTH = 100;
    public static final int HEIGHT = 30;

    private final KeyBinding keyBinding;

    public KeyBindingAppendix(IInfoBook infoBook, KeyBinding keyBinding) {
        super(infoBook);
        this.keyBinding = keyBinding;
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
    protected void drawElement(GuiInfoBook gui, int x, int y, int width, int height, int page, int mx, int my) {
        gui.drawOuterBorder(x - 1, y - 1, getWidth() + 2, getHeight() + 2, 0.5F, 0.5F, 0.5F, 0.4f);
        gui.drawTextBanner(x + width / 2, y - 2);
        gui.drawScaledCenteredString(L10NHelpers.localize("infobook.cyclopscore.keybinding"), x, y - 2, width, 0.9f, gui.getBannerWidth() - 6, Helpers.RGBToInt(30, 20, 120));

        gui.drawScaledCenteredString(TextFormatting.ITALIC.toString() + L10NHelpers.localize(keyBinding.getKeyDescription()),
                x, y - 2 + 12, width, 0.9f, gui.getBannerWidth() + 8, Helpers.RGBToInt(30, 20, 120));

        String binding = L10NHelpers.localize(keyBinding.getDisplayName());
        int bindingWidth = gui.getFontRenderer().getStringWidth(binding) + 2;
        gui.drawOuterBorder(x + width / 2 - bindingWidth / 2 - 1, y + 17, bindingWidth, 10, 1, 1, 1, 0.2f);
        gui.drawScaledCenteredString(binding, x, y + 22, width, 0.9f, gui.getBannerWidth() - 6, Helpers.RGBToInt(30, 20, 120));
    }

    @Override
    protected void postDrawElement(GuiInfoBook gui, int x, int y, int width, int height, int page, int mx, int my) {

    }

    @Override
    public void preBakeElement(InfoSection infoSection) {

    }

    @Override
    public void bakeElement(InfoSection infoSection) {

    }

}
