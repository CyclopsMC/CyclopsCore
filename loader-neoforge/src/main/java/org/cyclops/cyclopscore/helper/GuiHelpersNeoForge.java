package org.cyclops.cyclopscore.helper;

import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.ARGB;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class GuiHelpersNeoForge extends GuiHelpersCommon implements IGuiHelpersNeoForge {

    public GuiHelpersNeoForge(IModHelpers modHelpers) {
        super(modHelpers);
    }

    @Override
    public void renderFluidTank(GuiGraphics gui, @Nullable FluidStack fluidStack, int capacity,
                                int x, int y, int width, int height) {
        if (fluidStack != null && !fluidStack.isEmpty() && capacity > 0) {
            gui.pose().pushMatrix();

            int level = (int) (height * (((double) fluidStack.getAmount()) / capacity));
            TextureAtlasSprite icon = IModHelpersNeoForge.get().getRenderHelpers().getFluidIcon(fluidStack, Direction.UP);
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
                IClientFluidTypeExtensions renderProperties = IClientFluidTypeExtensions.of(fluidStack.getFluid().getFluidType());
                Triple<Float, Float, Float> colorParts = IModHelpers.get().getBaseHelpers().intToRGB(renderProperties.getTintColor(fluidStack));
                // Override water color, otherwise it's gray, since it depends on world biome.
                if (fluidStack.getFluid() == Fluids.WATER || fluidStack.getFluid() == Fluids.FLOWING_WATER) {
                    colorParts = Triple.of(0F, 0.335F, 1F);
                }

                gui.blitSprite(RenderPipelines.GUI_TEXTURED, icon, x, y - textureHeight - verticalOffset + height, width, textureHeight, ARGB.colorFromFloat(1, colorParts.getLeft(), colorParts.getMiddle(), colorParts.getRight()));

                verticalOffset = verticalOffset + 16;
            }

            gui.pose().popMatrix();
        }
    }

    @Override
    public void renderFluidSlot(GuiGraphics gui, @Nullable FluidStack fluidStack, int x, int y) {
        if (fluidStack != null) {
            IModHelpersNeoForge.get().getGuiHelpers().renderFluidTank(gui, fluidStack, fluidStack.getAmount(), x, y, getSlotSizeInner(), getSlotSizeInner());
        }
    }

    @Override
    public void renderOverlayedFluidTank(GuiGraphics gui, @Nullable FluidStack fluidStack, int capacity,
                                         int x, int y, int width, int height,
                                         ResourceLocation textureOverlay, int overlayTextureX, int overlayTextureY) {
        renderFluidTank(gui, fluidStack, capacity, x, y, width, height);
        if (fluidStack != null && capacity > 0) {
            gui.blit(RenderPipelines.GUI_TEXTURED, textureOverlay, x, y, overlayTextureX, overlayTextureY, width, height, 256, 256);
        }
    }

}
