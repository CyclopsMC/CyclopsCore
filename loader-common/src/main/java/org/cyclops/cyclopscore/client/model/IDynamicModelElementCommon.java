package org.cyclops.cyclopscore.client.model;

import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.client.resources.model.ModelResourceLocation;
import org.apache.commons.lang3.tuple.Pair;

import java.util.function.Consumer;

/**
 * Interface for blocks and items which can have a dynamic model.
 * @author rubensworks
 */
public interface IDynamicModelElementCommon {

    /**
     * This will only be called once.
     * @param modelConsumer The model bake consumer.
     * @return A dynamic model instance.
     */
    public BakedModel createDynamicModel(Consumer<Pair<ModelResourceLocation, BakedModel>> modelConsumer);

}
