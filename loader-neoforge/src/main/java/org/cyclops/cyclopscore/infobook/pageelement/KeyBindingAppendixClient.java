package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.ChatFormatting;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.infobook.InfoBookParser;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

/**
 * @author rubensworks
 */
public class KeyBindingAppendixClient extends SectionAppendixClient<KeyBindingAppendix> {

    private KeyMapping keyBinding;

    protected KeyBindingAppendixClient(KeyBindingAppendix sectionAppendix) throws InfoBookParser.InvalidAppendixException {
        super(sectionAppendix);
    }

    public void loadKeybinding() throws InfoBookParser.InvalidAppendixException {
        this.keyBinding = KeyMapping.get(getSectionAppendix().getKeybindingName());
        if (this.keyBinding == null) {
            throw new InfoBookParser.InvalidAppendixException("Could not find a keybinding by name " + getSectionAppendix().getKeybindingName());
        }
    }

    @Override
    protected void drawElement(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        gui.drawOuterBorder(guiGraphics, x - 1, y - 1, getSectionAppendix().getWidth() + 2, getSectionAppendix().getHeight() + 2, 0.5F, 0.5F, 0.5F, 0.4f);
        gui.drawTextBanner(guiGraphics, x + width / 2, y - 2);
        gui.drawScaledCenteredString(guiGraphics, IModHelpers.get().getL10NHelpers().localize("gui." + getSectionAppendix().getInfoBook().getMod().getModId() + ".keybinding"), x, y - 2, width, 0.9f, gui.getBannerWidth() - 6, IModHelpers.get().getBaseHelpers().RGBAToInt(30, 20, 120, 255));

        gui.drawScaledCenteredString(guiGraphics, ChatFormatting.ITALIC.toString() + IModHelpers.get().getL10NHelpers().localize(keyBinding.getName()),
                x, y - 2 + 12, width, 0.9f, gui.getBannerWidth() + 8, IModHelpers.get().getBaseHelpers().RGBAToInt(30, 20, 120, 255));

        String binding = keyBinding.getTranslatedKeyMessage().getString();
        int bindingWidth = gui.getFont().width(binding) + 2;
        gui.drawOuterBorder(guiGraphics, x + width / 2 - bindingWidth / 2 - 1, y + 17, bindingWidth, 10, 1, 1, 1, 0.2f);
        gui.drawScaledCenteredString(guiGraphics, binding, x, y + 22, width, 0.9f, gui.getBannerWidth() - 6, IModHelpers.get().getBaseHelpers().RGBAToInt(30, 20, 120, 255));
    }

    @Override
    protected void postDrawElement(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {

    }
}
