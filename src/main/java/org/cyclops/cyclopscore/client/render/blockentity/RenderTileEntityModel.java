package org.cyclops.cyclopscore.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Vector3f;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;

import java.util.function.Function;

/**
 * General renderer for {@link CyclopsBlockEntity} with models.
 * @author rubensworks
 *
 */
public abstract class RenderTileEntityModel<T extends CyclopsBlockEntity, M> implements BlockEntityRenderer<T> {

    protected final M model;
	private final Material material;

    /**
     * Make a new instance.
     * @param model The model to render.
     * @param material The material to render the model with.
     */
    public RenderTileEntityModel(M model, Material material) {
        this.model = model;
        this.material = material;
    }

    public M getModel() {
        return model;
    }

    /**
     * Get the material.
     * @return The material.
     */
	public Material getMaterial() {
		return material;
	}

	public Function<ResourceLocation, RenderType> getRenderTypeGetter() {
	    return RenderType::entityCutout;
    }

    protected void preRotate(T tile, PoseStack matrixStack) {
        matrixStack.translate(0.5F, 0.5F, 0.5F);
    }

    protected void postRotate(T tile, PoseStack matrixStack) {
        matrixStack.translate(-0.5F, -0.5F, -0.5F);
    }

    @Override
    public void render(T tile, float partialTick, PoseStack matrixStack, MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        Direction direction = tile.getRotation();

        VertexConsumer vertexBuilder = material.buffer(buffer, getRenderTypeGetter());

        matrixStack.pushPose();
        matrixStack.translate(0, 1.0F, 1.0F);
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        preRotate(tile, matrixStack);
        short rotation = 0;

        if (direction == Direction.SOUTH) {
            rotation = 180;
        }
        if (direction == Direction.NORTH) {
            rotation = 0;
        }
        if (direction == Direction.EAST) {
            rotation = 90;
        }
        if (direction == Direction.WEST) {
            rotation = -90;
        }

        matrixStack.mulPose(Vector3f.YP.rotationDegrees(rotation));
        postRotate(tile, matrixStack);

        renderModel(tile, getModel(), partialTick, matrixStack, vertexBuilder, buffer, combinedLight, combinedOverlay);
        matrixStack.popPose();
    }

    /**
     * Render the actual model, override this to change the way the model should be rendered.
     * @param tile The tile entity.
     * @param model The base model.
     * @param partialTick The partial tick value.
     * @param matrixStack The matrix stack.
     * @param vertexBuilder The vertex builder.
     * @param buffer The render type buffer.
     * @param combinedLight The combined light value.
     * @param combinedOverlay The combined overlay value.
     */
    protected abstract void renderModel(T tile, M model, float partialTick, PoseStack matrixStack,
                                        VertexConsumer vertexBuilder, MultiBufferSource buffer,
                                        int combinedLight, int combinedOverlay);
}
