package org.cyclops.cyclopscore.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.resources.model.Material;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;

/**
 * General renderer for {@link CyclopsBlockEntity} with {@link Model} models.
 * @author rubensworks
 *
 */
public class RenderBlockEntityModelBase<T extends CyclopsBlockEntity, M extends Model> extends RenderBlockEntityModel<T, M> {

    public RenderBlockEntityModelBase(M model, Material material) {
        super(model, material);
    }

    @Override
    protected void renderModel(T tile, M model, float partialTick, PoseStack matrixStack,
                               VertexConsumer vertexBuilder, MultiBufferSource buffer,
                               int combinedLight, int combinedOverlay) {
        model.renderToBuffer(matrixStack, vertexBuilder, combinedLight, combinedOverlay, 1, 1, 1, 1); // Last params are RGBA
    }
}
