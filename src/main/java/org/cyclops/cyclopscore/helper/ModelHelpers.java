package org.cyclops.cyclopscore.helper;

import com.google.common.base.Charsets;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.util.math.vector.Vector3f;
import net.minecraft.client.renderer.model.BakedQuad;
import net.minecraft.client.renderer.model.BlockModel;
import net.minecraft.client.renderer.model.ItemCameraTransforms;
import net.minecraft.client.renderer.model.ItemTransformVec3f;
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

    public static final ItemTransformVec3f THIRD_PERSON_RIGHT_HAND = new ItemTransformVec3f(
            new Vector3f(70, 45, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.375f, 0.375f, 0.375f));
    public static final ItemTransformVec3f THIRD_PERSON_LEFT_HAND = new ItemTransformVec3f(
            new Vector3f(70, 45, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.375f, 0.375f, 0.375f));
    public static final ItemTransformVec3f FIRST_PERSON_RIGHT_HAND = new ItemTransformVec3f(
            new Vector3f(0, 45, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.4f, 0.4f, 0.4f));
    public static final ItemTransformVec3f FIRST_PERSON_LEFT_HAND = new ItemTransformVec3f(
            new Vector3f(0, 255, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.4f, 0.4f, 0.4f));
    public static final ItemTransformVec3f HEAD = ItemTransformVec3f.DEFAULT;
    public static final ItemTransformVec3f GROUND = new ItemTransformVec3f(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.375f, 0.375f, 0.375f));
    public static final ItemTransformVec3f FIXED = new ItemTransformVec3f(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.5f, 0.5f, 0.5f));
    public static final ItemTransformVec3f GUI = new ItemTransformVec3f(
            new Vector3f(30, 225, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.625f, 0.625f, 0.625f));
    public static final ItemCameraTransforms DEFAULT_CAMERA_TRANSFORMS = new ItemCameraTransforms(
            THIRD_PERSON_RIGHT_HAND,
            THIRD_PERSON_LEFT_HAND,
            FIRST_PERSON_RIGHT_HAND,
            FIRST_PERSON_LEFT_HAND,
            HEAD,
            GUI,
            GROUND,
            FIXED
    );

    public static final ItemTransformVec3f THIRD_PERSON_RIGHT_HAND_ITEM = new ItemTransformVec3f(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.55f, 0.55f, 0.55f));
    public static final ItemTransformVec3f THIRD_PERSON_LEFT_HAND_ITEM = new ItemTransformVec3f(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.55f, 0.55f, 0.55f));
    public static final ItemTransformVec3f FIRST_PERSON_RIGHT_HAND_ITEM = new ItemTransformVec3f(
            new Vector3f(0, -90, 25),
            new Vector3f(0, 0, 0),
            new Vector3f(0.68F, 0.68F, 0.68F));
    public static final ItemTransformVec3f FIRST_PERSON_LEFT_HAND_ITEM = new ItemTransformVec3f(
            new Vector3f(0, -90, 25),
            new Vector3f(0, 0, 0),
            new Vector3f(0.68F, 0.68F, 0.68F));
    public static final ItemTransformVec3f HEAD_ITEM = ItemTransformVec3f.DEFAULT;
    public static final ItemTransformVec3f GROUND_ITEM = new ItemTransformVec3f(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(0.5f, 0.5f, 0.5f));
    public static final ItemTransformVec3f FIXED_ITEM = new ItemTransformVec3f(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(1f, 1f, 1f));
    public static final ItemTransformVec3f GUI_ITEM = new ItemTransformVec3f(
            new Vector3f(0, 0, 0),
            new Vector3f(0, 0, 0),
            new Vector3f(1f, 1f, 1f));
    public static final ItemCameraTransforms DEFAULT_CAMERA_TRANSFORMS_ITEM = new ItemCameraTransforms(
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

    /**
     * Apply the given overrides to the default transformations.
     * @param overrides The transformations to override.
     * @return The resulting transformation map.
     */
    public static ItemCameraTransforms modifyDefaultTransforms(Map<ItemCameraTransforms.TransformType, ItemTransformVec3f> overrides) {
        return new ItemCameraTransforms(
                overrides.getOrDefault(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_RIGHT_HAND),
                overrides.getOrDefault(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, THIRD_PERSON_LEFT_HAND),
                overrides.getOrDefault(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_RIGHT_HAND),
                overrides.getOrDefault(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, FIRST_PERSON_LEFT_HAND),
                overrides.getOrDefault(ItemCameraTransforms.TransformType.HEAD, HEAD),
                overrides.getOrDefault(ItemCameraTransforms.TransformType.GUI, GUI),
                overrides.getOrDefault(ItemCameraTransforms.TransformType.GROUND, GROUND),
                overrides.getOrDefault(ItemCameraTransforms.TransformType.FIXED, FIXED)
        );

    }
}
