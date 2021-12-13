package org.cyclops.cyclopscore.client.render.tileentity;

import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.vertex.IVertexBuilder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import net.minecraft.client.renderer.IRenderTypeBuffer;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.model.RenderMaterial;
import net.minecraft.client.renderer.tileentity.TileEntityRenderer;
import net.minecraft.client.renderer.tileentity.TileEntityRendererDispatcher;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import org.cyclops.cyclopscore.tileentity.CyclopsTileEntity;

import java.util.function.Function;

/**
 * General renderer for {@link org.cyclops.cyclopscore.tileentity.CyclopsTileEntity} with models.
 * @author rubensworks
 *
 */
public abstract class RenderTileEntityModel<T extends CyclopsTileEntity, M> extends TileEntityRenderer<T> {

    protected final M model;
	private final RenderMaterial material;

    /**
     * Make a new instance.
     * @param renderDispatcher The render dispatcher
     * @param model The model to render.
     * @param material The material to render the model with.
     */
    public RenderTileEntityModel(TileEntityRendererDispatcher renderDispatcher, M model, RenderMaterial material) {
        super(renderDispatcher);
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
	public RenderMaterial getMaterial() {
		return material;
	}

	public Function<ResourceLocation, RenderType> getRenderTypeGetter() {
	    return RenderType::entityCutout;
    }

    protected void preRotate(T tile) {
        GlStateManager._translatef(0.5F, 0.5F, 0.5F);
    }

    protected void postRotate(T tile) {
        GlStateManager._translatef(-0.5F, -0.5F, -0.5F);
    }

    @Override
    public void render(T tile, float partialTick, MatrixStack matrixStack, IRenderTypeBuffer buffer, int combinedLight, int combinedOverlay) {
        Direction direction = tile.getRotation();

        IVertexBuilder vertexBuilder = material.buffer(buffer, getRenderTypeGetter());

        matrixStack.pushPose();
        matrixStack.translate(0, 1.0F, 1.0F);
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        preRotate(tile);
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
        postRotate(tile);

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
    protected abstract void renderModel(T tile, M model, float partialTick, MatrixStack matrixStack,
                                        IVertexBuilder vertexBuilder, IRenderTypeBuffer buffer,
                                        int combinedLight, int combinedOverlay);
}
