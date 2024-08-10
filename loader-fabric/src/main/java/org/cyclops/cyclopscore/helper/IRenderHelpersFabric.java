package org.cyclops.cyclopscore.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.tuple.Triple;

/**
 * @author rubensworks
 */
public interface IRenderHelpersFabric extends IRenderHelpers {

    /**
     * Get the icon of a fluid for a side in a safe way.
     * @param fluid The fluid.
     * @param side The side to get the icon from, UP if null.
     * @return The icon.
     */
    public TextureAtlasSprite getFluidIcon(Fluid fluid, Direction side);

    /**
     * Get the icon of a fluid for a side in a safe way.
     * @param fluidVariant The fluid variant.
     * @param side The side to get the icon from, UP if null.
     * @return The icon.
     */
    public TextureAtlasSprite getFluidIcon(FluidVariant fluidVariant, Direction side);

    /**
     * Prepare a render system context for rendering fluids.
     * @param fluidVariant The fluid variant.
     * @param matrixStack The matrix stack.
     * @param render The actual fluid renderer.
     */
    public void renderFluidContext(FluidVariant fluidVariant, PoseStack matrixStack, IRenderHelpersFabric.IFluidContextRender render);

    /**
     * Get the fluid color to use in buffer rendering.
     * @param fluidVariant The fluid variant.
     * @return The RGB colors.
     */
    public Triple<Float, Float, Float> getFluidVertexBufferColor(FluidVariant fluidVariant);

    /**
     * Get the fluid color to use in a baked quad.
     * @param fluidVariant The fluid variant.
     * @return The BGR colors.
     */
    public int getFluidBakedQuadColor(FluidVariant fluidVariant);

    /**
     * Runnable for {@link IRenderHelpersFabric#renderFluidContext(FluidVariant, PoseStack, IRenderHelpersFabric.IFluidContextRender)}}}.
     * @author rubensworks
     */
    public static interface IFluidContextRender {

        /**
         * Render the fluid.
         */
        public void render();

    }

}
