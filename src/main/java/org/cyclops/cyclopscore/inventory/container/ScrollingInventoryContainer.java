package org.cyclops.cyclopscore.inventory.container;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.InventoryPlayer;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.inventory.IGuiContainerProvider;
import scala.actors.threadpool.Arrays;

import java.util.List;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

/**
 * An inventory container that has a scrollbar and searchfield.
 * Terminology:
 *      row: The row index from visible elements.
 *      elementIndex: The element index in all available elements
 *      visible: Currently on-screen by the user, maximum amount of elements is determined by the pageSize
 *      filtered: All items that are browsable by the user, might be more than the pageSize allows what leads to a scrollbar.
 *      unfiltered: All items, pattern searching will happen in this list.
 * @author rubensworks
 */
public abstract class ScrollingInventoryContainer<E> extends ExtendedInventoryContainer {

    private final List<E> unfilteredItems;
    private List<Pair<Integer, E>> filteredItems; // Pair: original index - item
    private final List<E> visibleItems;
    private final IItemPredicate<E> itemSearchPredicate;

    /**
     * Make a new instance.
     *
     * @param inventory   The player inventory.
     * @param guiProvider The gui provider.
     * @param items       All items to potentially show in this list.
     * @param filterer    The predicate that is used to filter on the given items.
     */
    @SuppressWarnings("unchecked")
    public ScrollingInventoryContainer(InventoryPlayer inventory, IGuiContainerProvider guiProvider, List<E> items,
                                       IItemPredicate<E> filterer) {
        super(inventory, guiProvider);
        this.unfilteredItems = Lists.newArrayList(items);
        this.filteredItems = Lists.newLinkedList();
        this.visibleItems = Arrays.asList(new Object[getPageSize()]);
        for(int i = 0; i < getPageSize(); i++) {
            this.visibleItems.set(i, null);
        }
        this.itemSearchPredicate = filterer;
    }

    protected List<E> getUnfilteredItems() {
        return this.unfilteredItems;
    }

    protected List<Pair<Integer, E>> getFilteredItems() {
        return this.filteredItems;
    }

    public int getUnfilteredItemCount() {
        return getUnfilteredItems().size();
    }

    public int getFilteredItemCount() {
        return getFilteredItems().size();
    }

    /**
     * @return The maximum amount of columns to show.
     */
    public int getColumns() {
        return 1;
    }

    /**
     * Scroll to the given relative position.
     * @param scroll A value between 0 and 1.
     */
    public void scrollTo(float scroll) {
        onScroll();
        int rows = (getFilteredItemCount() + getColumns() - 1) / getColumns() - getPageSize();
        int firstRow = (int)((double)(scroll * (float)rows) + 0.5D);
        if(firstRow < 0) firstRow = 0;

        for(int i = 0; i < getPageSize(); i++) {
            for(int j = 0; j < getColumns(); j++) {
                int index = i * getColumns() + j;
                int elementIndex = index + firstRow;
                this.visibleItems.set(index, null);
                if(elementIndex < getFilteredItemCount()) {
                    Pair<Integer, E> filteredItem = getFilteredItems().get(elementIndex);
                    enableElementAt(index, filteredItem.getLeft(), filteredItem.getRight());
                }
            }
        }
    }

    protected void onScroll() {

    }

    /**
     * @return The allowed page size.
     */
    public abstract int getPageSize();

    /**
     * After scrolling, this will be called to make items visible.
     * @param row The row to show the given element at.
     * @param elementIndex The absolute element index.
     * @param element The element to show.
     */
    protected void enableElementAt(int row, int elementIndex, E element) {
        this.visibleItems.set(row, element);
    }

    /**
     * Check if the given element is visible.
     * @param row The row the the given element is at.
     * @return If it is visible.
     */
    public boolean isElementVisible(int row) {
        return row < getPageSize() && getVisibleElement(row) != null;
    }

    /**
     * Get the currently visible element at the given row.
     * @param row The row the the given element is at.
     * @return The elements
     */
    public E getVisibleElement(int row) {
        if(row >= visibleItems.size()) return null;
        return this.visibleItems.get(row);
    }

    /**
     * Update the filtered items.
     * @param searchString The input string to search by.
     */
    public void updateFilter(String searchString) {
        Pattern pattern;
        try {
            pattern = Pattern.compile(".*" + searchString.toLowerCase() + ".*");
        } catch (PatternSyntaxException e) {
            pattern = Pattern.compile(".*");
        }
        this.filteredItems = filter(unfilteredItems, itemSearchPredicate, pattern);
        scrollTo(0); // Reset scroll, will also refresh items on-screen.
    }

    protected static <E> List<Pair<Integer, E>> filter(List<E> input, IItemPredicate<E> predicate, Pattern pattern) {
        List<Pair<Integer, E>> filtered = Lists.newLinkedList();
        int i = 0;
        for(E item : input) {
            if(predicate.apply(item, pattern)) {
                filtered.add(Pair.of(i, item));
            }
            i++;
        }
        return filtered;
    }

    /**
     * Predicate for matching items used to search.
     * @param <E> The type of item.
     */
    public static interface IItemPredicate<E> {

        /**
         * Check if the given item matches a string pattern.
         * @param item The item to check.
         * @param pattern The pattern to check.
         * @return If the item matches
         */
        public boolean apply(E item, Pattern pattern);

    }

}
