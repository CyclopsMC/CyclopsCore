package org.cyclops.cyclopscore.client.gui.image;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import lombok.Data;
import net.minecraft.client.gui.AbstractGui;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.util.math.vector.Matrix4f;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.helper.RenderHelpers;

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
    public void draw(AbstractGui gui, MatrixStack matrixStack, int x, int y) {
        RenderHelpers.bindTexture(resourceLocation);
        gui.blit(matrixStack, x, y, sheetX, sheetY, sheetWidth, sheetHeight);
    }

    @Override
    public void drawWorldWithAlpha(TextureManager textureManager, MatrixStack matrixStack, IRenderTypeBuffer renderTypeBuffer,
                                   int combinedLight, int combinedOverlay, float x1, float x2, float y1, float y2, float z, float alpha) {
        matrixStack.push();
        IVertexBuilder vb = renderTypeBuffer.getBuffer(RenderType.getText(getResourceLocation()));
        float u1, u2, v1, v2;
        u1 = (float) (getSheetX()) / 256F;
        u2 = (float) (getSheetX() + getSheetWidth()) / 256F;
        v1 = (float) (getSheetY()) / 256F;
        v2 = (float) (getSheetY() + getSheetHeight()) / 256F;
        int a = Math.round(alpha * 255F);
        Matrix4f matrix = matrixStack.getLast().getMatrix();
        vb.pos(matrix, x2, y2, z).color(255, 255, 255, a).tex(u2, v2).lightmap(combinedLight).endVertex();
        vb.pos(matrix, x2, y1, z).color(255, 255, 255, a).tex(u2, v1).lightmap(combinedLight).endVertex();
        vb.pos(matrix, x1, y1, z).color(255, 255, 255, a).tex(u1, v1).lightmap(combinedLight).endVertex();
        vb.pos(matrix, x1, y2, z).color(255, 255, 255, a).tex(u1, v2).lightmap(combinedLight).endVertex();
        matrixStack.pop();
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
