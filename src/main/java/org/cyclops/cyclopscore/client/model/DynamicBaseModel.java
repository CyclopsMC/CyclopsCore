package org.cyclops.cyclopscore.client.model;

import com.google.common.primitives.Ints;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.renderer.block.model.ItemOverrides;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.core.Direction;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import org.cyclops.cyclopscore.helper.Helpers;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * A model that can be used as a basis for flexible baked models.
 * @author rubensworks
 */
public abstract class DynamicBaseModel implements BakedModel {

    // Rotation UV coordinates
    protected static final float[][] ROTATION_UV = {{1, 0}, {1, 1}, {0, 1}, {0, 0}};
    // A rotation offset fix for all sides
    protected static final int[] ROTATION_FIX = {2, 0, 2, 0, 1, 3};
    // u1, v1; u2, v2
    protected static final float[][] UVS = {{0, 0}, {1, 1}};

    /**
     * Rotate a given vector to the given side.
     * @param vec The vector to rotate.
     * @param side The side to rotate by.
     * @return The rotated vector.
     */
    protected static Vec3 rotate(Vec3 vec, Direction side) {
        switch(side) {
            case DOWN:  return new Vec3( vec.x, -vec.y, -vec.z);
            case UP:    return new Vec3( vec.x,  vec.y,  vec.z);
            case NORTH: return new Vec3( vec.x,  vec.z, -vec.y);
            case SOUTH: return new Vec3( vec.x, -vec.z,  vec.y);
            case WEST:  return new Vec3(-vec.y,  vec.x,  vec.z);
            case EAST:  return new Vec3( vec.y, -vec.x,  vec.z);
        }
        return vec;
    }

    /**
     * Rotate a given vector inversely to the given side.
     * @param vec The vector to rotate.
     * @param side The side to rotate by.
     * @return The inversely rotated vector.
     */
    protected static Vec3 revRotate(Vec3 vec, Direction side) {
        switch(side) {
            case DOWN:  return new Vec3( vec.x, -vec.y, -vec.z);
            case UP:    return new Vec3( vec.x,  vec.y,  vec.z);
            case NORTH: return new Vec3( vec.x, -vec.z,  vec.y);
            case SOUTH: return new Vec3( vec.x,  vec.z, -vec.y);
            case WEST:  return new Vec3( vec.y, -vec.x,  vec.z);
            case EAST:  return new Vec3(-vec.y,  vec.x,  vec.z);
        }
        return vec;
    }

    /**
     * Make an int array of the given information so that it can be fed into a
     * {@link BakedQuad}.
     * @param x X
     * @param y Y
     * @param z Z
     * @param color Color
     * @param texture Texture
     * @param u Icon U
     * @param v Icon V
     * @return The assembled int array.
     */
    protected static int[] vertexToInts(float x, float y, float z, int color, TextureAtlasSprite texture, float u,
                                        float v) {
        return new int[] {
                Float.floatToRawIntBits(x),
                Float.floatToRawIntBits(y),
                Float.floatToRawIntBits(z),
                color,
                Float.floatToRawIntBits(texture.getU(u)),
                Float.floatToRawIntBits(texture.getV(v)),
                0,
                0
        };
    }

    /**
     * Add a given quad to a list of quads.
     * @param quads The quads to append to.
     * @param x1 Start X
     * @param x2 End X
     * @param z1 Start Z
     * @param z2 End Z
     * @param y Y
     * @param texture The base texture
     * @param side The side to add render quad at.
     */
    protected static void addBakedQuad(List<BakedQuad> quads, float x1, float x2, float z1, float z2, float y,
                                     TextureAtlasSprite texture, Direction side) {
        addBakedQuad(quads, x1, x2, z1, z2, y, texture, -1, side);
    }

    /**
     * Add a given quad to a list of quads.
     * @param quads The quads to append to.
     * @param x1 Start X
     * @param x2 End X
     * @param z1 Start Z
     * @param z2 End Z
     * @param y Y
     * @param texture The base texture
     * @param shadeColor shade color for the texture in BGR format
     * @param side The side to add render quad at.
     */
    protected static void addBakedQuad(List<BakedQuad> quads, float x1, float x2, float z1, float z2, float y,
                                       TextureAtlasSprite texture, int shadeColor, Direction side) {
        addBakedQuad(quads, x1, x2, z1, z2, y, texture, shadeColor, side, false);
    }

    /**
     * Add a given colored quad to a list of quads.
     * @param quads The quads to append to.
     * @param x1 Start X
     * @param x2 End X
     * @param z1 Start Z
     * @param z2 End Z
     * @param y Y
     * @param texture The base texture
     * @param shadeColor shade color for the texture
     * @param side The side to add render quad at.
     */
    protected static void addColoredBakedQuad(List<BakedQuad> quads, float x1, float x2, float z1, float z2, float y,
                                              TextureAtlasSprite texture, Color shadeColor, Direction side) {
        int color = Helpers.RGBAToInt(shadeColor.getBlue(), shadeColor.getGreen(), shadeColor.getRed(), shadeColor.getAlpha());
        addColoredBakedQuad(quads, x1, x2, z1, z2, y, texture, color, side);
    }

    /**
     * Add a given colored quad to a list of quads.
     * @param quads The quads to append to.
     * @param x1 Start X
     * @param x2 End X
     * @param z1 Start Z
     * @param z2 End Z
     * @param y Y
     * @param texture The base texture
     * @param shadeColor shade color for the texture in BGR format
     * @param side The side to add render quad at.
     */
    protected static void addColoredBakedQuad(List<BakedQuad> quads, float x1, float x2, float z1, float z2, float y,
                                              TextureAtlasSprite texture, int shadeColor, Direction side) {
        addBakedQuad(quads, x1, x2, z1, z2, y, texture, shadeColor, side, true);
    }

    /**
     * Add a given quad to a list of quads.
     * @param quads The quads to append to.
     * @param x1 Start X
     * @param x2 End X
     * @param z1 Start Z
     * @param z2 End Z
     * @param y Y
     * @param texture The base texture
     * @param shadeColor shade color for the texture in BGR format
     * @param side The side to add render quad at.
     * @param isColored When set to true a colored baked quad will be made, otherwise a regular baked quad is used.
     */
    private static void addBakedQuad(List<BakedQuad> quads, float x1, float x2, float z1, float z2, float y,
                                     TextureAtlasSprite texture, int shadeColor, Direction side, boolean isColored) {
        addBakedQuadRotated(quads, x1, x2, z1, z2, y, texture, side, 0, isColored,
                shadeColor, new float[][]{{x1, z1}, {x1, z2}, {x2, z2}, {x2, z1}});
    }

    /**
     * Add a given rotated quad to a list of quads.
     * @param quads The quads to append to.
     * @param x1 Start X
     * @param x2 End X
     * @param z1 Start Z
     * @param z2 End Z
     * @param y Y
     * @param texture The base texture
     * @param side The side to add render quad at.
     * @param rotation The rotation index to rotate by.
     */
    protected static void addBakedQuadRotated(List<BakedQuad> quads, float x1, float x2, float z1, float z2, float y,
                                              TextureAtlasSprite texture, Direction side, int rotation) {
        addBakedQuadRotated(quads, x1, x2, z1, z2, y, texture, side, rotation, false, -1, ROTATION_UV);
    }

    /**
     * Add a given rotated quad to a list of quads.
     * @param quads The quads to append to.
     * @param x1 Start X
     * @param x2 End X
     * @param z1 Start Z
     * @param z2 End Z
     * @param y Y
     * @param texture The base texture
     * @param side The side to add render quad at.
     * @param rotation The rotation index to rotate by.
     * @param isColored When set to true a colored baked quad will be made, otherwise a regular baked quad is used.
     * @param shadeColor The shade color
     * @param uvs A double array of 4 uv pairs
     */
    protected static void addBakedQuadRotated(List<BakedQuad> quads, float x1, float x2, float z1, float z2, float y,
                                              TextureAtlasSprite texture, Direction side, int rotation,
                                              boolean isColored, int shadeColor, float[][] uvs) {
        Vec3 v1 = rotate(new Vec3(x1 - .5, y - .5, z1 - .5), side).add(.5, .5, .5);
        Vec3 v2 = rotate(new Vec3(x1 - .5, y - .5, z2 - .5), side).add(.5, .5, .5);
        Vec3 v3 = rotate(new Vec3(x2 - .5, y - .5, z2 - .5), side).add(.5, .5, .5);
        Vec3 v4 = rotate(new Vec3(x2 - .5, y - .5, z1 - .5), side).add(.5, .5, .5);
        int[] data =  Ints.concat(
                vertexToInts((float) v1.x, (float) v1.y, (float) v1.z, shadeColor, texture, uvs[(0 + rotation) % 4][0] * 16, uvs[(0 + rotation) % 4][1] * 16),
                vertexToInts((float) v2.x, (float) v2.y, (float) v2.z, shadeColor, texture, uvs[(1 + rotation) % 4][0] * 16, uvs[(1 + rotation) % 4][1] * 16),
                vertexToInts((float) v3.x, (float) v3.y, (float) v3.z, shadeColor, texture, uvs[(2 + rotation) % 4][0] * 16, uvs[(2 + rotation) % 4][1] * 16),
                vertexToInts((float) v4.x, (float) v4.y, (float) v4.z, shadeColor, texture, uvs[(3 + rotation) % 4][0] * 16, uvs[(3 + rotation) % 4][1] * 16)
        );
        ForgeHooksClient.fillNormal(data, side); // This fixes lighting issues when item is rendered in hand/inventory
        quads.add(new BakedQuad(data, -1, side, texture, false));
    }

    @Override
    public List<BakedQuad> getQuads(@Nullable BlockState state, @Nullable Direction side, Random rand) {
        return Collections.emptyList();
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean useAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isCustomRenderer() {
        return false;
    }

    @Override
    public ItemOverrides getOverrides() {
        return ItemOverrides.EMPTY;
    }
}
