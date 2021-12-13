package org.cyclops.cyclopscore.infobook.pageelement;

import com.mojang.blaze3d.matrix.MatrixStack;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

/**
 * Images that can be added to sections.
 * @author rubensworks
 */
public class ImageAppendix extends SectionAppendix {

    private static final int OFFSET_Y = 0;

    private ResourceLocation resource;
    private int width;
    private int height;

    public ImageAppendix(IInfoBook infoBook, ResourceLocation resource, int width, int height) {
        super(infoBook);
        this.resource = resource;
        this.width = width;
        this.height = height;
    }

    @Override
    protected int getOffsetY() {
        return OFFSET_Y;
    }

    @Override
    protected int getWidth() {
        return width;
    }

    @Override
    protected int getHeight() {
        return height;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void drawElement(ScreenInfoBook gui, MatrixStack matrixStack, int x, int y, int width, int height, int page, int mx, int my) {
        Minecraft.getInstance().getTextureManager().bind(resource);
        gui.blit(matrixStack, x, y, 0, 0, getWidth(), getHeight());
        gui.drawOuterBorder(matrixStack, x, y, getWidth(), getHeight(), 0.5F, 0.5F, 0.5F, 0.4f);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void postDrawElement(ScreenInfoBook gui, MatrixStack matrixStack, int x, int y, int width, int height, int page, int mx, int my) {

    }

    @Override
    public void preBakeElement(InfoSection infoSection) {

    }

    @Override
    public void bakeElement(InfoSection infoSection) {

    }
}
