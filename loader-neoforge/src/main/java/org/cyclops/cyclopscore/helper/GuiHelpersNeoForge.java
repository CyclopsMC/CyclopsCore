package org.cyclops.cyclopscore.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.renderer.block.FluidModel;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.resources.Identifier;
import net.minecraft.util.ARGB;
import net.neoforged.neoforge.client.fluid.FluidTintSource;
import net.neoforged.neoforge.fluids.FluidStack;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class GuiHelpersNeoForge extends GuiHelpersCommon implements IGuiHelpersNeoForge {

    public GuiHelpersNeoForge(IModHelpers modHelpers) {
        super(modHelpers);
    }

    @Override
    public void renderFluidTank(GuiGraphicsExtractor gui, @Nullable FluidStack fluidStack, int capacity,
                                int x, int y, int width, int height) {
        if (fluidStack != null && !fluidStack.isEmpty() && capacity > 0) {
            gui.pose().pushMatrix();

            int level = (int) (height * (((double) fluidStack.getAmount()) / capacity));
            FluidModel fluidModel = Minecraft.getInstance().getModelManager().getFluidStateModelSet().get(fluidStack.getFluid().defaultFluidState());
            TextureAtlasSprite icon = fluidModel.stillMaterial().sprite();
            FluidTintSource tintSource = fluidModel.fluidTintSource();
            int verticalOffset = 0;
            while (level > 0) {
                int textureHeight;
                if (level > 16) {
                    textureHeight = 16;
                    level -= 16;
                } else {
                    textureHeight = level;
                    level = 0;
                }

                int color;
                if (tintSource != null) {
                    color = tintSource.colorAsStack(fluidStack);
                } else {
                    color = ARGB.colorFromFloat(1, 1F, 1F, 1F);
                }

                gui.blitSprite(RenderPipelines.GUI_TEXTURED, icon, x, y - textureHeight - verticalOffset + height, width, textureHeight, color);

                verticalOffset = verticalOffset + 16;
            }

            gui.pose().popMatrix();
        }
    }

    @Override
    public void renderFluidSlot(GuiGraphicsExtractor gui, @Nullable FluidStack fluidStack, int x, int y) {
        if (fluidStack != null) {
            IModHelpersNeoForge.get().getGuiHelpers().renderFluidTank(gui, fluidStack, fluidStack.getAmount(), x, y, getSlotSizeInner(), getSlotSizeInner());
        }
    }

    @Override
    public void renderOverlayedFluidTank(GuiGraphicsExtractor gui, @Nullable FluidStack fluidStack, int capacity,
                                         int x, int y, int width, int height,
                                         Identifier textureOverlay, int overlayTextureX, int overlayTextureY) {
        renderFluidTank(gui, fluidStack, capacity, x, y, width, height);
        if (fluidStack != null && capacity > 0) {
            gui.blit(RenderPipelines.GUI_TEXTURED, textureOverlay, x, y, overlayTextureX, overlayTextureY, width, height, 256, 256);
        }
    }

}
