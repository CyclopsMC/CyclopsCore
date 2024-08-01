package org.cyclops.commoncapabilities.api.capability.itemhandler;

import net.minecraft.core.component.DataComponentMap;
import net.minecraft.world.item.ItemStack;

import java.util.Comparator;

/**
 * Item matching flags to be used in {@link ISlotlessItemHandler}.
 * @author rubensworks
 */
public final class ItemMatch {

    /**
     * Convenience value matching any ItemStack.
     */
    public static final int ANY = 0;
    /**
     * Match ItemStack items.
     */
    public static final int ITEM = 1;
    /**
     * Match ItemStack data components.
     */
    public static final int DATA = 4;
    /**
     * Match ItemStack stacksizes.
     */
    public static final int STACKSIZE = 8;
    /**
     * Convenience value matching ItemStacks exactly by item, data component and stacksize.
     */
    public static final int EXACT = ITEM | DATA | STACKSIZE;

    /**
     * A comparator for data components. (This is set in GeneralConfig)
     */
    public static Comparator<DataComponentMap> DATA_COMPARATOR;

    public static boolean areItemStacksEqual(ItemStack a, ItemStack b, int matchFlags) {
        if (matchFlags == ANY) {
            return true;
        }
        boolean item      = (matchFlags & ITEM     ) > 0;
        boolean nbt       = (matchFlags & DATA) > 0;
        boolean stackSize = (matchFlags & STACKSIZE) > 0;
        return a == b || a.isEmpty() && b.isEmpty() ||
                (!a.isEmpty() && !b.isEmpty()
                        && (!item || a.getItem() == b.getItem())
                        && (!stackSize || a.getCount() == b.getCount())
                        && (!nbt || areItemStackDataComponentsEqual(a, b)));
    }

    public static boolean areItemStackDataComponentsEqual(ItemStack a, ItemStack b) {
        DataComponentMap tagA = a.getComponents();
        DataComponentMap tagB = b.getComponents();
        if (tagA.isEmpty() && tagB.isEmpty()) {
            return true;
        } else {
            if (tagA == null) {
                tagA = DataComponentMap.EMPTY;
            }
            if (tagB == null) {
                tagB = DataComponentMap.EMPTY;
            }
            return DATA_COMPARATOR.compare(tagA, tagB) == 0;
            // We don't include a.areCapsCompatible(b), because we expect that differing caps have different NBT tags.
        }
    }

}
