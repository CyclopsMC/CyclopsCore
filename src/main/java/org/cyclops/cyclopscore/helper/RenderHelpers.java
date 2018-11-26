package org.cyclops.cyclopscore.helper;

import com.google.common.base.Function;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleDigging;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.renderer.BlockModelShapes;
import net.minecraft.client.renderer.BlockRendererDispatcher;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.client.renderer.block.model.IBakedModel;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.init.Blocks;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.EnumBlockRenderType;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.AxisAlignedBB;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.tuple.Triple;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Random;

/**
 * A helper for rendering.
 * @author rubensworks
 *
 */
@SideOnly(Side.CLIENT)
public class RenderHelpers {

    private static final Random rand = new Random();
    public static final int SLOT_SIZE = 16;
    
    /**
     * Bind a texture to the rendering engine.
     * @param texture The texture to bind.
     */
    public static void bindTexture(ResourceLocation texture) {
    	Minecraft.getMinecraft().renderEngine.bindTexture(texture);
    }

    /**
     * Draw the given text with the given scale.
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param scale The scale to render the string by.
     * @param color The color to draw
     * @param dropShadow If a shadow should be rendered.
     */
    public static void drawScaledString(FontRenderer fontRenderer, String string, int x, int y, float scale, int color, boolean dropShadow) {
        GlStateManager.pushMatrix();
        GlStateManager.translate(x, y, 0);
        GlStateManager.scale(scale, scale, 1.0f);
        fontRenderer.drawString(string, 0, 0, color, dropShadow);
        GlStateManager.popMatrix();
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
        Minecraft mc = Minecraft.getMinecraft();
        BlockRendererDispatcher blockRendererDispatcher = mc.getBlockRendererDispatcher();
        BlockModelShapes blockModelShapes = blockRendererDispatcher.getBlockModelShapes();
        return blockModelShapes.getModelForState(blockState);
    }

    /**
     * Retrieve the potentially dynamic baked model for a position.
     * This will automatically take into account smart block models.
     * @param world The world.
     * @param pos The position.
     * @return The baked model.
     */

    public static IBakedModel getDynamicBakedModel(World world, BlockPos pos) {
        IBlockState blockState = world.getBlockState(pos);
        IBakedModel bakedModel = getBakedModel(blockState);
        /*if(bakedModel instanceof ISmartBlockModel) {
            IBlockState extendedBlockState = blockState.getBlock().getExtendedState(blockState, world, pos);
            bakedModel = ((ISmartBlockModel) bakedModel).handleBlockState(extendedBlockState);
        }*/
        return bakedModel;
    }

    /**
     * A custom way of spawning block hit effects.
     * @param particleManager The effect renderer.
     * @param world The world.
     * @param blockState The blockstate to render particles for.
     * @param pos The position.
     * @param side The hit side.
     */
    public static void addBlockHitEffects(ParticleManager particleManager, World world, IBlockState blockState, BlockPos pos, EnumFacing side)  {
        if (blockState.getRenderType() != EnumBlockRenderType.INVISIBLE) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            float f = 0.1F;
            AxisAlignedBB bb = blockState.getBoundingBox(world, pos);
            double d0 = (double)i + rand.nextDouble() * (bb.maxX - bb.minX - (double)(f * 2.0F)) + (double)f + bb.minX;
            double d1 = (double)j + rand.nextDouble() * (bb.maxY - bb.minY - (double)(f * 2.0F)) + (double)f + bb.minY;
            double d2 = (double)k + rand.nextDouble() * (bb.maxZ - bb.minZ - (double)(f * 2.0F)) + (double)f + bb.minZ;

            if (side == EnumFacing.DOWN)  d1 = (double)j + bb.minY - (double)f;
            if (side == EnumFacing.UP)    d1 = (double)j + bb.maxY + (double)f;
            if (side == EnumFacing.NORTH) d2 = (double)k + bb.minZ - (double)f;
            if (side == EnumFacing.SOUTH) d2 = (double)k + bb.maxZ + (double)f;
            if (side == EnumFacing.WEST)  d0 = (double)i + bb.minX - (double)f;
            if (side == EnumFacing.EAST)  d0 = (double)i + bb.maxX + (double)f;

            Particle fx = new ParticleDigging.Factory().createParticle(-1, world, d0, d1, d2, 0.0D, 0.0D, 0.0D, Block.getStateId(blockState));
            particleManager.addEffect(fx);
        }
    }

    /**
     * Render the given item in the world.
     * @param itemStack The item stack.
     */
    public static void renderItem(ItemStack itemStack) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        renderItem.renderItem(itemStack, ItemCameraTransforms.TransformType.NONE);
    }

    /**
     * Render the given item in the world.
     * @param itemStack The item stack.
     * @param transformType A transform type.
     */
    public static void renderItem(ItemStack itemStack, ItemCameraTransforms.TransformType transformType) {
        RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
        GlStateManager.translate(8.0F, 8.0F, 0.0F);
        GlStateManager.scale(1.0F, 1.0F, -1.0F);
        GlStateManager.scale(0.5F, 0.5F, 0.5F);
        IBakedModel ibakedmodel = renderItem.getItemModelMesher().getItemModel(itemStack);
        if (ibakedmodel.isGui3d()){
            GlStateManager.scale(40.0F, 40.0F, 40.0F);
            GlStateManager.rotate(210.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.rotate(-135.0F, 0.0F, 1.0F, 0.0F);
            GlStateManager.enableLighting();
        } else {
            GlStateManager.scale(64.0F, 64.0F, 64.0F);
            GlStateManager.rotate(180.0F, 1.0F, 0.0F, 0.0F);
            GlStateManager.disableLighting();
        }
        renderItem.renderItem(itemStack, transformType);
    }

    public static final Function<ResourceLocation, TextureAtlasSprite> TEXTURE_GETTER = new Function<ResourceLocation, TextureAtlasSprite>() {
        public TextureAtlasSprite apply(ResourceLocation location) {
            return Minecraft.getMinecraft().getTextureMapBlocks().getAtlasSprite(location.toString());
        }
    };

    /**
     * Get the default icon from a block.
     * @param block The block.
     * @return The icon.
     */
    public static TextureAtlasSprite getBlockIcon(Block block) {
        return Minecraft.getMinecraft().getBlockRendererDispatcher().getBlockModelShapes().getTexture(block.getDefaultState());
    }

    /**
     * Get the icon of a fluid for a side in a safe way.
     * @param fluid The fluid.
     * @param side The side to get the icon from, UP if null.
     * @return The icon.
     */
    public static TextureAtlasSprite getFluidIcon(Fluid fluid, EnumFacing side) {
        return getFluidIcon(new FluidStack(fluid, 1000), side);
    }

    /**
     * Get the icon of a fluid for a side in a safe way.
     * @param fluid The fluid stack.
     * @param side The side to get the icon from, UP if null.
     * @return The icon.
     */
    public static TextureAtlasSprite getFluidIcon(FluidStack fluid, EnumFacing side) {
        Block defaultBlock = Blocks.WATER;
        Block block = defaultBlock;
        if(fluid.getFluid().getBlock() != null) {
            block = fluid.getFluid().getBlock();
        }

        if(side == null) side = EnumFacing.UP;

        TextureAtlasSprite icon = TEXTURE_GETTER.apply(fluid.getFluid().getFlowing(fluid));
        if(icon == null || (side == EnumFacing.UP || side == EnumFacing.DOWN)) {
            icon = TEXTURE_GETTER.apply(fluid.getFluid().getStill(fluid));
        }
        if(icon == null) {
            icon = getBlockIcon(block);
            if(icon == null) {
                icon = getBlockIcon(defaultBlock);
            }
        }

        return icon;
    }

    /**
     * Prepare a GL context for rendering fluids.
     * @param fluid The fluid stack.
     * @param x X
     * @param y Y
     * @param z Z
     * @param render The actual fluid renderer.
     */
    public static void renderFluidContext(FluidStack fluid, double x, double y, double z, IFluidContextRender render) {
        if(fluid != null && fluid.amount > 0) {
            GlStateManager.pushMatrix();

            // Make sure both sides are rendered
            GlStateManager.enableBlend();
            GlStateManager.disableCull();

            // Correct color & lighting
            GlStateManager.color(1, 1, 1, 1);
            GlStateManager.disableLighting();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);

            // Set to current relative player location
            GlStateManager.translate(x, y, z);

            // Set blockState textures
            Minecraft.getMinecraft().getTextureManager().bindTexture(TextureMap.LOCATION_BLOCKS_TEXTURE);

            render.renderFluid(fluid);

            //GlStateManager.enableCull();
            GlStateManager.enableLighting();
            GlStateManager.disableBlend();
            //GlStateManager.disableDepth();
            GlStateManager.popMatrix();
        }
    }

    /**
     * Prepare a GL context for rendering fluids for tile entities.
     * @param fluid The fluid stack.
     * @param x X
     * @param y Y
     * @param z Z
     * @param tile The tile.
     * @param render The actual fluid renderer.
     */
    public static void renderTileFluidContext(final FluidStack fluid, final double x, final double y, final double z, final TileEntity tile, final IFluidContextRender render) {
        renderFluidContext(fluid, x, y, z, render);
    }

    /**
     * Get the fluid color to use in {@link net.minecraft.client.renderer.BufferBuilder} rendering.
     * @param fluidStack The fluid stack.
     * @return The RGB colors.
     */
    public static Triple<Float, Float, Float> getFluidVertexBufferColor(FluidStack fluidStack) {
        int color = fluidStack.getFluid().getColor(fluidStack);
        return Helpers.intToRGB(color);
    }

    /**
     * Get the fluid color to use in {@link net.minecraft.client.renderer.block.model.BakedQuad}.
     * @param fluidStack The fluid stack.
     * @return The BGR colors.
     */
    public static int getFluidBakedQuadColor(FluidStack fluidStack) {
        Triple<Float, Float, Float> colorParts = Helpers.intToRGB(fluidStack.getFluid().getColor(fluidStack));
        return Helpers.RGBAToInt(
                (int) (colorParts.getRight() * 255),
                (int) (colorParts.getMiddle() * 255),
                (int) (colorParts.getLeft() * 255),
                255
        );
    }

    /**
     * Check if a point is inside a region.
     * @param left Left-top corner x
     * @param top Left-top corner y
     * @param width The width
     * @param height The height
     * @param pointX The point x
     * @param pointY The point y
     * @return If the point is inside the region.
     */
    public static boolean isPointInRegion(int left, int top, int width, int height, int pointX, int pointY) {
        return pointX >= left && pointX < left + width && pointY >= top && pointY < top + height;
    }

    /**
     * Check if a point is inside a region.
     * @param region The region.
     * @param point The point.
     * @return If the point is inside the region.
     */
    public static boolean isPointInRegion(Rectangle region, Point point) {
        return isPointInRegion(region.x, region.y, region.width, region.height, point.x, point.y);
    }

    /**
     * Check if a point is inside a button's region.
     * @param button The button.
     * @param pointX The point x
     * @param pointY The point y
     * @return If the point is inside the button's region.
     */
    public static boolean isPointInButton(GuiButton button, int pointX, int pointY) {
        return isPointInRegion(button.x, button.y, button.width, button.height, pointX, pointY);
    }

    /**
     * Runnable for {@link RenderHelpers#renderFluidContext(FluidStack, double, double, double, IFluidContextRender)}.
     * @author rubensworks
     */
    public static interface IFluidContextRender {

        /**
         * Render the fluid.
         * @param fluid The fluid stack.
         */
        public void renderFluid(FluidStack fluid);

    }

}
