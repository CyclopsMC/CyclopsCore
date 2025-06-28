package org.cyclops.cyclopscore.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;

/**
 * @author rubensworks
 */
public class RenderHelpersForge extends RenderHelpersCommon implements IRenderHelpersForge {

    private final IModHelpers modHelpers;

    public RenderHelpersForge(IModHelpers modHelpers) {
        this.modHelpers = modHelpers;
    }

    @Override
    public TextureAtlasSprite getFluidIcon(Fluid fluid, Direction side) {
        return getFluidIcon(new FluidStack(fluid, 1000), side);
    }

    @Override
    public TextureAtlasSprite getFluidIcon(FluidStack fluid, Direction side) {
        if(side == null) side = Direction.UP;

        IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluid.getFluid());
        TextureAtlasSprite icon = getBlockTextureGetter().apply(renderProperties.getFlowingTexture(fluid));
        if(icon == null || (side == Direction.UP || side == Direction.DOWN)) {
            icon = getBlockTextureGetter().apply(renderProperties.getStillTexture(fluid));
        }

        return icon;
    }

    @Override
    public void renderFluidContext(FluidStack fluid, PoseStack matrixStack, IFluidContextRender render) {
        if(fluid != null && fluid.getAmount() > 0) {
            matrixStack.pushPose();
            render.render();
            matrixStack.popPose();
        }
    }

    @Override
    public Triple<Float, Float, Float> getFluidVertexBufferColor(FluidStack fluidStack) {
        IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        int color = renderProperties.getTintColor(fluidStack);
        return this.modHelpers.getBaseHelpers().intToRGB(color);
    }

    @Override
    public int getFluidBakedQuadColor(FluidStack fluidStack) {
        IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluidStack.getFluid());
        Triple<Float, Float, Float> colorParts = this.modHelpers.getBaseHelpers().intToRGB(renderProperties.getTintColor(fluidStack));
        return this.modHelpers.getBaseHelpers().RGBAToInt(
                (int) (colorParts.getRight() * 255),
                (int) (colorParts.getMiddle() * 255),
                (int) (colorParts.getLeft() * 255),
                255
        );
    }
}
