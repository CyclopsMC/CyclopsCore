package org.cyclops.cyclopscore.helper;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.resources.IResource;
import net.minecraft.util.Direction;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.data.IModelData;
import net.minecraftforge.client.model.data.ModelProperty;

import javax.annotation.Nullable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Helpers for models.
 * @author rubensworks
 */
public final class ModelHelpers {

    // An empty list^2 for quads.
    public static final Map<Direction, List<BakedQuad>> EMPTY_FACE_QUADS;
    static {
        EMPTY_FACE_QUADS = Maps.newHashMap();
        for (Direction facing : Direction.values()) {
            EMPTY_FACE_QUADS.put(facing, Collections.emptyList());
        }
    }

    /**
     * Read the given model location to a {@link BlockModel}.
     * @param modelLocation A model location (without .json suffix)
     * @return The corresponding model.
     * @throws IOException If the model file was invalid.
     */
    public static BlockModel loadModelBlock(ResourceLocation modelLocation) throws IOException {
        IResource resource = Minecraft.getInstance().getResourceManager().getResource(
                new ResourceLocation(modelLocation.getNamespace(), modelLocation.getPath() + ".json"));
        Reader reader = new InputStreamReader(resource.getInputStream(), Charsets.UTF_8);
        return BlockModel.deserialize(reader);
    }

    /**
     * Safely get a model data property for a data state and value that may not have been set yet.
     * @param modelData The model data.
     * @param property The property to get the value for.
     * @param fallback The fallback value when something has failed.
     * @param <T> The type of value to fetch.
     * @return The value.
     */
    public static <T> T getSafeProperty(@Nullable IModelData modelData, ModelProperty<T> property, T fallback) {
        if(modelData == null) {
            return fallback;
        }
        T value;
        try {
            value = modelData.getData(property);
        } catch (IllegalArgumentException e) {
            return fallback;
        }
        if(value == null) {
            return fallback;
        }
        return value;
    }
}
