package org.cyclops.cyclopscore.helper;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.minecraftforge.client.extensions.common.IClientFluidTypeExtensions;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class GuiHelpersForge extends GuiHelpersCommon implements IGuiHelpersForge {

    public GuiHelpersForge(IModHelpersForge modHelpers) {
        super(modHelpers);
    }

    @Override
    public void renderFluidTank(GuiGraphicsExtractor gui, @Nullable FluidStack fluidStack, int capacity,
                                int x, int y, int width, int height) {
        if (fluidStack != null && !fluidStack.isEmpty() && capacity > 0) {
            gui.pose().pushMatrix();

            int level = (int) (height * (((double) fluidStack.getAmount()) / capacity));
            TextureAtlasSprite icon = ((IModHelpersForge) modHelpers).getRenderHelpers().getFluidIcon(fluidStack, Direction.UP);
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
                Triple<Float, Float, Float> colorParts = modHelpers.getBaseHelpers().intToRGB(renderProperties.getTintColor(fluidStack));

                gui.blitSprite(RenderPipelines.GUI_OPAQUE_TEXTURED_BACKGROUND, icon, x, y - textureHeight - verticalOffset + height, width, textureHeight, ARGB.colorFromFloat(1, colorParts.getLeft(), colorParts.getMiddle(), colorParts.getRight()));

                verticalOffset = verticalOffset + 16;
            }

            gui.pose().popMatrix();
        }
    }

    @Override
    public void renderFluidSlot(GuiGraphicsExtractor gui, @Nullable FluidStack fluidStack, int x, int y) {
        if (fluidStack != null) {
            this.renderFluidTank(gui, fluidStack, fluidStack.getAmount(), x, y, getSlotSizeInner(), getSlotSizeInner());
        }
    }

    @Override
    public void renderOverlayedFluidTank(GuiGraphicsExtractor gui, @Nullable FluidStack fluidStack, int capacity,
                                         int x, int y, int width, int height,
                                         Identifier textureOverlay, int overlayTextureX, int overlayTextureY) {
        renderFluidTank(gui, fluidStack, capacity, x, y, width, height);
        if (fluidStack != null && capacity > 0) {
            gui.blit(RenderPipelines.GUI_OPAQUE_TEXTURED_BACKGROUND, textureOverlay, x, y, overlayTextureX, overlayTextureY, width, height, 256, 256);
        }
    }

}
