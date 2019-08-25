package org.cyclops.cyclopscore.client.gui.image;

import com.mojang.blaze3d.platform.GlStateManager;
import lombok.Data;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.lwjgl.opengl.GL11;

/**
 * A wrapper that contains a reference to a {@link net.minecraft.util.ResourceLocation} and its sheet position.
 * @author rubensworks
 */
@Data
public class Image implements IImage {

    private final ResourceLocation resourceLocation;
    private final int sheetX, sheetY, sheetWidth, sheetHeight;

    public Image(ResourceLocation resourceLocation, int sheetX, int sheetY, int sheetWidth, int sheetHeight) {
        this.resourceLocation = resourceLocation;
        this.sheetX = sheetX;
        this.sheetY = sheetY;
        this.sheetWidth = sheetWidth;
        this.sheetHeight = sheetHeight;
    }

    @Override
    public void draw(AbstractGui gui, int x, int y) {
        RenderHelpers.bindTexture(resourceLocation);
        gui.blit(x, y, sheetX, sheetY, sheetWidth, sheetHeight);
    }

    @Override
    public void drawWorld(TextureManager textureManager, float x1, float x2, float y1, float y2, float z) {
        drawWorldWithAlpha(textureManager, x1, x2, y1, y2, z, 1);
    }

    @Override
    public void drawWorld(TextureManager textureManager, float x1, float x2, float y1, float y2) {
        drawWorldWithAlpha(textureManager, x1, x2, y1, y2, 1);
    }

    @Override
    public void drawWorld(TextureManager textureManager, float x2, float y2) {
        drawWorldWithAlpha(textureManager, x2, y2, 1);
    }

    @Override
    public void drawWorldWithAlpha(TextureManager textureManager, float x1, float x2, float y1, float y2, float z, float alpha) {
        GlStateManager.pushMatrix();
        BufferBuilder worldRenderer = Tessellator.getInstance().getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX_COLOR);
        textureManager.bindTexture(getResourceLocation());
        float u1, u2, v1, v2;
        u1 = (float) (getSheetX()) / 256F;
        u2 = (float) (getSheetX() + getSheetWidth()) / 256F;
        v1 = (float) (getSheetY()) / 256F;
        v2 = (float) (getSheetY() + getSheetHeight()) / 256F;
        int a = Math.round(alpha * 255F);
        worldRenderer.pos(x2, y2, z).tex(u2, v2).color(255, 255, 255, a).endVertex();
        worldRenderer.pos(x2, y1, z).tex(u2, v1).color(255, 255, 255, a).endVertex();
        worldRenderer.pos(x1, y1, z).tex(u1, v1).color(255, 255, 255, a).endVertex();
        worldRenderer.pos(x1, y2, z).tex(u1, v2).color(255, 255, 255, a).endVertex();
        Tessellator.getInstance().draw();
        GlStateManager.popMatrix();
    }

    @Override
    public void drawWorldWithAlpha(TextureManager textureManager, float x1, float x2, float y1, float y2, float alpha) {
        this.drawWorldWithAlpha(textureManager, x1, x2, y1, y2, 0, alpha);
    }

    @Override
    public void drawWorldWithAlpha(TextureManager textureManager, float x2, float y2, float alpha) {
        this.drawWorldWithAlpha(textureManager, 0, x2, 0, y2, alpha);
    }

    @Override
    public int getWidth() {
        return this.sheetWidth;
    }

    @Override
    public int getHeight() {
        return this.sheetHeight;
    }

}
