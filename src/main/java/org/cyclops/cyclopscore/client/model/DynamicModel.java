package org.cyclops.cyclopscore.client.model;

import com.google.common.primitives.Ints;
import lombok.Getter;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.Attributes;
import net.minecraftforge.client.model.IFlexibleBakedModel;
import net.minecraftforge.client.model.ISmartBlockModel;
import net.minecraftforge.client.model.ISmartItemModel;
import net.minecraftforge.common.property.IExtendedBlockState;

import java.util.Collections;
import java.util.List;

/**
 * A model that can be used for {@link net.minecraftforge.common.property.IExtendedBlockState}s.
 * @author rubensworks
 */
public abstract class DynamicModel implements IFlexibleBakedModel, ISmartBlockModel, ISmartItemModel {

    @Getter private final IExtendedBlockState state;
    @Getter private final boolean isItemStack;

    public DynamicModel(IExtendedBlockState state, boolean isItemStack) {
        this.state = state;
        this.isItemStack = isItemStack;
    }

    public DynamicModel() {
        this(null, false);
    }

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
        return null;
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
        return null;
    }

    /**
     * Make an int array of the given information so that it can be fed into a
     * {@link net.minecraft.client.renderer.block.model.BakedQuad}.
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
        Vec3 v1 = rotate(new Vec3(x1 - .5, y - .5, z1 - .5), side).addVector(.5, .5, .5);
        Vec3 v2 = rotate(new Vec3(x1 - .5, y - .5, z2 - .5), side).addVector(.5, .5, .5);
        Vec3 v3 = rotate(new Vec3(x2 - .5, y - .5, z2 - .5), side).addVector(.5, .5, .5);
        Vec3 v4 = rotate(new Vec3(x2 - .5, y - .5, z1 - .5), side).addVector(.5, .5, .5);
        int[] data =  Ints.concat(
                vertexToInts((float) v1.xCoord, (float) v1.yCoord, (float) v1.zCoord, -1, texture, x1 * 16, z1 * 16),
                vertexToInts((float) v2.xCoord, (float) v2.yCoord, (float) v2.zCoord, -1, texture, x1 * 16, z2 * 16),
                vertexToInts((float) v3.xCoord, (float) v3.yCoord, (float) v3.zCoord, -1, texture, x2 * 16, z2 * 16),
                vertexToInts((float) v4.xCoord, (float) v4.yCoord, (float) v4.zCoord, -1, texture, x2 * 16, z1 * 16)
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
}
