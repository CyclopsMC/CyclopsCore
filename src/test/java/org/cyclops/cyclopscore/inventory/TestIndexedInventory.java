package org.cyclops.cyclopscore.inventory;

import com.google.common.collect.Iterators;
import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import org.junit.Test;

import static org.cyclops.cyclopscore.helper.CyclopsMatchers.isIterator;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link IndexedInventory}.
 * @author rubensworks
 */
public class TestIndexedInventory {

    static {
        SharedConstants.setVersion(DetectedVersion.BUILT_IN);
        Bootstrap.bootStrap();
    }

    private static final Item ITEM1 = new ItemDummy();
    private static final Item ITEM2 = new ItemDummy();
    private static final Item ITEM3 = new ItemDummy();

    private static final ItemStack STACK1 = new ItemStack(ITEM1);
    private static final ItemStack STACK2 = new ItemStack(ITEM2);
    private static final ItemStack STACK3 = new ItemStack(ITEM3);

    private static final ItemStack STACK1_1 = new ItemStack(ITEM1);
    private static final ItemStack STACK1_2 = new ItemStack(ITEM1);
    private static final ItemStack STACK1_3 = new ItemStack(ITEM1);

    /* ----- ----- ----- SIZE 0 ----- ----- -----  */
    
    @Test
    public void testSize0Empty() {
        IndexedInventory inv = new IndexedInventory();
        inv.createIndex();

        assertThat("Size is not 0", inv.getContainerSize(), is(0));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray()));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not empty", inv.getIndex().size(), is(0));
    }
    
    /* ----- ----- ----- SIZE 1 ----- ----- -----  */

    @Test
    public void testSize1Empty() {
        IndexedInventory inv = new IndexedInventory(1, 64);
        inv.createIndex();

        assertThat("Size is not 1", inv.getContainerSize(), is(1));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not empty", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize1EmptyRecreateIndex() {
        IndexedInventory inv = new IndexedInventory(1, 64);
        inv.createIndex();

        inv.createIndex();

        assertThat("Size is not 1", inv.getContainerSize(), is(1));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not empty", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize1NonEmpty() {
        IndexedInventory inv = new IndexedInventory(1, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        assertThat("Size is not 1", inv.getContainerSize(), is(1));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray()));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM2), is(false));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM3), is(false));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(not(STACK2)));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(not(STACK3)));
    }

    @Test
    public void testSize1NonEmptyRecreateIndex() {
        IndexedInventory inv = new IndexedInventory(1, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.createIndex();

        assertThat("Size is not 1", inv.getContainerSize(), is(1));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray()));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
    }

    @Test
    public void testSize1NonEmptyFill() {
        IndexedInventory inv = new IndexedInventory(1, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, STACK2);

        assertThat("Size is not 1", inv.getContainerSize(), is(1));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray()));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM2), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).get(0), is(STACK2));
    }

    @Test
    public void testSize1NonEmptyFillRecreateIndex() {
        IndexedInventory inv = new IndexedInventory(1, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, STACK2);

        inv.createIndex();

        assertThat("Size is not 1", inv.getContainerSize(), is(1));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray()));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM2), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).get(0), is(STACK2));
    }

    @Test
    public void testSize1NonEmptyFillAndEmpty1() {
        IndexedInventory inv = new IndexedInventory(1, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, ItemStack.EMPTY);

        assertThat("Size is not 1", inv.getContainerSize(), is(1));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not 0", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize1NonEmptyFillAndEmpty1RecreateIndex() {
        IndexedInventory inv = new IndexedInventory(1, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 1", inv.getContainerSize(), is(1));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not 0", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize1NonEmptyFillAndEmpty2() {
        IndexedInventory inv = new IndexedInventory(1, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, STACK2);

        inv.setItem(0, ItemStack.EMPTY);

        assertThat("Size is not 1", inv.getContainerSize(), is(1));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not 0", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize1NonEmptyFillAndEmpty2RecreateIndex() {
        IndexedInventory inv = new IndexedInventory(1, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, STACK2);

        inv.setItem(0, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 1", inv.getContainerSize(), is(1));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not 0", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize1EmptyEmptyAgain() {
        IndexedInventory inv = new IndexedInventory(1, 64);
        inv.createIndex();

        inv.setItem(0, ItemStack.EMPTY);

        assertThat("Size is not 1", inv.getContainerSize(), is(1));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not empty", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize1EmptyEmptyAgainRecreateIndex() {
        IndexedInventory inv = new IndexedInventory(1, 64);
        inv.createIndex();

        inv.setItem(0, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 1", inv.getContainerSize(), is(1));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not empty", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize1NonEmptyFillAndFillAgainEqual() {
        IndexedInventory inv = new IndexedInventory(1, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, STACK1);

        assertThat("Size is not 1", inv.getContainerSize(), is(1));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray()));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
    }

    @Test
    public void testSize1NonEmptyFillAndFillAgainEqualRecreateIndex() {
        IndexedInventory inv = new IndexedInventory(1, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, STACK1);

        inv.createIndex();

        assertThat("Size is not 1", inv.getContainerSize(), is(1));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray()));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
    }
    
    /* ----- ----- ----- SIZE 3 ----- ----- -----  */

    @Test
    public void testSize3Empty() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not empty", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize3EmptyRecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not empty", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize3NonEmpty() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
    }

    @Test
    public void testSize3NonEmptyRecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
    }

    @Test
    public void testSize3NonEmptyFill() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, STACK2);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM2), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).get(0), is(STACK2));
    }

    @Test
    public void testSize3NonEmptyFillRecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, STACK2);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM2), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).get(0), is(STACK2));
    }

    @Test
    public void testSize3NonEmptyFillAndEmpty1() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, ItemStack.EMPTY);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not 0", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize3NonEmptyFillAndEmpty1RecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not 0", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize3NonEmptyFillAndEmpty2() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, STACK2);

        inv.setItem(0, ItemStack.EMPTY);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not 0", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize3NonEmptyFillAndEmpty2RecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, STACK2);

        inv.setItem(0, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not 0", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize3EmptyEmptyAgain() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, ItemStack.EMPTY);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not empty", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize3EmptyEmptyAgainRecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not empty", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize3NonEmptyFillAndFillAgainEqual() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, STACK1);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
    }

    @Test
    public void testSize3NonEmptyFillAndFillAgainEqualRecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(0, STACK1);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
    }

    @Test
    public void testSize3NonEmptyFillPartial() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0, 1)));

        assertThat("Index is not 2", inv.getIndex().size(), is(2));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM2), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).get(1), is(STACK2));
    }

    @Test
    public void testSize3NonEmptyFillPartialRecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0, 1)));

        assertThat("Index is not 2", inv.getIndex().size(), is(2));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM2), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).get(1), is(STACK2));
    }

    @Test
    public void testSize3NonEmptyFillPartialAndEmpty1_1() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(1, ItemStack.EMPTY);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
    }

    @Test
    public void testSize3NonEmptyFillPartialAndEmpty1_1RecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(1, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
    }

    @Test
    public void testSize3NonEmptyFillPartialAndEmpty1_2() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(1, ItemStack.EMPTY);

        inv.setItem(0, ItemStack.EMPTY);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not 0", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize3NonEmptyFillPartialAndEmpty1_2RecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(1, ItemStack.EMPTY);

        inv.setItem(0, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not 0", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize3NonEmptyFillPartialAndEmpty2_1() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(0, ItemStack.EMPTY);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(1)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM2), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).get(1), is(STACK2));
    }

    @Test
    public void testSize3NonEmptyFillPartialAndEmpty2_1RecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(0, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(1)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM2), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).get(1), is(STACK2));
    }

    @Test
    public void testSize3NonEmptyFillPartialAndEmpty2_2() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(0, ItemStack.EMPTY);

        inv.setItem(1, ItemStack.EMPTY);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not 0", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize3NonEmptyFillPartialAndEmpty2_2RecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(0, ItemStack.EMPTY);

        inv.setItem(1, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not 0", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize3NonEmptyFillFull() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(2, STACK3);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray()));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));

        assertThat("Index is not 3", inv.getIndex().size(), is(3));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM2), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM3), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).get(1), is(STACK2));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM3).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM3).get(2), is(STACK3));
    }

    @Test
    public void testSize3NonEmptyFillFullRecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(2, STACK3);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray()));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));

        assertThat("Index is not 3", inv.getIndex().size(), is(3));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM2), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM3), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).get(1), is(STACK2));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM3).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM3).get(2), is(STACK3));
    }

    @Test
    public void testSize3NonEmptyFillFullAndEmpty1() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(2, STACK3);

        inv.setItem(2, ItemStack.EMPTY);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0, 1)));

        assertThat("Index is not 2", inv.getIndex().size(), is(2));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM2), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).get(1), is(STACK2));
    }

    @Test
    public void testSize3NonEmptyFillFullAndEmpty1RecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(2, STACK3);

        inv.setItem(2, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0, 1)));

        assertThat("Index is not 2", inv.getIndex().size(), is(2));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM2), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).get(1), is(STACK2));
    }

    @Test
    public void testSize3NonEmptyFillFullAndEmpty2_1() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(2, STACK3);

        inv.setItem(1, ItemStack.EMPTY);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(1)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0, 2)));

        assertThat("Index is not 2", inv.getIndex().size(), is(2));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM3), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM3).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM3).get(2), is(STACK3));
    }

    @Test
    public void testSize3NonEmptyFillFullAndEmpty2_1RecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(2, STACK3);

        inv.setItem(1, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(1)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0, 2)));

        assertThat("Index is not 2", inv.getIndex().size(), is(2));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM3), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM3).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM3).get(2), is(STACK3));
    }

    @Test
    public void testSize3NonEmptyFillFullAndEmpty2_1_1() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(2, STACK3);

        inv.setItem(1, ItemStack.EMPTY);

        inv.setItem(0, ItemStack.EMPTY);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(2)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM3), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM3).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM3).get(2), is(STACK3));
    }

    @Test
    public void testSize3NonEmptyFillFullAndEmpty2_1_1RecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(2, STACK3);

        inv.setItem(1, ItemStack.EMPTY);

        inv.setItem(0, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(2)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM3), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM3).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM3).get(2), is(STACK3));
    }

    @Test
    public void testSize3NonEmptyFillFullAndEmpty2_1_2() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(2, STACK3);

        inv.setItem(1, ItemStack.EMPTY);

        inv.setItem(0, ItemStack.EMPTY);

        inv.setItem(2, ItemStack.EMPTY);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not 0", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize3NonEmptyFillFullAndEmpty2_1_2RecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(2, STACK3);

        inv.setItem(1, ItemStack.EMPTY);

        inv.setItem(0, ItemStack.EMPTY);

        inv.setItem(2, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray()));

        assertThat("Index is not 0", inv.getIndex().size(), is(0));
    }

    @Test
    public void testSize3NonEmptyFillFullAndEmpty2_2() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(2, STACK3);

        inv.setItem(1, ItemStack.EMPTY);

        inv.setItem(2, ItemStack.EMPTY);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
    }

    @Test
    public void testSize3NonEmptyFillFullAndEmpty2_2RecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1);

        inv.setItem(1, STACK2);

        inv.setItem(2, STACK3);

        inv.setItem(1, ItemStack.EMPTY);

        inv.setItem(2, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(1, 2)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1));
    }

    @Test
    public void testSize3NonEmptyFillEqualItems() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1_1);

        inv.setItem(1, STACK1_2);

        inv.setItem(2, STACK1_3);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray()));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(3));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1_1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(1), is(STACK1_2));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(2), is(STACK1_3));
    }

    @Test
    public void testSize3NonEmptyFillEqualItemsRecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1_1);

        inv.setItem(1, STACK1_2);

        inv.setItem(2, STACK1_3);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray()));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(3));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1_1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(1), is(STACK1_2));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(2), is(STACK1_3));
    }

    @Test
    public void testSize3NonEmptyFillEqualItemsAndEmpty_1() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1_1);

        inv.setItem(1, STACK1_2);

        inv.setItem(2, STACK1_3);

        inv.setItem(1, ItemStack.EMPTY);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(1)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0, 2)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(2));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1_1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(2), is(STACK1_3));
    }

    @Test
    public void testSize3NonEmptyFillEqualItemsAndEmpty_1RecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1_1);

        inv.setItem(1, STACK1_2);

        inv.setItem(2, STACK1_3);

        inv.setItem(1, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(1)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0, 2)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(2));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1_1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(2), is(STACK1_3));
    }

    @Test
    public void testSize3NonEmptyFillEqualItemsAndEmpty_2() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1_1);

        inv.setItem(1, STACK1_2);

        inv.setItem(2, STACK1_3);

        inv.setItem(1, ItemStack.EMPTY);

        inv.setItem(0, ItemStack.EMPTY);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(2)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(2), is(STACK1_3));
    }

    @Test
    public void testSize3NonEmptyFillEqualItemsAndEmpty_2RecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1_1);

        inv.setItem(1, STACK1_2);

        inv.setItem(2, STACK1_3);

        inv.setItem(1, ItemStack.EMPTY);

        inv.setItem(0, ItemStack.EMPTY);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray(0, 1)));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(2)));

        assertThat("Index is not 1", inv.getIndex().size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(2), is(STACK1_3));
    }

    @Test
    public void testSize3NonEmptyFillEqualItemsMixed() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1_1);

        inv.setItem(1, STACK2);

        inv.setItem(2, STACK1_3);

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray()));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));

        assertThat("Index is not 1", inv.getIndex().size(), is(2));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM2), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(2));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1_1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(2), is(STACK1_3));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).get(1), is(STACK2));
    }

    @Test
    public void testSize3NonEmptyFillEqualItemsMixedRecreateIndex() {
        IndexedInventory inv = new IndexedInventory(3, 64);
        inv.createIndex();

        inv.setItem(0, STACK1_1);

        inv.setItem(1, STACK2);

        inv.setItem(2, STACK1_3);

        inv.createIndex();

        assertThat("Size is not 3", inv.getContainerSize(), is(3));

        assertThat("Empty slots are incorrect", inv.getEmptySlots(), isIterator(Iterators.forArray()));
        assertThat("Non-empty slots are incorrect", inv.getNonEmptySlots(), isIterator(Iterators.forArray(0, 1, 2)));

        assertThat("Index is not 1", inv.getIndex().size(), is(2));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM1), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().containsKey(ITEM2), is(true));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).size(), is(2));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(0), is(STACK1_1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM1).get(2), is(STACK1_3));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).size(), is(1));
        assertThat("Index has incorrect contents", inv.getIndex().get(ITEM2).get(1), is(STACK2));
    }

}
