package org.cyclops.cyclopscore.client.model;

import net.minecraft.client.model.geom.builders.UVPair;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.block.model.SimpleModelWrapper;
import net.minecraft.client.renderer.chunk.ChunkSectionLayer;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.QuadCollection;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.client.model.quad.BakedColors;
import net.neoforged.neoforge.client.model.quad.BakedNormals;
import net.neoforged.neoforge.model.data.ModelData;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.joml.Vector3f;

import java.awt.*;
import java.util.List;

/**
 * A model that can be used as a basis for flexible baked models.
 * @author rubensworks
 */
public abstract class DynamicBaseModel implements BlockStateModel {

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
    protected static Vector3f rotate(Vector3f vec, Direction side) {
        switch(side) {
            case DOWN:  return new Vector3f( vec.x, -vec.y, -vec.z);
            case UP:    return new Vector3f( vec.x,  vec.y,  vec.z);
            case NORTH: return new Vector3f( vec.x,  vec.z, -vec.y);
            case SOUTH: return new Vector3f( vec.x, -vec.z,  vec.y);
            case WEST:  return new Vector3f(-vec.y,  vec.x,  vec.z);
            case EAST:  return new Vector3f( vec.y, -vec.x,  vec.z);
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
        int color = IModHelpers.get().getBaseHelpers().RGBAToInt(shadeColor.getBlue(), shadeColor.getGreen(), shadeColor.getRed(), shadeColor.getAlpha());
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
        // If needed, these Vector3fc's could be cached using ModelBaker: modelBaker.parts().vector(...)
        Vector3f v1 = rotate(new Vector3f(x1 - .5f, y - .5f, z1 - .5f), side).add(.5f, .5f, .5f);
        Vector3f v2 = rotate(new Vector3f(x1 - .5f, y - .5f, z2 - .5f), side).add(.5f, .5f, .5f);
        Vector3f v3 = rotate(new Vector3f(x2 - .5f, y - .5f, z2 - .5f), side).add(.5f, .5f, .5f);
        Vector3f v4 = rotate(new Vector3f(x2 - .5f, y - .5f, z1 - .5f), side).add(.5f, .5f, .5f);
        // See QuadBakingVertexConsumer for examples on how BakedQuad is constructed
        quads.add(new BakedQuad(
                v1,
                v2,
                v3,
                v4,
                UVPair.pack(texture.getU(uvs[(0 + rotation) % 4][0]), texture.getV(uvs[(0 + rotation) % 4][1])),
                UVPair.pack(texture.getU(uvs[(1 + rotation) % 4][0]), texture.getV(uvs[(1 + rotation) % 4][1])),
                UVPair.pack(texture.getU(uvs[(2 + rotation) % 4][0]), texture.getV(uvs[(2 + rotation) % 4][1])),
                UVPair.pack(texture.getU(uvs[(3 + rotation) % 4][0]), texture.getV(uvs[(3 + rotation) % 4][1])),
                -1,
                side,
                texture,
                false,
                0,
                BakedNormals.UNSPECIFIED,
                isColored ? BakedColors.of(shadeColor) : BakedColors.DEFAULT,
                true
        ));
    }

    public abstract List<BakedQuad> handleBlockState(BlockAndTintGetter level, BlockPos pos,
                                                     BlockState state, Direction side,
                                                     RandomSource rand, ModelData extraData,
                                                     ChunkSectionLayer renderType);

    @Override
    public void collectParts(BlockAndTintGetter level, BlockPos pos, BlockState state, RandomSource random, List<BlockModelPart> parts) {
        ModelData extraData = getModelData(level, pos, state, level.getModelData(pos));
        for (ChunkSectionLayer renderType : getRenderTypes(state, random, extraData)) {
            for (Direction side : Direction.values()) {
                QuadCollection.Builder quadCollectionBuilder = new QuadCollection.Builder();
                for (BakedQuad blockStateQuad : handleBlockState(level, pos, state, side, random, extraData, renderType)) {
                    quadCollectionBuilder = quadCollectionBuilder.addCulledFace(side, blockStateQuad);
                }
                parts.add(new SimpleModelWrapper(
                        quadCollectionBuilder.build(),
                        usesBlockLight(),
                        particleIcon(level, pos, state),
                        renderType
                ));
            }
        }
    }

    @Override
    public void collectParts(RandomSource random, List<BlockModelPart> output) {
        // Do nothing, as it's should be never called.
    }

    public abstract ModelData getModelData(BlockAndTintGetter world, BlockPos pos, BlockState state, ModelData tileData);

    public abstract List<ChunkSectionLayer> getRenderTypes(BlockState state, RandomSource rand, ModelData data);

    public abstract boolean usesBlockLight();
}
