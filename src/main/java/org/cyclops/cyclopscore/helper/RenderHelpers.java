package org.cyclops.cyclopscore.helper;

import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.particle.EffectRenderer;
import net.minecraft.client.particle.EntityDiggingFX;
import net.minecraft.client.particle.EntityFX;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockPos;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import java.util.Random;

/**
 * A helper for rendering.
 * @author rubensworks
 *
 */
@SideOnly(Side.CLIENT)
public class RenderHelpers {

    private static final Random rand = new Random();
    
    /**
     * Bind a texture to the rendering engine.
     * @param texture The texture to bind.
     */
    public static void bindTexture(ResourceLocation texture) {
    	Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    }

    /**
     * Draw the given text and scale it to the max width.
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param maxWidth The maximum width to scale to
     * @param color The color to draw
     */
    public static void drawScaledCenteredString(FontRenderer fontRenderer, String string, int x, int y, int maxWidth, int color) {
        drawScaledCenteredString(fontRenderer, string, x, y, maxWidth, 1.0F, maxWidth, color);
    }

    /**
     * Draw the given text and scale it to the max width.
     * The given string may already be scaled and its width must be passed in that case.
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param width The scaled width
     * @param originalScale The original scale
     * @param maxWidth The maximum width to scale to
     * @param color The color to draw
     */
    public static void drawScaledCenteredString(FontRenderer fontRenderer, String string, int x, int y, int width, float originalScale, int maxWidth, int color) {
        float originalWidth = fontRenderer.getStringWidth(string) * originalScale;
        float scale = Math.min(originalScale, maxWidth / originalWidth * originalScale);
        drawScaledCenteredString(fontRenderer, string, x, y, width, scale, color);
    }

    /**
     * Draw the given text with the given width and desired scale.
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param width The scaled width
     * @param scale The desired scale
     * @param color The color to draw
     */
    public static void drawScaledCenteredString(FontRenderer fontRenderer, String string, int x, int y, int width, float scale, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0f);
        int titleLength = fontRenderer.getStringWidth(string);
        int titleHeight = fontRenderer.FONT_HEIGHT;
        fontRenderer.drawString(string, Math.round((x + width / 2) / scale - titleLength / 2), Math.round(y / scale - titleHeight / 2), color);
        GlStateManager.popMatrix();
    }

    /**
     * Retrieve the baked model from a given block state.
     * @param blockState The block state.
     * @return The corresponding baked model.
     */
    public static IBakedModel getBakedModel(IBlockState blockState) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getModelManager().getBlockModelShapes().getModelForState(blockState);
    }

    /**
     * A custom way of spawning block hit effects.
     * @param effectRenderer The effect renderer.
     * @param world The world.
     * @param blockState The blockstate to render particles for.
     * @param pos The position.
     * @param side The hit side.
     */
    public static void addBlockHitEffects(EffectRenderer effectRenderer, World world, IBlockState blockState, BlockPos pos, EnumFacing side)  {
        Block block = blockState.getBlock();
        if (block.getRenderType() != -1) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            float f = 0.1F;
            double d0 = (double)i + rand.nextDouble() * (block.getBlockBoundsMaxX() - block.getBlockBoundsMinX() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinX();
            double d1 = (double)j + rand.nextDouble() * (block.getBlockBoundsMaxY() - block.getBlockBoundsMinY() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinY();
            double d2 = (double)k + rand.nextDouble() * (block.getBlockBoundsMaxZ() - block.getBlockBoundsMinZ() - (double)(f * 2.0F)) + (double)f + block.getBlockBoundsMinZ();

            if (side == EnumFacing.DOWN)  d1 = (double)j + block.getBlockBoundsMinY() - (double)f;
            if (side == EnumFacing.UP)    d1 = (double)j + block.getBlockBoundsMaxY() + (double)f;
            if (side == EnumFacing.NORTH) d2 = (double)k + block.getBlockBoundsMinZ() - (double)f;
            if (side == EnumFacing.SOUTH) d2 = (double)k + block.getBlockBoundsMaxZ() + (double)f;
            if (side == EnumFacing.WEST)  d0 = (double)i + block.getBlockBoundsMinX() - (double)f;
            if (side == EnumFacing.EAST)  d0 = (double)i + block.getBlockBoundsMaxX() + (double)f;

            EntityFX fx = new EntityDiggingFX.Factory().getEntityFX(-1, world, d0, d1, d2, 0.0D, 0.0D, 0.0D, Block.getStateId(blockState));
            effectRenderer.addEffect(fx);
        }
    }

    /**
     * Render the given item in the world.
     * @param itemStack The item stack.
     */
    public static void renderItem(ItemStack itemStack) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.renderItem(itemStack, renderItem.getItemModelMesher().getItemModel(itemStack));
    }

}
