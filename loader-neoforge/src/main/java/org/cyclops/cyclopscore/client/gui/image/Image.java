package org.cyclops.cyclopscore.client.gui.image;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import lombok.Data;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.joml.Matrix4f;

/**
 * A wrapper that contains a reference to a {@link  net.minecraft.resources.ResourceLocation} and its sheet position.
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
    public void draw(GuiGraphics gui, int x, int y) {
        gui.blit(resourceLocation, x, y, sheetX, sheetY, sheetWidth, sheetHeight);
    }

    @Override
    public void drawWithColor(GuiGraphics gui, int x, int y, float r, float g, float b, float a) {
        RenderHelpers.bindTexture(resourceLocation);
        RenderHelpers.blitColored(gui, x, y, 0, sheetX, sheetY, sheetWidth, sheetHeight, r, g, b, a);
    }

    @Override
    public void drawWorldWithAlpha(TextureManager textureManager, PoseStack matrixStack, MultiBufferSource renderTypeBuffer,
                                   int combinedLight, int combinedOverlay, float x1, float x2, float y1, float y2, float z, float alpha) {
        matrixStack.pushPose();
        VertexConsumer vb = renderTypeBuffer.getBuffer(RenderType.text(getResourceLocation()));
        float u1, u2, v1, v2;
        u1 = (float) (getSheetX()) / 256F;
        u2 = (float) (getSheetX() + getSheetWidth()) / 256F;
        v1 = (float) (getSheetY()) / 256F;
        v2 = (float) (getSheetY() + getSheetHeight()) / 256F;
        int a = Math.round(alpha * 255F);
        Matrix4f matrix = matrixStack.last().pose();
        vb.addVertex(matrix, x2, y2, z).setColor(255, 255, 255, a).setUv(u2, v2).setLight(combinedLight);
        vb.addVertex(matrix, x2, y1, z).setColor(255, 255, 255, a).setUv(u2, v1).setLight(combinedLight);
        vb.addVertex(matrix, x1, y1, z).setColor(255, 255, 255, a).setUv(u1, v1).setLight(combinedLight);
        vb.addVertex(matrix, x1, y2, z).setColor(255, 255, 255, a).setUv(u1, v2).setLight(combinedLight);
        matrixStack.popPose();
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
