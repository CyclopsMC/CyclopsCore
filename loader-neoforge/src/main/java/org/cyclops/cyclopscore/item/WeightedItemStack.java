package org.cyclops.cyclopscore.item;

import com.google.common.collect.Lists;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Set;

/**
 * An ItemStack that has a certain weight that can be used to occur with a certain chance.
 *
 * @author rubensworks
 */
public class WeightedItemStack {

    @Nullable
    private final ItemStack itemStack;
    private final int weight;

    public WeightedItemStack(@Nullable ItemStack itemStack, int weight) {
        this.itemStack = itemStack;
        this.weight = weight;
    }

    /**
     * Create a weighted list of the given input items.
     *
     * @param input The set of unique weighted itemstacks.
     * @return A list which can be used to get random weighted itemstacks.
     */
    public static List<WeightedItemStack> createWeightedList(Set<WeightedItemStack> input) {
        List<WeightedItemStack> trueList = Lists.newLinkedList();
        for (WeightedItemStack itemStack : input) {
            for (int i = 0; i < itemStack.getWeight(); i++) {
                trueList.add(itemStack);
            }
        }
        return trueList;
    }

    /**
     * Get a random item from the given list.
     *
     * @param list   The list, generated from {@link WeightedItemStack#createWeightedList}
     * @param random A random instance.
     * @return A random item.
     */
    public static WeightedItemStack getRandomWeightedItemStack(List<WeightedItemStack> list, RandomSource random) {
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Get a copy of the itemstack with a randomized stacksize.
     * The original itemstack size represent the maximum stacksize +1.
     *
     * @param random A random instance.
     * @return A new itemstack.
     */
    public ItemStack getItemStackWithRandomizedSize(RandomSource random) {
        if (getItemStack() == null || getItemStack().isEmpty()) {
            return ItemStack.EMPTY;
        }
        ItemStack itemStack = getItemStack().copy();
        itemStack.setCount(random.nextInt(itemStack.getCount()) + 1);
        return itemStack;
    }

    @Override
    public String toString() {
        return "{ItemStack: " + itemStack + "; Weight: " + weight + "}";
    }

    @Nullable
    public ItemStack getItemStack() {
        return this.itemStack;
    }

    public int getWeight() {
        return this.weight;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof WeightedItemStack)) return false;
        final WeightedItemStack other = (WeightedItemStack) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$itemStack = this.getItemStack();
        final Object other$itemStack = other.getItemStack();
        if (this$itemStack == null ? other$itemStack != null : !this$itemStack.equals(other$itemStack)) return false;
        if (this.getWeight() != other.getWeight()) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof WeightedItemStack;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $itemStack = this.getItemStack();
        result = result * PRIME + ($itemStack == null ? 43 : $itemStack.hashCode());
        result = result * PRIME + this.getWeight();
        return result;
    }
}
