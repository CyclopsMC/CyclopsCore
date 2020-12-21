package org.cyclops.cyclopscore.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.model.Model;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

/**
 * General renderer for {@link org.cyclops.cyclopscore.tileentity.CyclopsTileEntity}
 * with {@link net.minecraft.client.renderer.model.Model} models.
 * @author rubensworks
 *
 */
public class RenderTileEntityModelBase<T extends CyclopsTileEntity, M extends Model> extends RenderTileEntityModel<T, M> {

    public RenderTileEntityModelBase(TileEntityRendererDispatcher renderDispatcher, M model, RenderMaterial material) {
        super(renderDispatcher, model, material);
    }

    @Override
    protected void renderModel(T tile, M model, float partialTick, MatrixStack matrixStack,
                               IVertexBuilder vertexBuilder, IRenderTypeBuffer buffer,
                               int combinedLight, int combinedOverlay) {
        model.render(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1); // Last params are RGBA
    }
}
