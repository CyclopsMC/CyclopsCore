package org.cyclops.cyclopscore.client.model;

import com.google.common.collect.Lists;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModelPart;
import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.resources.model.ResolvedModel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.model.data.ModelData;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.util.Collections;
import java.util.List;

/**
 * A dynamic model that can be used for items and blocks.
 * @author rubensworks
 */
public abstract class DynamicItemAndBlockModel extends DynamicBaseModel implements ResolvedModel {

    private final boolean factory;
    private final boolean item;

    private Direction renderingSide;

    public DynamicItemAndBlockModel(boolean factory, boolean item) {
        super(Collections.emptyList());
        this.factory = factory;
        this.item = item;
    }

    protected boolean isItemStack() {
        return item;
    }

    public List<BakedQuad> getBlockStateQuads(BlockAndTintGetter level, BlockPos pos,
                                              BlockState state, Direction side,
                                              RandomSource rand, ModelData extraData,
                                              RenderType renderType) {
        List<BakedQuad> quads = Lists.newArrayList();
        BlockStateModel blockModel = handleBlockState(state, side, rand, extraData, renderType);
        for (BlockModelPart part : blockModel.collectParts(level, pos, state, rand)) {
            quads.addAll(part.getQuads(side));
        }
        return quads;
    }

    public List<BakedQuad> getGeneralQuads() {
        return Collections.emptyList();
    }

    public abstract BlockStateModel handleBlockState(@Nullable BlockState state, @Nullable Direction side,
                                                 @Nonnull RandomSource rand, @Nonnull ModelData extraData,
                                                @Nullable RenderType renderType);
    public abstract List<BakedQuad> handleItemState(@Nullable ItemStack stack, @Nullable Level world,
                                              @Nullable LivingEntity entity);

}
