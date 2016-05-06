package org.cyclops.cyclopscore.helper;

import com.google.common.base.Charsets;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.ItemCameraTransforms;
import net.minecraft.client.renderer.block.model.ModelBlock;
import net.minecraft.client.resources.IResource;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.model.TRSRTransformation;
import org.apache.commons.lang3.tuple.Triple;

import javax.vecmath.Vector3f;
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

    public static final TRSRTransformation THIRD_PERSON_RIGHT_HAND = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
            new Vector3f(0, 0, 0),
            TRSRTransformation.quatFromXYZDegrees(new Vector3f(70, 45, 0)),
            new Vector3f(0.375f, 0.375f, 0.375f),
            null));
    public static final TRSRTransformation THIRD_PERSON_LEFT_HAND = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
            new Vector3f(0, 0, 0),
            TRSRTransformation.quatFromXYZDegrees(new Vector3f(70, 45, 0)),
            new Vector3f(0.375f, 0.375f, 0.375f),
            null));
    public static final TRSRTransformation FIRST_PERSON_RIGHT_HAND = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
            new Vector3f(0, 0, 0),
            TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, 45, 0)),
            new Vector3f(0.4F, 0.4F, 0.4F),
            null));
    public static final TRSRTransformation FIRST_PERSON_LEFT_HAND = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
            new Vector3f(0, 0, 0),
            TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, 225, 0)),
            new Vector3f(0.4F, 0.4F, 0.4F),
            null));
    public static final TRSRTransformation GROUND = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
            new Vector3f(0, 0, 0),
            TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, 0, 0)),
            new Vector3f(0.375f, 0.375f, 0.375f),
            null));
    public static final TRSRTransformation FIXED = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
            new Vector3f(0, 0, 0),
            TRSRTransformation.quatFromXYZDegrees(new Vector3f(0, 0, 0)),
            new Vector3f(0.5f, 0.5f, 0.5f),
            null));
    public static final TRSRTransformation GUI = TRSRTransformation.blockCenterToCorner(new TRSRTransformation(
            new Vector3f(0, 0, 0),
            TRSRTransformation.quatFromXYZDegrees(new Vector3f(30, 225, 0)),
            new Vector3f(0.625f, 0.625f, 0.625f),
            null));

    public static final ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation>
            DEFAULT_PERSPECTIVE_TRANSFORMS = new ImmutableMap.Builder<ItemCameraTransforms.TransformType, TRSRTransformation>()
            .put(ItemCameraTransforms.TransformType.THIRD_PERSON_RIGHT_HAND, THIRD_PERSON_RIGHT_HAND)
            .put(ItemCameraTransforms.TransformType.THIRD_PERSON_LEFT_HAND, THIRD_PERSON_LEFT_HAND)
            .put(ItemCameraTransforms.TransformType.FIRST_PERSON_RIGHT_HAND, FIRST_PERSON_RIGHT_HAND)
            .put(ItemCameraTransforms.TransformType.FIRST_PERSON_LEFT_HAND, FIRST_PERSON_LEFT_HAND)
            .put(ItemCameraTransforms.TransformType.GROUND, GROUND)
            .put(ItemCameraTransforms.TransformType.FIXED, FIXED)
            .put(ItemCameraTransforms.TransformType.GUI, GUI)
            .build();

    // An empty list^2 for quads.
    public static final Map<EnumFacing, List<BakedQuad>> EMPTY_FACE_QUADS;
    static {
        EMPTY_FACE_QUADS = Maps.newHashMap();
        for (EnumFacing facing : EnumFacing.VALUES) {
            EMPTY_FACE_QUADS.put(facing, Collections.<BakedQuad>emptyList());
        }
    }

    /**
     * Read the given model location to a {@link net.minecraft.client.renderer.block.model.ModelBlock}.
     * @param modelLocation A model location (without .json suffix)
     * @return The corresponding model.
     * @throws IOException If the model file was invalid.
     */
    public static ModelBlock loadModelBlock(ResourceLocation modelLocation) throws IOException {
        IResource resource = Minecraft.getMinecraft().getResourceManager().getResource(
                new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath() + ".json"));
        Reader reader = new InputStreamReader(resource.getInputStream(), Charsets.UTF_8);
        return ModelBlock.deserialize(reader);
    }

    /**
     * Apply the given overrides to the default transformations.
     * @param overrides The transformations to override.
     * @return The resulting transformation map.
     */
    public static ImmutableMap<ItemCameraTransforms.TransformType, TRSRTransformation> modifyDefaultTransforms(
            Map<ItemCameraTransforms.TransformType, TRSRTransformation> overrides) {
        Map<ItemCameraTransforms.TransformType, TRSRTransformation> transforms =
                Maps.newHashMap(DEFAULT_PERSPECTIVE_TRANSFORMS);
        transforms.putAll(overrides);
        return new ImmutableMap.Builder<ItemCameraTransforms.TransformType, TRSRTransformation>()
                .putAll(transforms).build();

    }

    /**
     * Convert the given color from RGB encoding to BGR encoding.
     * @param color The color in RGB
     * @param alpha The alpha to apply
     * @return The color in BGR
     */
    public static int rgbToBgra(int color, int alpha) {
        Triple<Float, Float, Float> triple = Helpers.intToRGB(color);
        // RGB to BGR
        return Helpers.RGBAToInt(
                (int) (float) (triple.getRight() * 255F), (int) (float) (triple.getMiddle() * 255F), (int) (float) (triple.getLeft() * 255F),
                alpha);
    }

    /**
     * Convert the given color from RGB encoding to BGR encoding.
     * @param color The color in RGB
     * @return The color in BGR
     */
    public static int rgbToBgr(int color) {
        return rgbToBgra(color, 255);
    }
}
