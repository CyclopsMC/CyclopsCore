package org.cyclops.cyclopscore.client.model;

import net.minecraft.client.renderer.block.model.BlockStateModel;
import net.minecraft.client.renderer.item.ItemModel;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Consumer;
import java.util.function.Function;

/**
 * Interface for blocks and items which can have a dynamic model.
 * @author rubensworks
 */
public interface IDynamicModelElementCommon {

    /**
     * This will only be called once.
     * @param modelConsumer The model bake consumer.
     * @return A dynamic block model instance.
     */
    public BlockStateModel createDynamicBlockModel(Consumer<Pair<BlockState, BlockStateModel>> modelConsumer, Function<BlockState, BlockStateModel> modelRetriever);

    /**
     * This will only be called once.
     * @param modelConsumer The model bake consumer.
     * @return A dynamic item model instance.
     */
    public ItemModel createDynamicItemModel(Consumer<Pair<Identifier, ItemModel>> modelConsumer, Function<Identifier, ItemModel> modelRetriever);

}
