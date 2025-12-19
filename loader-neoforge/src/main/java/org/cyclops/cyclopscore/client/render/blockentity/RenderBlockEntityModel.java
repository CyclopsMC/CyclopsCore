package org.cyclops.cyclopscore.client.render.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.CameraRenderState;
import net.minecraft.client.resources.model.Material;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntity;

import java.util.function.Function;

/**
 * General renderer for {@link CyclopsBlockEntity} with models.
 * @author rubensworks
 *
 */
public abstract class RenderBlockEntityModel<T extends CyclopsBlockEntity, S extends BlockEntityRenderState & RenderBlockEntityModel.IRotationRenderState, M> implements BlockEntityRenderer<T, S> {

    protected final M model;
    private final Material material;

    /**
     * Make a new instance.
     * @param model The model to render.
     * @param material The material to render the model with.
     */
    public RenderBlockEntityModel(M model, Material material) {
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

    public Function<Identifier, RenderType> getRenderTypeGetter() {
        return RenderTypes::entityCutout;
    }

    protected void preRotate(S renderState, PoseStack matrixStack) {
        matrixStack.translate(0.5F, 0.5F, 0.5F);
    }

    protected void postRotate(S renderState, PoseStack matrixStack) {
        matrixStack.translate(-0.5F, -0.5F, -0.5F);
    }

    @Override
    public void submit(S renderState, PoseStack matrixStack, SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState) {
        Direction direction = renderState.rotation();

        matrixStack.pushPose();
        matrixStack.translate(0, 1.0F, 1.0F);
        matrixStack.scale(1.0F, -1.0F, -1.0F);
        preRotate(renderState, matrixStack);
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

        matrixStack.mulPose(Axis.YP.rotationDegrees(rotation));
        postRotate(renderState, matrixStack);

        submitModel(renderState, getModel(), matrixStack, nodeCollector, cameraRenderState);
        matrixStack.popPose();
    }

    /**
     * Render the actual model, override this to change the way the model should be rendered.
     * @param renderState The tile entity render state.
     * @param model The base model.
     * @param matrixStack The matrix stack.
     * @param nodeCollector The node collector.
     * @param cameraRenderState The camera.
     */
    protected abstract void submitModel(S renderState, M model, PoseStack matrixStack,
                                        SubmitNodeCollector nodeCollector, CameraRenderState cameraRenderState);

    public static interface IRotationRenderState {
        public Direction rotation();
    }
}
