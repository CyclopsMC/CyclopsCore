package org.cyclops.cyclopscore.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;

/**
 * @author rubensworks
 */
public interface IRenderHelpersNeoForge extends IRenderHelpers {

    /**
     * Get the icon of a fluid for a side in a safe way.
     * @param fluid The fluid.
     * @param side The side to get the icon from, UP if null.
     * @return The icon.
     */
    public TextureAtlasSprite getFluidIcon(Fluid fluid, Direction side);

    /**
     * Get the icon of a fluid for a side in a safe way.
     * @param fluid The fluid stack.
     * @param side The side to get the icon from, UP if null.
     * @return The icon.
     */
    public TextureAtlasSprite getFluidIcon(FluidStack fluid, Direction side);

    /**
     * Prepare a render system context for rendering fluids.
     * @param fluid The fluid stack.
     * @param matrixStack The matrix stack.
     * @param render The actual fluid renderer.
     */
    public void renderFluidContext(FluidStack fluid, PoseStack matrixStack, IRenderHelpersNeoForge.IFluidContextRender render);

    /**
     * Get the fluid color to use in buffer rendering.
     * @param fluidStack The fluid stack.
     * @return The RGB colors.
     */
    public Triple<Float, Float, Float> getFluidVertexBufferColor(FluidStack fluidStack);

    /**
     * Get the fluid color to use in a baked quad.
     * @param fluidStack The fluid stack.
     * @return The BGR colors.
     */
    public int getFluidBakedQuadColor(FluidStack fluidStack);

    /**
     * Runnable for {@link IRenderHelpersNeoForge#renderFluidContext(FluidStack, PoseStack, IRenderHelpersNeoForge.IFluidContextRender)}}}.
     * @author rubensworks
     */
    public static interface IFluidContextRender {

        /**
         * Render the fluid.
         */
        public void render();

    }

}
