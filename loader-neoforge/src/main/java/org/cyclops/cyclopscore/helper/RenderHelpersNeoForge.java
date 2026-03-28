package org.cyclops.cyclopscore.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;
import net.minecraft.client.renderer.block.FluidModel;
import org.apache.commons.lang3.tuple.Triple;

/**
 * @author rubensworks
 */
public class RenderHelpersNeoForge extends RenderHelpersCommon implements IRenderHelpersNeoForge {

    private final IModHelpers modHelpers;

    public RenderHelpersNeoForge(IModHelpers modHelpers) {
        this.modHelpers = modHelpers;
    }

    private FluidModel getFluidModel(Fluid fluid) {
        return Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluid.defaultFluidState());
    }

    @Override
    public TextureAtlasSprite getFluidIcon(Fluid fluid, Direction side) {
        return getFluidIcon(new FluidStack(fluid, 1000), side);
    }

    @Override
    public TextureAtlasSprite getFluidIcon(FluidStack fluid, Direction side) {
        if (side == null) side = Direction.UP;
        FluidModel model = getFluidModel(fluid.getFluid());
        if (side == Direction.UP || side == Direction.DOWN) {
            return model.stillMaterial().sprite();
        }
        return model.flowingMaterial().sprite();
    }

    @Override
    public void renderFluidContext(FluidStack fluid, PoseStack matrixStack, IFluidContextRender render) {
        if (fluid != null && fluid.getAmount() > 0) {
            matrixStack.pushPose();
            render.render();
            matrixStack.popPose();
        }
    }

    @Override
    public Triple<Float, Float, Float> getFluidVertexBufferColor(FluidStack fluidStack) {
        int color = getFluidTintColor(fluidStack);
        return this.modHelpers.getBaseHelpers().intToRGB(color);
    }

    @Override
    public int getFluidBakedQuadColor(FluidStack fluidStack) {
        Triple<Float, Float, Float> colorParts = this.modHelpers.getBaseHelpers().intToRGB(getFluidTintColor(fluidStack));
        return this.modHelpers.getBaseHelpers().RGBAToInt(
                (int) (colorParts.getRight() * 255),
                (int) (colorParts.getMiddle() * 255),
                (int) (colorParts.getLeft() * 255),
                255
        );
    }

    private int getFluidTintColor(FluidStack fluidStack) {
        FluidModel model = getFluidModel(fluidStack.getFluid());
        FluidTintSource tintSource = model.fluidTintSource();
        return tintSource != null ? tintSource.colorAsStack(fluidStack) : -1;
    }
}
