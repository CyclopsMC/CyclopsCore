package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

/**
 * @author rubensworks
 */
public class ImageAppendixClient extends SectionAppendixClient<ImageAppendix> {
    protected ImageAppendixClient(ImageAppendix sectionAppendix) {
        super(sectionAppendix);
    }

    @Override
    protected void drawElement(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, getSectionAppendix().getResource(), x, y, 0, 0, getSectionAppendix().getWidth(), getSectionAppendix().getHeight(), 256, 256);
        gui.drawOuterBorder(guiGraphics, x, y, getSectionAppendix().getWidth(), getSectionAppendix().getHeight(), 0.5F, 0.5F, 0.5F, 0.4f);
    }

    @Override
    protected void postDrawElement(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {

    }
}
