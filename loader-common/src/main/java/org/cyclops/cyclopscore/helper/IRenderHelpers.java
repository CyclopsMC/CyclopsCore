package org.cyclops.cyclopscore.helper;

import com.google.common.base.Function;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import java.awt.*;

/**
 * @author rubensworks
 */
public interface IRenderHelpers {

    /**
     * Bind a texture to the rendering engine.
     * @param texture The texture to bind.
     */
    public void bindTexture(ResourceLocation texture);

    /**
     * Add a particle to the world.
     * @param particle A particle.
     */
    public void emitParticle(Particle particle);

    /**
     * Draw the given text with the given scale with a shadow.
     * @param guiGraphics The gui graphics
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param scale The scale to render the string by.
     * @param color The color to draw
     * @param shadow If a shadow should be drawn
     * @param displayMode The display mode
     */
    public void drawScaledString(GuiGraphics guiGraphics, Font fontRenderer, String string, int x, int y, float scale, int color, boolean shadow, Font.DisplayMode displayMode);

    /**
     * Draw the given text and scale it to the max width.
     * @param guiGraphics The gui graphics
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param maxWidth The maximum width to scale to
     * @param color The color to draw
     * @param shadow If a shadow should be drawn
     * @param displayMode The display mode
     */
    public void drawScaledCenteredString(GuiGraphics guiGraphics, Font fontRenderer, String string, int x, int y, int maxWidth, int color, boolean shadow, Font.DisplayMode displayMode);

    /**
     * Draw the given text and scale it to the max width.
     * The given string may already be scaled and its width must be passed in that case.
     * @param guiGraphics The gui graphics
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param width The scaled width
     * @param originalScale The original scale
     * @param maxWidth The maximum width to scale to
     * @param shadow If a shadow should be drawn
     * @param displayMode The display mode
     */
    public void drawScaledCenteredString(GuiGraphics guiGraphics, Font fontRenderer, String string, int x, int y, int width, float originalScale, int maxWidth, int color, boolean shadow, Font.DisplayMode displayMode);

    /**
     * Draw the given text with the given width and desired scale.
     * @param guiGraphics The gui graphics
     * @param fontRenderer The font renderer
     * @param string The string to draw
     * @param x The center X
     * @param y The center Y
     * @param width The scaled width
     * @param scale The desired scale
     * @param color The color to draw
     * @param shadow If a shadow should be drawn
     * @param displayMode The font display mode
     */
    public void drawScaledCenteredString(GuiGraphics guiGraphics, Font fontRenderer, String string, int x, int y, int width, float scale, int color, boolean shadow, Font.DisplayMode displayMode);

    /**
     * Retrieve the baked model from a given block state.
     * @param blockState The block state.
     * @return The corresponding baked model.
     */
    public BakedModel getBakedModel(BlockState blockState);

    /**
     * Retrieve the potentially dynamic baked model for a position.
     * This will automatically take into account smart block models.
     * @param world The world.
     * @param pos The position.
     * @return The baked model.
     */

    public BakedModel getDynamicBakedModel(Level world, BlockPos pos);

    /**
     * A custom way of spawning block hit effects.
     * @param particleManager The effect renderer.
     * @param world The world.
     * @param blockState The blockstate to render particles for.
     * @param pos The position.
     * @param side The hit side.
     */
    public void addBlockHitEffects(ParticleEngine particleManager, ClientLevel world, BlockState blockState, BlockPos pos, Direction side);

    /**
     * @return Texture getter for the block atlas
     */
    public Function<ResourceLocation, TextureAtlasSprite> getBlockTextureGetter();

    /**
     * Get the default icon from a block.
     * @param block The block.
     * @return The icon.
     */
    public TextureAtlasSprite getBlockIcon(Block block);

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
    public boolean isPointInRegion(int left, int top, int width, int height, double pointX, double pointY);

    /**
     * Check if a point is inside a region.
     * @param region The region.
     * @param point The point.
     * @return If the point is inside the region.
     */
    public boolean isPointInRegion(Rectangle region, Point point);

    /**
     * Check if a point is inside a button's region.
     * @param button The button.
     * @param pointX The point x
     * @param pointY The point y
     * @return If the point is inside the button's region.
     */
    public boolean isPointInButton(Button button, int pointX, int pointY);

    public void blitColored(GuiGraphics guiGraphics, int x, int y, int z, float u, float v, int width, int height, float r, float g, float b, float a);

    public void blitColored(GuiGraphics guiGraphics, int x, int y, int z, int width, int height, float u0, float u1, float v0, float v1, float r, float g, float b, float a);

}
