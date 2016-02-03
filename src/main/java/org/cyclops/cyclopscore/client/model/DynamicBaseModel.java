package org.cyclops.cyclopscore.client.model;

import com.google.common.collect.ImmutableMap;
import com.google.common.primitives.Ints;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.model.*;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.Helpers;
import org.lwjgl.util.Color;

import javax.vecmath.Matrix4f;
import javax.vecmath.Vector3f;
import java.util.Collections;
import java.util.List;

/**
 * A model that can be used as a basis for flexible baked models.
 * @author rubensworks
 */
public abstract class DynamicBaseModel implements IFlexibleBakedModel, IPerspectiveAwareModel {

    // Rotation UV coordinates
    protected static final float[][] ROTATION_UV = {{1, 0}, {1, 1}, {0, 1}, {0, 0}};
    // A rotation offset fix for all sides
    protected static final int[] ROTATION_FIX = {2, 0, 2, 0, 1, 3};
    // u1, v1; u2, v2
    protected static final float[][] UVS = {{0, 0}, {1, 1}};

    // Third person transform for block items
    private static final TRSRTransformation THIRD_PERSON = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
            new Vector3f(0, 1.5f / 16, -2.75f / 16),
            TRSRTransformation.quatFromYXZDegrees(new Vector3f(10, -45, 170)),
            new Vector3f(0.375f, 0.375f, 0.375f),
            null));

    // Default perspective transforms
    protected static final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> DEFAULT_PERSPECTIVE_TRANSFORMS =
            ImmutableMap.of(ItemCameraTransforms.TransformType.THIRD_PERSON, THIRD_PERSON);

    /**
     * Rotate a given vector to the given side.
     * @param vec The vector to rotate.
     * @param side The side to rotate by.
     * @return The rotated vector.
     */
    protected static Vec3 rotate(Vec3 vec, EnumFacing side) {
        switch(side) {
            case DOWN:  return new Vec3( vec.xCoord, -vec.yCoord, -vec.zCoord);
            case UP:    return new Vec3( vec.xCoord,  vec.yCoord,  vec.zCoord);
            case NORTH: return new Vec3( vec.xCoord,  vec.zCoord, -vec.yCoord);
            case SOUTH: return new Vec3( vec.xCoord, -vec.zCoord,  vec.yCoord);
            case WEST:  return new Vec3(-vec.yCoord,  vec.xCoord,  vec.zCoord);
            case EAST:  return new Vec3( vec.yCoord, -vec.xCoord,  vec.zCoord);
        }
        return vec;
    }

    /**
     * Rotate a given vector inversely to the given side.
     * @param vec The vector to rotate.
     * @param side The side to rotate by.
     * @return The inversely rotated vector.
     */
    protected static Vec3 revRotate(Vec3 vec, EnumFacing side) {
        switch(side) {
            case DOWN:  return new Vec3( vec.xCoord, -vec.yCoord, -vec.zCoord);
            case UP:    return new Vec3( vec.xCoord,  vec.yCoord,  vec.zCoord);
            case NORTH: return new Vec3( vec.xCoord, -vec.zCoord,  vec.yCoord);
            case SOUTH: return new Vec3( vec.xCoord,  vec.zCoord, -vec.yCoord);
            case WEST:  return new Vec3( vec.yCoord, -vec.xCoord,  vec.zCoord);
            case EAST:  return new Vec3(-vec.yCoord,  vec.xCoord,  vec.zCoord);
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
                Float.floatToRawIntBits(texture.getInterpolatedU(u)),
                Float.floatToRawIntBits(texture.getInterpolatedV(v)),
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
                                     TextureAtlasSprite texture, EnumFacing side) {
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
                                       TextureAtlasSprite texture, int shadeColor, EnumFacing side) {
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
                                              TextureAtlasSprite texture, Color shadeColor, EnumFacing side) {
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
                                              TextureAtlasSprite texture, int shadeColor, EnumFacing side) {
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
                                       TextureAtlasSprite texture, int shadeColor, EnumFacing side, boolean isColored) {
        Vec3 v1 = rotate(new Vec3(x1 - .5, y - .5, z1 - .5), side).addVector(.5, .5, .5);
        Vec3 v2 = rotate(new Vec3(x1 - .5, y - .5, z2 - .5), side).addVector(.5, .5, .5);
        Vec3 v3 = rotate(new Vec3(x2 - .5, y - .5, z2 - .5), side).addVector(.5, .5, .5);
        Vec3 v4 = rotate(new Vec3(x2 - .5, y - .5, z1 - .5), side).addVector(.5, .5, .5);
        int[] data =  Ints.concat(
                vertexToInts((float) v1.xCoord, (float) v1.yCoord, (float) v1.zCoord, shadeColor, texture, x1 * 16, z1 * 16),
                vertexToInts((float) v2.xCoord, (float) v2.yCoord, (float) v2.zCoord, shadeColor, texture, x1 * 16, z2 * 16),
                vertexToInts((float) v3.xCoord, (float) v3.yCoord, (float) v3.zCoord, shadeColor, texture, x2 * 16, z2 * 16),
                vertexToInts((float) v4.xCoord, (float) v4.yCoord, (float) v4.zCoord, shadeColor, texture, x2 * 16, z1 * 16)
        );
        ForgeHooksClient.fillNormal(data, side); // This fixes lighting issues when item is rendered in hand/inventory
        quads.add(isColored ? new IColoredBakedQuad.ColoredBakedQuad(data, -1, side) : new BakedQuad(data, -1, side));
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
                                              TextureAtlasSprite texture, EnumFacing side, int rotation) {
        float[][] r = ROTATION_UV;
        Vec3 v1 = rotate(new Vec3(x1 - .5, y - .5, z1 - .5), side).addVector(.5, .5, .5);
        Vec3 v2 = rotate(new Vec3(x1 - .5, y - .5, z2 - .5), side).addVector(.5, .5, .5);
        Vec3 v3 = rotate(new Vec3(x2 - .5, y - .5, z2 - .5), side).addVector(.5, .5, .5);
        Vec3 v4 = rotate(new Vec3(x2 - .5, y - .5, z1 - .5), side).addVector(.5, .5, .5);
        int[] data =  Ints.concat(
                vertexToInts((float) v1.xCoord, (float) v1.yCoord, (float) v1.zCoord, -1, texture, r[(0 + rotation) % 4][0] * 16, r[(0 + rotation) % 4][1] * 16),
                vertexToInts((float) v2.xCoord, (float) v2.yCoord, (float) v2.zCoord, -1, texture, r[(1 + rotation) % 4][0] * 16, r[(1 + rotation) % 4][1] * 16),
                vertexToInts((float) v3.xCoord, (float) v3.yCoord, (float) v3.zCoord, -1, texture, r[(2 + rotation) % 4][0] * 16, r[(2 + rotation) % 4][1] * 16),
                vertexToInts((float) v4.xCoord, (float) v4.yCoord, (float) v4.zCoord, -1, texture, r[(3 + rotation) % 4][0] * 16, r[(3 + rotation) % 4][1] * 16)
        );
        quads.add(new BakedQuad(data, -1, side));
    }

    @Override
    public List<BakedQuad> getFaceQuads(EnumFacing side) {
        return Collections.emptyList();
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        return null;
    }

    @Override
    public boolean isGui3d() {
        return true;
    }

    @Override
    public boolean isAmbientOcclusion() {
        return true;
    }

    @Override
    public boolean isBuiltInRenderer() {
        return false;
    }

    @Override
    public ItemCameraTransforms getItemCameraTransforms() {
        return ItemCameraTransforms.DEFAULT;
    }

    @Override
    public VertexFormat getFormat() {
        return Attributes.DEFAULT_BAKED_FORMAT;
    }

    @Override
    public Pair<? extends IFlexibleBakedModel, Matrix4f> handlePerspective(ItemCameraTransforms.TransformType cameraTransformType) {
        return IPerspectiveAwareModel.MapWrapper.handlePerspective(this, DEFAULT_PERSPECTIVE_TRANSFORMS, cameraTransformType);
    }
}
