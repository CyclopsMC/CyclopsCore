package org.cyclops.cyclopscore.inventory.container;

import com.google.common.collect.Lists;
import net.minecraft.entity.player.InventoryPlayer;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.inventory.IGuiContainerProvider;

import java.util.Arrays;
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
 * TODO: rewrite with GuiScrollBar
 */
public abstract class ScrollingInventoryContainer<E> extends ExtendedInventoryContainer {

    private final List<E> unfilteredItems;
    private List<Pair<Integer, E>> filteredItems; // Pair: original index - item
    private final List<E> visibleItems;
    private final IItemPredicate<E> itemSearchPredicate;
    private String lastSearchString = "";
    private int firstElement = 0;

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
        this.visibleItems = (List<E>) Arrays.asList(new Object[getPageSize() * getColumns()]);
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

    public int getFirstElement() {
        return this.firstElement;
    }

    /**
     * @return The maximum amount of columns to show.
     */
    public int getColumns() {
        return 1;
    }

    /**
     * @return The stepsize for scrolling.
     */
    public int getScrollStepSize() {
        return getColumns();
    }

    /**
     * Scroll to the given relative position.
     * @param scroll A value between 0 and 1.
     */
    public void scrollTo(float scroll) {
        onScroll();
        int elements = (getFilteredItemCount() + getColumns() - 1) - getPageSize() * getColumns();
        firstElement = (int)((double)(scroll * (float)elements) + 0.5D);
        firstElement -= firstElement % getScrollStepSize();
        if(firstElement < 0) firstElement = 0;
        for(int i = 0; i < getPageSize(); i++) {
            for(int j = 0; j < getColumns(); j++) {
                int index = i * getColumns() + j;
                int elementIndex = index + firstElement;
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
     * @param visibleIndex The visible item index.
     * @param elementIndex The absolute element index.
     * @param element The element to show.
     */
    protected void enableElementAt(int visibleIndex, int elementIndex, E element) {
        this.visibleItems.set(visibleIndex, element);
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

    public void refreshFilter() {
        updateFilter(lastSearchString);
    }

    /**
     * Update the filtered items.
     * @param searchString The input string to search by.
     */
    public void updateFilter(String searchString) {
        this.lastSearchString = searchString;
        Pattern pattern;
        try {
            pattern = Pattern.compile(".*" + searchString.toLowerCase() + ".*");
        } catch (PatternSyntaxException e) {
            pattern = Pattern.compile(".*");
        }
        this.filteredItems = filter(getUnfilteredItems(), itemSearchPredicate, pattern);
        scrollTo(0); // Reset scroll, will also refresh items on-screen.
    }

    protected List<Pair<Integer, E>> filter(List<E> input, IItemPredicate<E> predicate, Pattern pattern) {
        List<Pair<Integer, E>> filtered = Lists.newLinkedList();
        int i = 0;
        for(E item : input) {
            if(predicate.apply(item, pattern) && additionalApplies(item)) {
                filtered.add(Pair.of(i, item));
            }
            i++;
        }
        return filtered;
    }

    /**
     * An additional conditional that can be added for filtering items.
     * @param item The item to check.
     * @return If the item should be shown.
     */
    protected boolean additionalApplies(E item) {
        return true;
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
