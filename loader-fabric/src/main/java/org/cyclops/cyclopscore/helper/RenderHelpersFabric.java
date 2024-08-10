package org.cyclops.cyclopscore.helper;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandler;
import net.fabricmc.fabric.api.client.render.fluid.v1.FluidRenderHandlerRegistry;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.tuple.Triple;
import org.lwjgl.opengl.GL11;

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

        FluidRenderHandler handler = FluidRenderHandlerRegistry.INSTANCE.get(fluidVariant.getFluid());
        if (handler == null) {
            handler = FluidRenderHandlerRegistry.INSTANCE.get(Fluids.WATER);
        }
        TextureAtlasSprite[] textures = handler.getFluidSprites(null, null, fluidVariant.getFluid().defaultFluidState());
        if (side == Direction.UP || side == Direction.DOWN) {
            return textures[0];
        }
        return textures[1];
    }

    @Override
    public void renderFluidContext(FluidVariant fluidVariant, PoseStack matrixStack, IFluidContextRender render) {
        if (!fluidVariant.isBlank()) {
            matrixStack.pushPose();

            // Make sure both sides are rendered
            RenderSystem.enableBlend();
            RenderSystem.disableCull();

            // Correct color & lighting
            RenderSystem.enableBlend();
            RenderSystem.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            // Set blockState textures
            Minecraft.getInstance().getTextureManager().bindForSetup(TextureAtlas.LOCATION_BLOCKS);

            render.render();

            RenderSystem.disableBlend();
            matrixStack.popPose();
        }
    }

    @Override
    public Triple<Float, Float, Float> getFluidVertexBufferColor(FluidVariant fluidVariant) {
        FluidRenderHandler handler = FluidRenderHandlerRegistry.INSTANCE.get(fluidVariant.getFluid());
        return this.modHelpers.getBaseHelpers().intToRGB(handler != null ? handler.getFluidColor(null, null, fluidVariant.getFluid().defaultFluidState()) : -1);
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
