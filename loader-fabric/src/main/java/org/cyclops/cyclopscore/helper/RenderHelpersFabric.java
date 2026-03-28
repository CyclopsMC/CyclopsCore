package org.cyclops.cyclopscore.helper;

import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.block.FluidStateModelSet;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import org.apache.commons.lang3.tuple.Triple;

/**
 * @author rubensworks
 */
public class RenderHelpersFabric extends RenderHelpersCommon implements IRenderHelpersFabric {

    private final IModHelpers modHelpers;

    public RenderHelpersFabric(IModHelpers modHelpers) {
        this.modHelpers = modHelpers;
    }

    @Override
    public TextureAtlasSprite getFluidIcon(Fluid fluid, Direction side) {
        return getFluidIcon(FluidVariant.of(fluid), side);
    }

    @Override
    public TextureAtlasSprite getFluidIcon(FluidVariant fluidVariant, Direction side) {
        if (side == null) side = Direction.UP;

        FluidStateModelSet fluidModels = Minecraft.getInstance().getModelManager().getFluidStateModelSet();
        FluidModel model = fluidModels.get(fluidVariant.getFluid().defaultFluidState());
        if (side == Direction.UP || side == Direction.DOWN) {
            return model.stillMaterial().sprite();
        }
        return model.flowingMaterial().sprite();
    }

    @Override
    public void renderFluidContext(FluidVariant fluidVariant, PoseStack matrixStack, IFluidContextRender render) {
        if (!fluidVariant.isBlank()) {
            matrixStack.pushPose();
            render.render();
            matrixStack.popPose();
        }
    }

    @Override
    public Triple<Float, Float, Float> getFluidVertexBufferColor(FluidVariant fluidVariant) {
        FluidStateModelSet fluidModels = Minecraft.getInstance().getModelManager().getFluidStateModelSet();
        FluidModel model = fluidModels.get(fluidVariant.getFluid().defaultFluidState());
        int color = model.tintSource().color(fluidVariant.getFluid().defaultFluidState().createLegacyBlock());
        return this.modHelpers.getBaseHelpers().intToRGB(color);
    }

    @Override
    public int getFluidBakedQuadColor(FluidVariant fluidVariant) {
        Triple<Float, Float, Float> colorParts = getFluidVertexBufferColor(fluidVariant);
        return this.modHelpers.getBaseHelpers().RGBAToInt(
                (int) (colorParts.getRight() * 255),
                (int) (colorParts.getMiddle() * 255),
                (int) (colorParts.getLeft() * 255),
                255
        );
    }
}
