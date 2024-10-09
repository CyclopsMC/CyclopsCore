package org.cyclops.cyclopscore.helper;

import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.material.Fluids;
import org.apache.commons.lang3.tuple.Triple;
import org.lwjgl.opengl.GL11;

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
            gui.pose().pushPose();
            GlStateManager._enableBlend();
            GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Lighting.setupFor3DItems();
            GL11.glEnable(GL11.GL_DEPTH_TEST);

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

                Lighting.setupForFlatItems();
                RenderSystem.setShaderColor(colorParts.getLeft(), colorParts.getMiddle(), colorParts.getRight(), 1);
                gui.blit(x, y - textureHeight - verticalOffset + height, 0, width, textureHeight, icon);
                Lighting.setupFor3DItems();
                RenderSystem.setShaderColor(1, 1, 1, 1);

                verticalOffset = verticalOffset + 16;
            }

            TextureManager textureManager = Minecraft.getInstance().getTextureManager();
            textureManager.bindForSetup(InventoryMenu.BLOCK_ATLAS);
//            textureManager.getTexture(InventoryMenu.BLOCK_ATLAS).restoreLastBlurMipmap();

            Lighting.setupForFlatItems();
            gui.pose().popPose();
            GL11.glDisable(GL11.GL_DEPTH_TEST);
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
            GlStateManager._enableBlend();
            gui.blit(textureOverlay, x, y, overlayTextureX, overlayTextureY, width, height);
        }
    }

}
