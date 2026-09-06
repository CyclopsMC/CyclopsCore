package org.cyclops.cyclopscore.helper;

import net.minecraft.core.Holder;
import net.minecraft.core.MappedRegistry;
import net.minecraft.core.component.DataComponentMap;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.inventory.ItemDummy;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Unit tests for the ItemStack hash code of {@link ItemStackHelpersCommon}.
 *
 * The hash has to take data components into account. Ingredient collections key on it, and a
 * component-blind hash puts every stack of the same item in one bucket, which turns their
 * lookups into scans over full component comparisons.
 *
 * @author rubensworks
 */
public class TestItemStackHelpersHashCode {

    static {
        ((MappedRegistry) BuiltInRegistries.ITEM).unfreeze(true);
    }

    private static final Item ITEM1 = new ItemDummy();
    private static final Item ITEM2 = new ItemDummy();

    static {
        ((Holder.Reference<Item>) ITEM1.builtInRegistryHolder()).bindComponents(DataComponentMap.EMPTY);
        ((Holder.Reference<Item>) ITEM2.builtInRegistryHolder()).bindComponents(DataComponentMap.EMPTY);
    }

    private static final IItemStackHelpers HELPERS = new ItemStackHelpersNeoForge();

    private static int hash(ItemStack stack) {
        return HELPERS.getItemStackHashCode(stack);
    }

    private static ItemStack named(Item item, int count, String name) {
        ItemStack stack = new ItemStack(item, count);
        stack.set(DataComponents.CUSTOM_NAME, Component.literal(name));
        return stack;
    }

    @Test
    public void testEqualStacksHashEqual() {
        assertThat(hash(new ItemStack(ITEM1)), is(hash(new ItemStack(ITEM1))));
        assertThat(hash(new ItemStack(ITEM1, 7)), is(hash(new ItemStack(ITEM1, 7))));
        assertThat(hash(named(ITEM1, 3, "a")), is(hash(named(ITEM1, 3, "a"))));
        assertThat(hash(ItemStack.EMPTY), is(hash(ItemStack.EMPTY)));
    }

    @Test
    public void testDifferentItemsHashDifferently() {
        assertThat(hash(new ItemStack(ITEM1)), is(not(hash(new ItemStack(ITEM2)))));
    }

    @Test
    public void testDifferentCountsHashDifferently() {
        assertThat(hash(new ItemStack(ITEM1, 1)), is(not(hash(new ItemStack(ITEM1, 2)))));
    }

    /**
     * The regression this guards: stacks of one item differing only by components used to share a hash.
     */
    @Test
    public void testComponentsAffectHash() {
        assertThat(hash(named(ITEM1, 1, "a")), is(not(hash(named(ITEM1, 1, "b")))));
        assertThat(hash(new ItemStack(ITEM1)), is(not(hash(named(ITEM1, 1, "a")))));
    }

    /**
     * A single differing hash could be luck. Over a sample of same-item stacks the hash has to
     * spread, or hash-based collections degrade to linear scans.
     */
    @Test
    public void testComponentVariantsSpreadOverManyBuckets() {
        int samples = 1000;
        Set<Integer> hashes = new HashSet<>();
        for (int i = 0; i < samples; i++) {
            hashes.add(hash(named(ITEM1, 1, "variant " + i)));
        }
        assertThat("Component variants of one item have to produce distinct hashes",
                hashes.size() > samples * 0.99, is(true));
    }

    /**
     * Stacks carrying no component patch skip the component hash, so this pins that they still
     * spread over items and counts, and still agree with equality.
     */
    @Test
    public void testPlainStacksSpreadOverItemsAndCounts() {
        assertThat(hash(new ItemStack(ITEM1, 1)), is(not(hash(new ItemStack(ITEM2, 1)))));
        assertThat(hash(new ItemStack(ITEM1, 1)), is(not(hash(new ItemStack(ITEM1, 2)))));
        assertThat(hash(new ItemStack(ITEM1, 5)), is(hash(new ItemStack(ITEM1, 5))));
    }

    /**
     * A stack whose only component is set back to its default carries no patch any more, so it is
     * equal to the plain stack and has to hash like one.
     */
    @Test
    public void testComponentSetBackToDefaultHashesAsPlain() {
        ItemStack plain = new ItemStack(ITEM1);
        ItemStack restored = new ItemStack(ITEM1);
        restored.set(DataComponents.CUSTOM_NAME, Component.literal("a"));
        assertThat(hash(restored), is(not(hash(plain))));
        restored.set(DataComponents.CUSTOM_NAME, null);
        assertThat(ItemStack.isSameItemSameComponents(plain, restored), is(true));
        assertThat(hash(restored), is(hash(plain)));
    }

    /**
     * The hash may never distinguish two stacks that count as equal, or lookups miss.
     */
    @Test
    public void testHashIsConsistentWithComponentEquality() {
        for (int i = 0; i < 100; i++) {
            ItemStack a = named(ITEM1, 1 + (i % 5), "variant " + i);
            ItemStack b = named(ITEM1, 1 + (i % 5), "variant " + i);
            assertThat(ItemStack.isSameItemSameComponents(a, b), is(true));
            assertThat(a.getCount(), is(b.getCount()));
            assertThat(hash(a), is(hash(b)));
        }
    }
}
