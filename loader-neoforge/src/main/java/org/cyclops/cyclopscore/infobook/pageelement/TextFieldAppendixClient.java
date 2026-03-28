package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

/**
 * @author rubensworks
 */
public class TextFieldAppendixClient extends SectionAppendixClient<TextFieldAppendix> {
    protected TextFieldAppendixClient(TextFieldAppendix sectionAppendix) {
        super(sectionAppendix);
    }

    @Override
    protected void drawElement(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        int lineId = 0;
        for (String line : getSectionAppendix().getLines()) {
            IModHelpers.get().getRenderHelpers().drawScaledString(
                    guiGraphics,
                    gui.getFont(),
                    line,
                    x,
                    (int) (y + (((float) lineId) * gui.getFont().lineHeight * getSectionAppendix().getScale())),
                    (float) getSectionAppendix().getScale(),
                    IModHelpers.get().getBaseHelpers().RGBAToInt(10, 10, 10, 255),
                    false,
                    Font.DisplayMode.NORMAL
            );
            lineId++;
        }

        gui.drawOuterBorder(guiGraphics, x - 1, y - 1, getSectionAppendix().getWidth() + 2, getSectionAppendix().getHeight() + 2, 0.5F, 0.5F, 0.5F, 0.4f);
    }

    @Override
    protected void postDrawElement(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {

    }
}
