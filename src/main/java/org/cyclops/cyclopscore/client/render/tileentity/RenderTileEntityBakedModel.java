package org.cyclops.cyclopscore.client.render.tileentity;

import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.block.BlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.model.IBakedModel;
import net.minecraft.client.renderer.texture.AtlasTexture;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

/**
 * General renderer for {@link org.cyclops.cyclopscore.tileentity.CyclopsTileEntity} with {@link IBakedModel} models.
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

    public void renderTileEntityAt(T tile, double x, double y, double z, float partialTick, int destroyStage) {
        GlStateManager.enableRescaleNormal();
        GlStateManager.alphaFunc(516, 0.1F);
        GlStateManager.enableBlend();
        GlStateManager.blendFuncSeparate(770, 771, 1, 0);
        GlStateManager.pushMatrix();
        GlStateManager.pushTextureAttributes();

        GlStateManager.enableRescaleNormal();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        GlStateManager.translatef((float) x, (float) y, (float) z);
        GlStateManager.disableRescaleNormal();

        Minecraft mc = Minecraft.getInstance();
        BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
        BlockModelShapes blockModelShapes = blockRendererDispatcher.getBlockModelShapes();
        IBakedModel bakedModel = blockModelShapes.getModel(getBlockState(tile, x, y, z, partialTick, destroyStage));
        Minecraft.getInstance().getTextureManager().bindTexture(AtlasTexture.LOCATION_BLOCKS_TEXTURE);
        renderModel(tile, bakedModel, partialTick, destroyStage);

        GlStateManager.popAttributes();
        GlStateManager.popMatrix();
        GlStateManager.disableRescaleNormal();
        GlStateManager.disableBlend();
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected abstract BlockState getBlockState(T tile, double x, double y, double z, float partialTick, int destroyStage);

    @Override
    protected void renderModel(T tile, IBakedModel model, float partialTick, int destroyStage) {
        Minecraft.getInstance().getBlockRendererDispatcher().getBlockModelRenderer().
                renderModelBrightnessColor(model, 1.0F, 1.0F, 1.0F, 1.0F);
    }
}
