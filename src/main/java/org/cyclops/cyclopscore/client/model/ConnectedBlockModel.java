package org.cyclops.cyclopscore.client.model;

import com.google.common.collect.Lists;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.IBakedModel;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.property.IExtendedBlockState;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockConnectedTexture;
import org.cyclops.cyclopscore.helper.BlockHelpers;

import java.util.List;

/**
 * Block model for allowing connected textures.
 * @author rubensworks
 */
public class ConnectedBlockModel extends DynamicModel {

    // With what direction should side X with rotation Y connect. rotation: N, E, S, W
    protected static final EnumFacing[][] CONNECT_MATRIX = {
            {EnumFacing.NORTH, EnumFacing.WEST,  EnumFacing.SOUTH, EnumFacing.EAST}, // DOWN
            {EnumFacing.NORTH, EnumFacing.EAST,  EnumFacing.SOUTH, EnumFacing.WEST}, // UP
            {EnumFacing.UP,    EnumFacing.WEST,  EnumFacing.DOWN,  EnumFacing.EAST}, // NORTH
            {EnumFacing.UP,    EnumFacing.EAST,  EnumFacing.DOWN,  EnumFacing.WEST}, // SOUTH
            {EnumFacing.UP,    EnumFacing.SOUTH, EnumFacing.DOWN,  EnumFacing.NORTH}, // WEST
            {EnumFacing.UP,    EnumFacing.NORTH, EnumFacing.DOWN,  EnumFacing.SOUTH}, // EAST
    };
    // With what direction should side X with rotation Y connect. rotation: N, E, S, W
    protected static final DirectionCorner[][] CONNECT_CORNER_MATRIX = {
            {DirectionCorner.MIDDLE_NORTHWEST, DirectionCorner.MIDDLE_SOUTHWEST, DirectionCorner.MIDDLE_SOUTHEAST, DirectionCorner.MIDDLE_NORTHEAST}, // DOWN
            {DirectionCorner.MIDDLE_NORTHWEST, DirectionCorner.MIDDLE_NORTHEAST, DirectionCorner.MIDDLE_SOUTHEAST, DirectionCorner.MIDDLE_SOUTHWEST}, // UP
            {DirectionCorner.UPPER_EAST,       DirectionCorner.UPPER_WEST,       DirectionCorner.LOWER_WEST,       DirectionCorner.LOWER_EAST},       // NORTH
            {DirectionCorner.UPPER_WEST,       DirectionCorner.UPPER_EAST,       DirectionCorner.LOWER_EAST,       DirectionCorner.LOWER_WEST},       // SOUTH
            {DirectionCorner.UPPER_NORTH,      DirectionCorner.UPPER_SOUTH,      DirectionCorner.LOWER_SOUTH,      DirectionCorner.LOWER_NORTH},      // WEST
            {DirectionCorner.UPPER_SOUTH,      DirectionCorner.UPPER_NORTH,      DirectionCorner.LOWER_NORTH,      DirectionCorner.LOWER_SOUTH},      // EAST
    };

    private final ConfigurableBlockConnectedTexture block;

    public ConnectedBlockModel(ConfigurableBlockConnectedTexture block, IExtendedBlockState state, boolean isItemStack) {
        super(state, isItemStack);
        this.block = block;
    }

    public ConnectedBlockModel(ConfigurableBlockConnectedTexture block) {
        super();
        this.block = block;
    }

    @Override
    public List<BakedQuad> getGeneralQuads() {
        float[][] v = UVS;

        List<BakedQuad> ret = Lists.newLinkedList();
        TextureAtlasSprite backgroundTexture = getTexture();
        TextureAtlasSprite sideTexture = block.getTexture("border");
        TextureAtlasSprite cornerTexture = block.getTexture("corner");
        TextureAtlasSprite innerCornerTexture= block.getTexture("innerCorner");

        // Loop over all sides
        for (EnumFacing side : EnumFacing.values()) {
            if(isItemStack() || !BlockHelpers.getSafeBlockStateProperty(getState(), ConfigurableBlockConnectedTexture.CONNECTED[side.ordinal()], false)) {
                // background texture
                addBakedQuad(ret, 0, 1, 0, 1, 1, backgroundTexture, side);

                // borders
                for (int rotation = 0; rotation < 4; rotation++) {
                    // add correction for each side to rotation
                    int ar = (rotation + ROTATION_FIX[side.ordinal()]) % 4;

                    EnumFacing realSide = CONNECT_MATRIX[side.ordinal()][rotation];
                    boolean isConnected = !isItemStack() && BlockHelpers.getSafeBlockStateProperty(getState(), ConfigurableBlockConnectedTexture.CONNECTED[realSide.ordinal()], false);
                    if (!isConnected) {
                        addBakedQuadRotated(ret, v[0][0], v[1][0], v[0][1], v[1][1], 1, sideTexture, side, ar);
                    }
                }

                // corners and inner corners
                for (int rotation = 0; rotation < 4; rotation++) {
                    // add correction for each side to rotation
                    int arPrev = (rotation + 3 + ROTATION_FIX[side.ordinal()]) % 4;

                    EnumFacing realSide = CONNECT_MATRIX[side.ordinal()][rotation];
                    EnumFacing realSidePrev = CONNECT_MATRIX[side.ordinal()][(rotation + 3) % 4];
                    DirectionCorner corner = CONNECT_CORNER_MATRIX[side.ordinal()][rotation];
                    boolean isConnected = !isItemStack() && BlockHelpers.getSafeBlockStateProperty(getState(), ConfigurableBlockConnectedTexture.CONNECTED[realSide.ordinal()], false);
                    boolean isConnectedPrev = !isItemStack() && BlockHelpers.getSafeBlockStateProperty(getState(), ConfigurableBlockConnectedTexture.CONNECTED[realSidePrev.ordinal()], false);
                    boolean isCornerConnected = !isItemStack() && BlockHelpers.getSafeBlockStateProperty(getState(), ConfigurableBlockConnectedTexture.CONNECTED_CORNER[corner.ordinal()], false);
                    if (!isConnected && !isConnectedPrev) {
                        addBakedQuadRotated(ret, v[0][0], v[1][0], v[0][1], v[1][1], 1, cornerTexture, side, arPrev);
                    }
                    if (isConnected && isConnectedPrev && !isCornerConnected) {
                        addBakedQuadRotated(ret, v[0][0], v[1][0], v[0][1], v[1][1], 1, innerCornerTexture, side, arPrev);
                    }
                }
            }
        }

        return ret;
    }

    @Override
    public IBakedModel handleBlockState(IBlockState state) {
        return new ConnectedBlockModel(block, (IExtendedBlockState) state, false);
    }

    @Override
    public IBakedModel handleItemState(ItemStack stack) {
        return new ConnectedBlockModel(block, (IExtendedBlockState) block.getDefaultState(), true);
    }

    @Override
    public TextureAtlasSprite getTexture() {
        return block.getTexture("background");
    }
}
