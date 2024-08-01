package org.cyclops.commoncapabilities.api.ingredient;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * A collection of ingredient instances of different types.
 * @author rubensworks
 */
public interface IMixedIngredients extends Comparable<IMixedIngredients> {

    /**
     * @return The ingredient component types.
     */
    public Set<IngredientComponent<?, ?>> getComponents();

    /**
     * Get the instances of a certain type.
     * @param ingredientComponent An ingredient component type.
     * @param <T> The instance type.
     * @return Instances.
     */
    public <T> List<T> getInstances(IngredientComponent<T, ?> ingredientComponent);

    /**
     * Get the first non-empty instance of the given type.
     * @param ingredientComponent An ingredient component type.
     * @param <T> The instance type.
     * @return The first non-empty instance, or the empty instance if none could be found.
     */
    public default <T> T getFirstNonEmpty(IngredientComponent<T, ?> ingredientComponent) {
        IIngredientMatcher<T, ?> matcher = ingredientComponent.getMatcher();
        for (T instance : getInstances(ingredientComponent)) {
            if (!matcher.isEmpty(instance)) {
                return instance;
            }
        }
        return matcher.getEmptyInstance();
    }

    /**
     * Check if at least all ingredients from the supplied mixed ingredients are contained in this mixed ingredients.
     * This mixed ingredients could contain more ingredients.
     * @param that The ingredients to look for.
     * @return If at least all ingredients in that are contained in this.
     */
    public default boolean containsAll(IMixedIngredients that) {
        if (!this.getComponents().containsAll(that.getComponents())) {
            return false;
        }

        for (IngredientComponent<?, ?> component : that.getComponents()) {
            List<?> thisInstances = Lists.newArrayList(this.getInstances(component));
            List<?> thatInstances = that.getInstances(component);
            IIngredientMatcher matcher = component.getMatcher();

            for (Object thatInstance : thatInstances) {
                boolean found = false;
                Iterator<?> it = thisInstances.iterator();
                while (it.hasNext() && !found) {
                    if (matcher.matchesExactly(thatInstance, it.next())) {
                        found = true;
                        it.remove();
                    }
                }

                if (!found) {
                    return false;
                }
            }
        }

        return true;
    }

    /**
     * @return If at least one non-empty ingredient is present.
     */
    public default boolean isEmpty() {
        for (IngredientComponent<?, ?> component : getComponents()) {
            IIngredientMatcher matcher = component.getMatcher();
            for (Object instance : getInstances(component)) {
                if (!matcher.isEmpty(instance)) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Deserialize ingredients to NBT.
     * @param ingredients Ingredients.
     * @return An NBT representation of the given ingredients.
     */
    public static CompoundTag serialize(IMixedIngredients ingredients) {
        CompoundTag tag = new CompoundTag();
        for (IngredientComponent<?, ?> component : ingredients.getComponents()) {
            ListTag instances = new ListTag();
            IIngredientSerializer serializer = component.getSerializer();
            for (Object instance : ingredients.getInstances(component)) {
                instances.add(serializer.serializeInstance(instance));
            }
            tag.put(IngredientComponent.REGISTRY.getKey(component).toString(), instances);
        }
        return tag;
    }

    /**
     * Deserialize ingredients from NBT
     * @param tag An NBT tag.
     * @return A new mixed ingredients instance.
     * @throws IllegalArgumentException If the given tag is invalid or does not contain data on the given ingredients.
     */
    public static MixedIngredients deserialize(CompoundTag tag) throws IllegalArgumentException {
        Map<IngredientComponent<?, ?>, List<?>> ingredients = Maps.newIdentityHashMap();
        for (String componentName : tag.getAllKeys()) {
            IngredientComponent<?, ?> component = IngredientComponent.REGISTRY.get(ResourceLocation.parse(componentName));
            if (component == null) {
                throw new IllegalArgumentException("Could not find the ingredient component type " + componentName);
            }
            Tag subTag = tag.get(componentName);
            if (!(subTag instanceof ListTag)) {
                throw new IllegalArgumentException("The ingredient component type " + componentName + " did not contain a valid list of instances");
            }
            ListTag instancesTag = (ListTag) subTag;
            IIngredientSerializer serializer = component.getSerializer();
            List instances = Lists.newArrayList();
            for (Tag instanceTag : instancesTag) {
                instances.add(serializer.deserializeInstance(instanceTag));
            }
            ingredients.put(component, instances);
        }
        return new MixedIngredients(ingredients);
    }

}
