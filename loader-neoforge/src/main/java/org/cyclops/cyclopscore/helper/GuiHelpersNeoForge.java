package org.cyclops.cyclopscore.helper;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;
import org.lwjgl.opengl.GL11;

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
            gui.pose().pushPose();
            GlStateManager._enableBlend();
            GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Lighting.setupFor3DItems();
            GL11.glEnable(GL11.GL_DEPTH_TEST);

            int level = (int) (height * (((double) fluidStack.getAmount()) / capacity));
            TextureAtlasSprite icon = RenderHelpers.getFluidIcon(fluidStack, Direction.UP);
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
                Triple<Float, Float, Float> colorParts = Helpers.intToRGB(renderProperties.getTintColor(fluidStack));
                // Override water color, otherwise it's gray, since it depends on world biome.
                if (fluidStack.getFluid() == Fluids.WATER || fluidStack.getFluid() == Fluids.FLOWING_WATER) {
                    colorParts = Triple.of(0F, 0.335F, 1F);
                }

                Lighting.setupForFlatItems();
                RenderSystem.setShaderColor(colorParts.getLeft(), colorParts.getMiddle(), colorParts.getRight(), 1);
                gui.blit(x, y - textureHeight - verticalOffset + height, 0, width, textureHeight, icon);
                Lighting.setupFor3DItems();
                RenderSystem.setShaderColor(1, 1, 1, 1);

                verticalOffset = verticalOffset + 16;
            }

            TextureManager textureManager = Minecraft.getInstance().getTextureManager();
            textureManager.bindForSetup(InventoryMenu.BLOCK_ATLAS);
            textureManager.getTexture(InventoryMenu.BLOCK_ATLAS).restoreLastBlurMipmap();

            Lighting.setupForFlatItems();
            gui.pose().popPose();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
        }
    }

    @Override
    public void renderFluidSlot(GuiGraphics gui, @Nullable FluidStack fluidStack, int x, int y) {
        if (fluidStack != null) {
            GuiHelpers.renderFluidTank(gui, fluidStack, fluidStack.getAmount(), x, y, getSlotSizeInner(), getSlotSizeInner());
        }
    }

    @Override
    public void renderOverlayedFluidTank(GuiGraphics gui, @Nullable FluidStack fluidStack, int capacity,
                                         int x, int y, int width, int height,
                                         ResourceLocation textureOverlay, int overlayTextureX, int overlayTextureY) {
        renderFluidTank(gui, fluidStack, capacity, x, y, width, height);
        if (fluidStack != null && capacity > 0) {
            GlStateManager._enableBlend();
            gui.blit(textureOverlay, x, y, overlayTextureX, overlayTextureY, width, height);
        }
    }

}
