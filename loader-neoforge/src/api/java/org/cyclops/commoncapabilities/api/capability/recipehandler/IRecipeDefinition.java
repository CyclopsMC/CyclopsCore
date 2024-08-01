package org.cyclops.commoncapabilities.api.capability.recipehandler;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.nbt.ByteArrayTag;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.nbt.ListTag;
import net.minecraft.resources.ResourceLocation;
import org.cyclops.commoncapabilities.api.ingredient.IMixedIngredients;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Defines the inputs and outputs of a recipe.
 * Inputs are ingredient prototypes for ingredient component types.
 * Outputs are exact instances for ingredient component types.
 *
 * Implementing classes should properly implement the equals and hashCode methods.
 *
 * @author rubensworks
 */
public interface IRecipeDefinition extends Comparable<IRecipeDefinition> {

    /**
     * @return The input ingredient component types.
     */
    public Set<IngredientComponent<?, ?>> getInputComponents();

    /**
     * Get the input prototypes of a certain type.
     *
     * The first list contains a list of ingredients,
     * whereas the deeper second list contains different prototype-based alternatives for the ingredient at this position.
     *
     * @param ingredientComponent An ingredient component type.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter, may be Void.
     * @return Input prototypes.
     */
    public <T, M> List<IPrototypedIngredientAlternatives<T, M>> getInputs(IngredientComponent<T, M> ingredientComponent);

    /**
     * If the input at the given index is reusable.
     * If an ingredient is reusable, this means that a crafting job for this recipe will not (fully) consume this
     * ingredient, and could potentially be reused in later crafting jobs.
     * @param ingredientComponent An ingredient component type.
     * @param index The index of an input, based on the order in {@link #getInputs(IngredientComponent)}.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter, may be Void.
     * @return If the input at this index is reusable.
     */
    public <T, M> boolean isInputReusable(IngredientComponent<T, M> ingredientComponent, int index);

    /**
     * @return The output ingredients.
     */
    public IMixedIngredients getOutput();

    /**
     * Deserialize a recipe to NBT.
     * @param recipe A recipe.
     * @return An NBT representation of the given recipe.
     */
    public static CompoundTag serialize(IRecipeDefinition recipe) {
        CompoundTag tag = new CompoundTag();
        CompoundTag inputTag = new CompoundTag();
        CompoundTag inputReusableTag = new CompoundTag();
        for (IngredientComponent<?, ?> component : recipe.getInputComponents()) {
            ListTag instances = new ListTag();
            List<Byte> reusableBytes = Lists.newArrayList();
            int index = 0;
            for (IPrototypedIngredientAlternatives ingredient : recipe.getInputs(component)) {
                CompoundTag subTag = new CompoundTag();
                IPrototypedIngredientAlternatives.ISerializer serializer = ingredient.getSerializer();
                subTag.put("val", serializer.serialize(component, ingredient));
                subTag.putByte("type", serializer.getId());
                instances.add(subTag);
                reusableBytes.add((byte) (recipe.isInputReusable(component, index) ? 1 : 0));
                index++;
            }
            String componentName = IngredientComponent.REGISTRY.getKey(component).toString();
            inputTag.put(componentName, instances);
            inputReusableTag.put(componentName, new ByteArrayTag(reusableBytes));
        }
        tag.put("input", inputTag);
        tag.put("inputReusable", inputReusableTag);
        tag.put("output", IMixedIngredients.serialize(recipe.getOutput()));
        return tag;
    }

    /**
     * Deserialize a recipe from NBT
     * @param tag An NBT tag.
     * @return A new mixed recipe instance.
     * @throws IllegalArgumentException If the given tag is invalid or does not contain data on the given recipe.
     */
    public static RecipeDefinition deserialize(CompoundTag tag) throws IllegalArgumentException {
        Map<IngredientComponent<?, ?>, List<IPrototypedIngredientAlternatives<?, ?>>> inputs = Maps.newIdentityHashMap();
        Map<IngredientComponent<?, ?>, List<Boolean>> inputsReusable = Maps.newIdentityHashMap();
        if (!tag.contains("input")) {
            throw new IllegalArgumentException("A recipe tag did not contain a valid input tag");
        }
        if (!tag.contains("output")) {
            throw new IllegalArgumentException("A recipe tag did not contain a valid output tag");
        }

        CompoundTag inputTag = tag.getCompound("input");
        for (String componentName : inputTag.getAllKeys()) {
            IngredientComponent<?, ?> component = IngredientComponent.REGISTRY.get(ResourceLocation.parse(componentName));
            if (component == null) {
                throw new IllegalArgumentException("Could not find the ingredient component type " + componentName);
            }
            Tag subTag = inputTag.get(componentName);
            if (!(subTag instanceof ListTag)) {
                throw new IllegalArgumentException("The ingredient component type " + componentName + " did not contain a valid list of instances");
            }
            ListTag instancesTag = (ListTag) subTag;
            List<IPrototypedIngredientAlternatives<?, ?>> instances = Lists.newArrayList();
            for (Tag instanceTag : instancesTag) {
                IPrototypedIngredientAlternatives.ISerializer alternativeSerializer;
                Tag deserializeTag;
                if (instanceTag instanceof CompoundTag) {
                    CompoundTag instanceTagCompound = (CompoundTag) instanceTag;
                    byte type = instanceTagCompound.getByte("type");
                    alternativeSerializer = IPrototypedIngredientAlternatives.SERIALIZERS.get(type);
                    if (alternativeSerializer == null) {
                        throw new IllegalArgumentException("Could not find a prototyped ingredient alternative serializer for id " + type);
                    }
                    deserializeTag = ((CompoundTag) instanceTag).get("val");
                } else {
                    throw new IllegalArgumentException("The ingredient component type " + componentName + " did not contain a valid reference to instances");
                }
                IPrototypedIngredientAlternatives alternatives = alternativeSerializer.deserialize(component, deserializeTag);
                instances.add(alternatives);
            }
            inputs.put(component, instances);
        }

        if (tag.contains("inputReusable")) {
            CompoundTag inputReusableTag = tag.getCompound("inputReusable");
            for (String componentName : inputReusableTag.getAllKeys()) {
                IngredientComponent<?, ?> component = IngredientComponent.REGISTRY.get(ResourceLocation.parse(componentName));
                if (component == null) {
                    throw new IllegalArgumentException("Could not find the ingredient component type " + componentName);
                }
                Tag subTag = inputReusableTag.get(componentName);
                if (!(subTag instanceof ByteArrayTag instancesReusable)) {
                    throw new IllegalArgumentException("The ingredient component type " + componentName + " did not contain a valid list of instance reusable bytes");
                }
                List<Boolean> inputReusable = Lists.newArrayList();
                for (byte b : instancesReusable.getAsByteArray()) {
                    inputReusable.add(b == (byte)1);
                }
                inputsReusable.put(component, inputReusable);
            }
        }

        IMixedIngredients output = IMixedIngredients.deserialize(tag.getCompound("output"));

        return new RecipeDefinition(inputs, inputsReusable, output);
    }

}
