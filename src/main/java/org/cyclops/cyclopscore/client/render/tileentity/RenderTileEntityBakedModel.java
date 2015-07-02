package org.cyclops.cyclopscore.client.render.tileentity;

import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.client.resources.model.IBakedModel;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

/**
 * General renderer for {@link org.cyclops.cyclopscore.tileentity.CyclopsTileEntity} with {@link net.minecraft.client.resources.model.IBakedModel} models.
 * @author rubensworks
 *
 */
public abstract class RenderTileEntityBakedModel<T extends CyclopsTileEntity> extends RenderTileEntityModel<T, IBakedModel> {

    /**
     * Make a new instance.
     */
    public RenderTileEntityBakedModel() {
        super(null, null);
    }

    protected void renderTileEntityAt(T tile, double x, double y, double z, float partialTick, int destroyStage) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.tryBlendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        GlStateManager.pushAttrib();

        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translate((float) x, (float) y, (float) z);
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);

        Minecraft mc = Minecraft.getMinecraft();
        BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
        BlockModelShapes blockModelShapes = blockRendererDispatcher.getBlockModelShapes();
        IBakedModel bakedModel = blockModelShapes.getModelForState(getBlockState(tile, x, y, z, partialTick, destroyStage));
        Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.locationBlocksTexture);
        renderModel(tile, bakedModel, partialTick);

        GlStateManager.popAttrib();
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected abstract IBlockState getBlockState(T tile, double x, double y, double z, float partialTick, int destroyStage);

    @Override
    protected void renderModel(T tile, IBakedModel model, float partialTick) {
        Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelRenderer().
                renderModelBrightnessColor(model, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
