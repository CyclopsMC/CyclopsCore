package org.cyclops.cyclopscore.ingredient.collection;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.ListIterator;
import java.util.Spliterator;
import java.util.Spliterators;

/**
 * An ingredient collection using list semantics.
 * This means that instances exist in a predefined order and that instances can exist multiple time in the collection.
 *
 * @see List
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 */
public interface IIngredientList<T, M> extends IIngredientCollection<T, M> {

    /**
     * Get the instance at the given index.
     * @param index An index.
     * @return The instance at the given index.
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    @Nullable
    T get(int index);

    /**
     * Set the instance at the given index.
     * @param index An index.
     * @param instance The instance to set.
     * @return The previous instance at the given position.
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    @Nullable
    T set(int index, T instance);

    /**
     * Insert the given instance at the given position
     * and shift all instances after it (if any) to the right (add one to their index).
     * @param index An index.
     * @param instance The instances to add.
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt; size()</tt>)
     */
    void add(int index, T instance);

    /**
     * Remove the instance at the given index.
     * All following instances (with higher index; if any) will be shifted to the left (subtract one from their index).
     * @param index An index.
     * @return The removed index if one existed.
     * @throws IndexOutOfBoundsException if the index is out of range
     *         (<tt>index &lt; 0 || index &gt;= size()</tt>)
     */
    @Nullable
    T remove(int index);

    /**
     * The first index that has an instance that equals the given instance.
     * @param instance An instance.
     * @return The first matching instance's index, or -1 if none.
     */
    int firstIndexOf(T instance);

    /**
     * The last index that has an instance that equals the given instance.
     * @param instance An instance.
     * @return The last matching instance's index, or -1 if none.
     */
    int lastIndexOf(T instance);

    /**
     * @return A list iterator over all instances.
     */
    ListIterator<T> listIterator();

    /**
     * Get the offsetted list index.
     * @param offset An index to start iterating from.
     * @return A list iterator that is offsetted by `offset` instances.
     * @throws IndexOutOfBoundsException for an illegal endpoint index value
     *         (<tt>fromIndex &lt; 0 || toIndex &gt; size ||
     *         fromIndex &gt; toIndex</tt>)
     */
    ListIterator<T> listIterator(int offset);

    /**
     * Get a view of a sublist of the this list.
     * As this is a view, any mutations on it will reflect on this list as well.
     * @param fromIndex The starting index (inclusive).
     * @param toIndex The ending index (exclusive).
     * @return The sublist view.
     * @throws IndexOutOfBoundsException for an illegal endpoint index value
     *         (<tt>fromIndex &lt; 0 || toIndex &gt; size</tt>)
     * @throws IllegalArgumentException if fromIndex &gt; toIndex
     */
    IIngredientList<T, M> subList(int fromIndex, int toIndex);

    /**
     * Sort this list based on the given comparator.
     * @param comparator A comparator.
     */
    default void sort(Comparator<? super T> comparator) {
        T[] a = this.toArray();
        Arrays.sort(a, comparator);
        ListIterator<T> it = this.listIterator();
        for (T e : a) {
            it.next();
            it.set(e);
        }
    }

    @Override
    default Spliterator<T> spliterator() {
        return Spliterators.spliterator(this.iterator(), this.size(), Spliterator.ORDERED);
    }

}
