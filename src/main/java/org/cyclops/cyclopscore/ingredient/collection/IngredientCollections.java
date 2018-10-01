package org.cyclops.cyclopscore.ingredient.collection;

import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.util.Constants;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientMatcher;
import org.cyclops.commoncapabilities.api.ingredient.IIngredientSerializer;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * Helper functions for ingredient collections.
 */
public final class IngredientCollections {

    IngredientCollections() {

    }

    /**
     * Create a new immutable empty collection.
     * @param ingredientComponent The ingredient component the collection should be made for.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return An immutable empty ingredient collection.
     */
    public static <T, M> IIngredientCollection<T, M> emptyCollection(IngredientComponent<T, M> ingredientComponent) {
        return new IngredientCollectionEmpty<>(ingredientComponent);
    }

    /**
     * Check if the two collection are equal by order.
     * @param c1 A first collection.
     * @param c2 A second collection.
     * @return If the two collection are equal by order.
     */
    public static boolean equalsOrdered(IIngredientCollection<?, ?> c1, IIngredientCollection<?, ?> c2) {
        return equalsCheckedOrdered((IIngredientCollection) c1, c2);
    }

    /**
     * Check if the two collection are equal by order using safe types.
     * @param c1 A first collection.
     * @param c2 A second collection.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return If the two collection are equal by order.
     */
    public static <T, M> boolean equalsCheckedOrdered(IIngredientCollection<T, M> c1, IIngredientCollection<T, M> c2) {
        if (c1 == c2) {
            return true;
        }
        if (c1.getComponent() != c2.getComponent() || c1.size() != c2.size()) {
            return false;
        }
        Iterator<T> it1 = c1.iterator();
        Iterator<T> it2 = c2.iterator();
        IIngredientMatcher<T, M> matcher = c1.getComponent().getMatcher();
        while (it1.hasNext()) {
            if (!matcher.matchesExactly(it1.next(), it2.next())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Check if the two maps are equal.
     * @param c1 A first map.
     * @param c2 A second map.
     * @return If the two maps are equal.
     */
    public static boolean equalsMap(IIngredientMap<?, ?, ?> c1, IIngredientMap<?, ?, ?> c2) {
        if (c1 == c2) {
            return true;
        }
        if (c1.getComponent() != c2.getComponent()) {
            return false;
        }
        return equalsMapChecked((IIngredientMap) c1, c2);
    }

    /**
     * Check if the two maps are equal using safe types.
     * @param c1 A first map.
     * @param c2 A second map.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @param <V> The value type.
     * @return If the two maps are equal.
     */
    public static <T, M, V> boolean equalsMapChecked(IIngredientMap<T, M, V> c1, IIngredientMap<T, M, V> c2) {
        if (c1.size() != c2.size()) {
            return false;
        }
        for (Map.Entry<T, V> entry : c1) {
            if (!Objects.equals(c2.get(entry.getKey()), entry.getValue())) {
                return false;
            }
        }
        return true;
    }

    /**
     * Hash the given collection.
     *
     * This will hash each instance in the collection using the component type.
     *
     * @param collection A collection.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return A hashcode.
     */
    public static <T, M> int hash(IIngredientCollection<T, M> collection) {
        int hash = collection.getComponent().hashCode();
        IIngredientMatcher<T, M> matcher = collection.getComponent().getMatcher();
        for (T instance : collection) {
            hash = hash | matcher.hash(instance);
        }
        return hash;
    }

    /**
     * Hash the given map.
     *
     * This will hash each instance in the map using the component type.
     *
     * @param map A map.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @param <V> The type of mapped values.
     * @return A hashcode.
     */
    public static <T, M, V> int hash(IIngredientMap<T, M, V> map) {
        int hash = map.getComponent().hashCode();
        IIngredientMatcher<T, M> matcher = map.getComponent().getMatcher();
        for (Map.Entry<T, V> entry : map.entrySet()) {
            hash = hash | (matcher.hash(entry.getKey()) ^ entry.getValue().hashCode());
        }
        return hash;
    }

    /**
     * Stringifies the given collection.
     *
     * This will create a string by calling the toString() method on each instance in the collection.
     *
     * @param collection A collection.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return A string representation of the collection.
     */
    public static <T, M> String toString(IIngredientCollection<T, M> collection) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        boolean first = true;
        for (T instance : collection) {
            if (first) {
                first = false;
            } else {
                stringBuilder.append(", ");
            }
            stringBuilder.append(instance);
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    /**
     * Stringifies the given map.
     *
     * This will create a string by calling the toString() method on each entry in the map.
     *
     * @param map A map.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @param <V> The type of mapped values.
     * @return A string representation of the map.
     */
    public static <T, M, V> String toString(IIngredientMap<T, M, V> map) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("[");
        boolean first = true;
        for (Map.Entry<T, V> entry : map) {
            if (first) {
                first = false;
            } else {
                stringBuilder.append(", ");
            }
            stringBuilder.append("{");
            stringBuilder.append(entry.getKey().toString());
            stringBuilder.append(",");
            stringBuilder.append(entry.getValue().toString());
            stringBuilder.append("}");
        }
        stringBuilder.append("]");
        return stringBuilder.toString();
    }

    /**
     * Created a sorted collection using the given comparator and all instances from the given collection.
     * @param collection A collection to create a sorted copy of.
     * @param comparator A compatator.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return A sorted collection based on the given collection and comparator.
     */
    public static <T, M> IngredientArrayList<T, M> sort(IIngredientCollection<T, M> collection,
                                                        Comparator<? super T> comparator) {
        T[] a = collection.toArray();
        Arrays.sort(a, comparator);
        return new IngredientArrayList<>(collection.getComponent(), a);
    }

    /**
     * Serialize the given collection to an NBT tag.
     * The type of collection will be lost,
     * only the component type and the ingredients will be saved.
     *
     * @param collection An ingredient collection.
     * @param <T> The instance type.
     * @param <M> The matching condition parameter.
     * @return An NBT tag.
     */
    public static <T, M> NBTTagCompound serialize(IIngredientCollection<T, M> collection) {
        NBTTagCompound tag = new NBTTagCompound();

        IngredientComponent<T, M> component = collection.getComponent();
        tag.setString("component", component.getName().toString());

        NBTTagList list = new NBTTagList();
        IIngredientSerializer<T, M> serializer = component.getSerializer();
        for (T ingredient : collection) {
            list.appendTag(serializer.serializeInstance(ingredient));
        }
        tag.setTag("ingredients", list);

        return tag;
    }

    /**
     * Deserialize the given NBT tag to an ingredient array list.
     *
     * @param tag An NBT tag.
     * @param ingredientCollectionFactory A function that creates a {@link IIngredientCollectionMutable}
     *                                    from an {@link IngredientComponent}.
     * @param <C> The collection type.
     * @return An ingredient collection.
     * @throws IllegalArgumentException If the tag was invalid.
     */
    public static <C extends IIngredientCollectionMutable<?, ?>> C deserialize(NBTTagCompound tag,
                                                                               IIngredientCollectionConstructor<C>
                                                                                       ingredientCollectionFactory) {
        // Validate tag
        if (!tag.hasKey("component", Constants.NBT.TAG_STRING)) {
            throw new IllegalArgumentException("No component type was found in the given tag");
        }
        if (!tag.hasKey("ingredients", Constants.NBT.TAG_LIST)) {
            throw new IllegalArgumentException("No ingredients list was found in the given tag");
        }

        // Validate component
        String componentTypeName = tag.getString("component");
        IngredientComponent<?, ?> component = IngredientComponent.REGISTRY.getValue(new ResourceLocation(componentTypeName));
        if (component == null) {
            throw new IllegalArgumentException("No ingredient component with the given name was found: " + component);
        }

        // Actual deserialization of ingredients
        IIngredientSerializer<?, ?> serializer = component.getSerializer();
        NBTTagList ingredients = (NBTTagList) tag.getTag("ingredients");
        C collection = ingredientCollectionFactory.create(component);
        IIngredientCollectionMutable collectionUnsafe = collection;
        for (NBTBase subTag : ingredients) {
            collectionUnsafe.add(serializer.deserializeInstance(subTag));
        }

        return collection;
    }

    /**
     * Deserialize the given NBT tag to an ingredient array list.
     *
     * @param tag An NBT tag.
     * @return An ingredient array list.
     * @throws IllegalArgumentException If the tag was invalid.
     */
    public static IngredientArrayList<?, ?> deserialize(NBTTagCompound tag) {
        return deserialize(tag, IngredientArrayList::new);
    }

    /**
     * Helper interface for constructing an {@link IIngredientCollection} based on an {@link IngredientComponent}.
     * @param <C>
     */
    public static interface IIngredientCollectionConstructor<C extends IIngredientCollection<?, ?>> {
        public <T, M> C create(IngredientComponent<T, M> ingredientComponent);
    }

}
