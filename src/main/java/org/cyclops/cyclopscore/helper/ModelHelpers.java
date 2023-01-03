package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.BlockModel;
import net.minecraft.client.renderer.block.model.ItemTransform;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.Resource;
import net.minecraftforge.client.model.data.ModelData;
import net.minecraftforge.client.model.data.ModelProperty;
import org.joml.Vector3f;

import javax.annotation.Nullable;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Helpers for models.
 * @author rubensworks
 */
public final class ModelHelpers {

    public static final ItemTransform THIRD_PERSON_RIGHT_HAND = new ItemTransform(
            new Vector3f(70, 45, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.375f, 0.375f, 0.375f));
    public static final ItemTransform THIRD_PERSON_LEFT_HAND = new ItemTransform(
            new Vector3f(70, 45, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.375f, 0.375f, 0.375f));
    public static final ItemTransform FIRST_PERSON_RIGHT_HAND = new ItemTransform(
            new Vector3f(0, 45, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.4f, 0.4f, 0.4f));
    public static final ItemTransform FIRST_PERSON_LEFT_HAND = new ItemTransform(
            new Vector3f(0, 255, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.4f, 0.4f, 0.4f));
    public static final ItemTransform HEAD = ItemTransform.NO_TRANSFORM;
    public static final ItemTransform GROUND = new ItemTransform(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.375f, 0.375f, 0.375f));
    public static final ItemTransform FIXED = new ItemTransform(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.5f, 0.5f, 0.5f));
    public static final ItemTransform GUI = new ItemTransform(
            new Vector3f(30, 225, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.625f, 0.625f, 0.625f));
    public static final ItemTransforms DEFAULT_CAMERA_TRANSFORMS = new ItemTransforms(
            THIRD_PERSON_RIGHT_HAND,
            THIRD_PERSON_LEFT_HAND,
            FIRST_PERSON_RIGHT_HAND,
            FIRST_PERSON_LEFT_HAND,
            HEAD,
            GUI,
            GROUND,
            FIXED
    );

    public static final ItemTransform THIRD_PERSON_RIGHT_HAND_ITEM = new ItemTransform(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.55f, 0.55f, 0.55f));
    public static final ItemTransform THIRD_PERSON_LEFT_HAND_ITEM = new ItemTransform(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.55f, 0.55f, 0.55f));
    public static final ItemTransform FIRST_PERSON_RIGHT_HAND_ITEM = new ItemTransform(
            new Vector3f(0, -90, 25),
            new Vector3f(0, 0, 0),
            new Vector3f(0.68F, 0.68F, 0.68F));
    public static final ItemTransform FIRST_PERSON_LEFT_HAND_ITEM = new ItemTransform(
            new Vector3f(0, -90, 25),
            new Vector3f(0, 0, 0),
            new Vector3f(0.68F, 0.68F, 0.68F));
    public static final ItemTransform HEAD_ITEM = ItemTransform.NO_TRANSFORM;
    public static final ItemTransform GROUND_ITEM = new ItemTransform(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.5f, 0.5f, 0.5f));
    public static final ItemTransform FIXED_ITEM = new ItemTransform(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(1f, 1f, 1f));
    public static final ItemTransform GUI_ITEM = new ItemTransform(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(1f, 1f, 1f));
    public static final ItemTransforms DEFAULT_CAMERA_TRANSFORMS_ITEM = new ItemTransforms(
            THIRD_PERSON_RIGHT_HAND_ITEM,
            THIRD_PERSON_LEFT_HAND_ITEM,
            FIRST_PERSON_RIGHT_HAND_ITEM,
            FIRST_PERSON_LEFT_HAND_ITEM,
            HEAD_ITEM,
            GUI_ITEM,
            GROUND_ITEM,
            FIXED_ITEM
    );

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
        Resource resource = Minecraft.getInstance().getResourceManager().getResource(
                new ResourceLocation(modelLocation.getNamespace(), modelLocation.getPath() + ".json")).get();
        return BlockModel.fromStream(resource.openAsReader());
    }

    /**
     * Safely get a model data property for a data state and value that may not have been set yet.
     * @param modelData The model data.
     * @param property The property to get the value for.
     * @param fallback The fallback value when something has failed.
     * @param <T> The type of value to fetch.
     * @return The value.
     */
    public static <T> T getSafeProperty(@Nullable ModelData modelData, ModelProperty<T> property, T fallback) {
        if(modelData == null) {
            return fallback;
        }
        T value;
        try {
            value = modelData.get(property);
        } catch (IllegalArgumentException e) {
            return fallback;
        }
        if(value == null) {
            return fallback;
        }
        return value;
    }

    /**
     * Apply the given overrides to the default transformations.
     * @param overrides The transformations to override.
     * @return The resulting transformation map.
     */
    public static ItemTransforms modifyDefaultTransforms(Map<ItemTransforms.TransformType, ItemTransform> overrides) {
        return new ItemTransforms(
                overrides.getOrDefault(ItemTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_RIGHT_HAND),
                overrides.getOrDefault(ItemTransforms.TransformType.THIRD_PERSON_LEFT_HAND, THIRD_PERSON_LEFT_HAND),
                overrides.getOrDefault(ItemTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_RIGHT_HAND),
                overrides.getOrDefault(ItemTransforms.TransformType.FIRST_PERSON_LEFT_HAND, FIRST_PERSON_LEFT_HAND),
                overrides.getOrDefault(ItemTransforms.TransformType.HEAD, HEAD),
                overrides.getOrDefault(ItemTransforms.TransformType.GUI, GUI),
                overrides.getOrDefault(ItemTransforms.TransformType.GROUND, GROUND),
                overrides.getOrDefault(ItemTransforms.TransformType.FIXED, FIXED)
        );

    }
}
