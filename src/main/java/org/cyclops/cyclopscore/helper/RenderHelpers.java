package org.cyclops.cyclopscore.helper;

import com.google.common.base.Function;
import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import com.mojang.math.Matrix4f;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.TerrainParticle;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.block.BlockModelShaper;
import net.minecraft.client.renderer.block.BlockRenderDispatcher;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.inventory.InventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Triple;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.Random;

/**
 * A helper for rendering.
 * @author rubensworks
 *
 */
@OnlyIn(Dist.CLIENT)
public class RenderHelpers {

    private static final Random rand = new Random();
    public static final int SLOT_SIZE = 16;

    /**
     * Bind a texture to the rendering engine.
     * @param texture The texture to bind.
     */
    public static void bindTexture(ResourceLocation texture) {
    	Minecraft.getInstance().getTextureManager().bindForSetup(texture);
    }

    /**
     * Add a particle to the world.
     * @param particle A particle.
     */
    public static void emitParticle(Particle particle) {
        Minecraft.getInstance().particleEngine.add(particle);
    }

    /**
     * Draw the given text with the given scale.
     * @param matrixStack The matrix stack
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param scale The scale to render the string by.
     * @param color The color to draw
     */
    public static void drawScaledString(PoseStack matrixStack, Font fontRenderer, String string, int x, int y, float scale, int color) {
        matrixStack.pushPose();
        matrixStack.translate(x, y, 0);
        matrixStack.scale(scale, scale, 1.0f);
        fontRenderer.draw(matrixStack, string, 0, 0, color);
        matrixStack.popPose();
    }

    /**
     * Draw the given text with the given scale with a shadow.
     * @param matrixStack The matrix stack
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param scale The scale to render the string by.
     * @param color The color to draw
     */
    public static void drawScaledStringWithShadow(PoseStack matrixStack, Font fontRenderer, String string, int x, int y, float scale, int color) {
        matrixStack.pushPose();
        matrixStack.translate(x, y, 0);
        matrixStack.scale(scale, scale, 1.0f);
        fontRenderer.drawShadow(matrixStack, string, 0, 0, color);
        matrixStack.popPose();
    }

    /**
     * Draw the given text and scale it to the max width.
     * @param matrixStack The matrix stack
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param maxWidth The maximum width to scale to
     * @param color The color to draw
     */
    public static void drawScaledCenteredString(PoseStack matrixStack, Font fontRenderer, String string, int x, int y, int maxWidth, int color) {
        drawScaledCenteredString(matrixStack, fontRenderer, string, x, y, maxWidth, 1.0F, maxWidth, color);
    }

    /**
     * Draw the given text and scale it to the max width.
     * The given string may already be scaled and its width must be passed in that case.
     * @param matrixStack The matrix stack
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param width The scaled width
     * @param originalScale The original scale
     * @param maxWidth The maximum width to scale to
     * @param color The color to draw
     */
    public static void drawScaledCenteredString(PoseStack matrixStack, Font fontRenderer, String string, int x, int y, int width, float originalScale, int maxWidth, int color) {
        float originalWidth = fontRenderer.width(string) * originalScale;
        float scale = Math.min(originalScale, maxWidth / originalWidth * originalScale);
        drawScaledCenteredString(matrixStack, fontRenderer, string, x, y, width, scale, color);
    }

    /**
     * Draw the given text with the given width and desired scale.
     * @param matrixStack The matrix stack
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param width The scaled width
     * @param scale The desired scale
     * @param color The color to draw
     */
    public static void drawScaledCenteredString(PoseStack matrixStack, Font fontRenderer, String string, int x, int y, int width, float scale, int color) {
        matrixStack.pushPose();
        matrixStack.scale(scale, scale, 1.0f);
        int titleLength = fontRenderer.width(string);
        int titleHeight = fontRenderer.lineHeight;
        fontRenderer.draw(matrixStack, string, Math.round((x + width / 2) / scale - titleLength / 2), Math.round(y / scale - titleHeight / 2), color);
        matrixStack.popPose();
    }

    /**
     * Retrieve the baked model from a given block state.
     * @param blockState The block state.
     * @return The corresponding baked model.
     */
    public static BakedModel getBakedModel(BlockState blockState) {
        Minecraft mc = Minecraft.getInstance();
        BlockRenderDispatcher blockRendererDispatcher = mc.getBlockRenderer();
        BlockModelShaper blockModelShapes = blockRendererDispatcher.getBlockModelShaper();
        return blockModelShapes.getBlockModel(blockState);
    }

    /**
     * Retrieve the potentially dynamic baked model for a position.
     * This will automatically take into account smart block models.
     * @param world The world.
     * @param pos The position.
     * @return The baked model.
     */

    public static BakedModel getDynamicBakedModel(Level world, BlockPos pos) {
        return getBakedModel(world.getBlockState(pos));
    }

    /**
     * A custom way of spawning block hit effects.
     * @param particleManager The effect renderer.
     * @param world The world.
     * @param blockState The blockstate to render particles for.
     * @param pos The position.
     * @param side The hit side.
     */
    public static void addBlockHitEffects(ParticleEngine particleManager, ClientLevel world, BlockState blockState, BlockPos pos, Direction side)  {
        if (blockState.getRenderShape() != RenderShape.INVISIBLE) {
            int i = pos.getX();
            int j = pos.getY();
            int k = pos.getZ();
            float f = 0.1F;
            AABB bb = blockState.getShape(world, pos).bounds();
            double d0 = (double)i + rand.nextDouble() * (bb.maxX - bb.minX - (double)(f * 2.0F)) + (double)f + bb.minX;
            double d1 = (double)j + rand.nextDouble() * (bb.maxY - bb.minY - (double)(f * 2.0F)) + (double)f + bb.minY;
            double d2 = (double)k + rand.nextDouble() * (bb.maxZ - bb.minZ - (double)(f * 2.0F)) + (double)f + bb.minZ;

            if (side == Direction.DOWN)  d1 = (double)j + bb.minY - (double)f;
            if (side == Direction.UP)    d1 = (double)j + bb.maxY + (double)f;
            if (side == Direction.NORTH) d2 = (double)k + bb.minZ - (double)f;
            if (side == Direction.SOUTH) d2 = (double)k + bb.maxZ + (double)f;
            if (side == Direction.WEST)  d0 = (double)i + bb.minX - (double)f;
            if (side == Direction.EAST)  d0 = (double)i + bb.maxX + (double)f;

            Particle fx = new TerrainParticle.Provider().createParticle(new BlockParticleOption(ParticleTypes.BLOCK, blockState), world, d0, d1, d2, 0.0D, 0.0D, 0.0D);
            particleManager.add(fx);
        }
    }

    public static final Function<ResourceLocation, TextureAtlasSprite> TEXTURE_GETTER =
            location -> Minecraft.getInstance().getTextureAtlas(InventoryMenu.BLOCK_ATLAS).apply(location);

    /**
     * Get the default icon from a block.
     * @param block The block.
     * @return The icon.
     */
    public static TextureAtlasSprite getBlockIcon(Block block) {
        return Minecraft.getInstance().getBlockRenderer().getBlockModelShaper().getParticleIcon(block.defaultBlockState());
    }

    /**
     * Get the icon of a fluid for a side in a safe way.
     * @param fluid The fluid.
     * @param side The side to get the icon from, UP if null.
     * @return The icon.
     */
    public static TextureAtlasSprite getFluidIcon(Fluid fluid, Direction side) {
        return getFluidIcon(new FluidStack(fluid, 1000), side);
    }

    /**
     * Get the icon of a fluid for a side in a safe way.
     * @param fluid The fluid stack.
     * @param side The side to get the icon from, UP if null.
     * @return The icon.
     */
    public static TextureAtlasSprite getFluidIcon(FluidStack fluid, Direction side) {
        if(side == null) side = Direction.UP;

        TextureAtlasSprite icon = TEXTURE_GETTER.apply(fluid.getFluid().getAttributes().getFlowingTexture(fluid));
        if(icon == null || (side == Direction.UP || side == Direction.DOWN)) {
            icon = TEXTURE_GETTER.apply(fluid.getFluid().getAttributes().getStillTexture(fluid));
        }

        return icon;
    }

    /**
     * Prepare a render system context for rendering fluids.
     * @param fluid The fluid stack.
     * @param matrixStack The matrix stack.
     * @param render The actual fluid renderer.
     */
    public static void renderFluidContext(FluidStack fluid, PoseStack matrixStack, IFluidContextRender render) {
        if(fluid != null && fluid.getAmount() > 0) {
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

    /**
     * Get the fluid color to use in buffer rendering.
     * @param fluidStack The fluid stack.
     * @return The RGB colors.
     */
    public static Triple<Float, Float, Float> getFluidVertexBufferColor(FluidStack fluidStack) {
        int color = fluidStack.getFluid().getAttributes().getColor(fluidStack);
        return Helpers.intToRGB(color);
    }

    /**
     * Get the fluid color to use in a baked quad.
     * @param fluidStack The fluid stack.
     * @return The BGR colors.
     */
    public static int getFluidBakedQuadColor(FluidStack fluidStack) {
        Triple<Float, Float, Float> colorParts = Helpers.intToRGB(fluidStack.getFluid().getAttributes().getColor(fluidStack));
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
    public static boolean isPointInRegion(int left, int top, int width, int height, double pointX, double pointY) {
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
    public static boolean isPointInButton(Button button, int pointX, int pointY) {
        return isPointInRegion(button.x, button.y, button.getWidth(), button.getHeight(), pointX, pointY);
    }

    public static void blitColored(PoseStack poseStack, int x, int y, int z, int width, int height, TextureAtlasSprite sprite, float r, float g, float b, float a) {
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        Matrix4f matrix4f = poseStack.last().pose();
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferbuilder.vertex(matrix4f, x, y, z).color(r, g, b, a).uv(sprite.getU0(), sprite.getV1()).endVertex();
        bufferbuilder.vertex(matrix4f, x + width, y, z).color(r, g, b, a).uv(sprite.getU1(), sprite.getV1()).endVertex();
        bufferbuilder.vertex(matrix4f, x + width, y + height, z).color(r, g, b, a).uv(sprite.getU1(), sprite.getV0()).endVertex();
        bufferbuilder.vertex(matrix4f, x, y + height, z).color(r, g, b, a).uv(sprite.getU0(), sprite.getV0()).endVertex();
        bufferbuilder.end();
    }

    public static void blitColored(PoseStack poseStack, int x, int y, int z, float u, float v, int width, int height, float r, float g, float b, float a) {
        blitColored(poseStack, x, y, z, width, height, u, u + width, v, v + height, r, g, b, a);
    }

    public static void blitColored(PoseStack poseStack, int x, int y, int z, int width, int height, float u0, float u1, float v0, float v1, float r, float g, float b, float a) {
        Matrix4f matrix4f = poseStack.last().pose();
        RenderSystem.setShader(GameRenderer::getPositionTexColorShader);
        BufferBuilder bufferbuilder = Tesselator.getInstance().getBuilder();
        bufferbuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR_TEX);
        bufferbuilder.vertex(matrix4f, (float)x, (float)y + height, (float)z).color(r, g, b, a).uv(u0, v1).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x + width, (float)y + height, (float)z).color(r, g, b, a).uv(u1, v1).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x + width, (float)y, (float)z).color(r, g, b, a).uv(u1, v0).endVertex();
        bufferbuilder.vertex(matrix4f, (float)x, (float)y, (float)z).color(r, g, b, a).uv(u0, v0).endVertex();
        bufferbuilder.end();
        BufferUploader.end(bufferbuilder);
    }

    /**
     * Runnable for {@link RenderHelpers#renderFluidContext(FluidStack, PoseStack, IFluidContextRender)}}}.
     * @author rubensworks
     */
    public static interface IFluidContextRender {

        /**
         * Render the fluid.
         */
        public void render();

    }

}
