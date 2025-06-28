package org.cyclops.cyclopscore.helper;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.tuple.Triple;

/**
 * @author rubensworks
 */
public class GuiHelpersFabric extends GuiHelpersCommon implements IGuiHelpersFabric {

    public GuiHelpersFabric(IModHelpersFabric modHelpers) {
        super(modHelpers);
    }

    @Override
    public void renderFluidTank(GuiGraphics gui, FluidVariant fluidStack, long amount, long capacity,
                                int x, int y, int width, int height) {
        if (fluidStack != null && !fluidStack.isBlank() && amount > 0 && capacity > 0) {
            gui.pose().pushMatrix();

            int level = (int) (height * (((double) amount) / capacity));
            IRenderHelpersFabric renderHelpers = ((IModHelpersFabric) modHelpers).getRenderHelpers();
            TextureAtlasSprite icon = renderHelpers.getFluidIcon(fluidStack, Direction.UP);
            int verticalOffset = 0;
            while(level > 0) {
                int textureHeight;
                if(level > 16) {
                    textureHeight = 16;
                    level -= 16;
                } else {
                    textureHeight = level;
                    level = 0;
                }

                // Fluids can have a custom overlay color, use this to render.
                Triple<Float, Float, Float> colorParts = renderHelpers.getFluidVertexBufferColor(fluidStack);
                // Override water color, otherwise it's gray, since it depends on world biome.
                if (fluidStack.getFluid() == Fluids.WATER || fluidStack.getFluid() == Fluids.FLOWING_WATER) {
                    colorParts = Triple.of(0F, 0.335F, 1F);
                }

                gui.blitSprite(RenderPipelines.GUI_OPAQUE_TEXTURED_BACKGROUND, icon, x, y - textureHeight - verticalOffset + height, width, textureHeight, ARGB.colorFromFloat(1, colorParts.getLeft(), colorParts.getMiddle(), colorParts.getRight()));

                verticalOffset = verticalOffset + 16;
            }

            gui.pose().popMatrix();
        }
    }

    @Override
    public void renderFluidSlot(GuiGraphics gui, FluidVariant fluidStack, long amount, int x, int y) {
        if (fluidStack != null) {
            this.renderFluidTank(gui, fluidStack, amount, amount, x, y, getSlotSizeInner(), getSlotSizeInner());
        }
    }

    @Override
    public void renderOverlayedFluidTank(GuiGraphics gui, FluidVariant fluidStack, long amount, long capacity,
                                         int x, int y, int width, int height,
                                         ResourceLocation textureOverlay, int overlayTextureX, int overlayTextureY) {
        renderFluidTank(gui, fluidStack, amount, capacity, x, y, width, height);
        if (fluidStack != null && capacity > 0) {
            gui.blit(RenderPipelines.GUI_OPAQUE_TEXTURED_BACKGROUND, textureOverlay, x, y, overlayTextureX, overlayTextureY, width, height, 256, 256);
        }
    }

}
