package org.cyclops.cyclopscore.inventory;

import com.google.common.collect.Maps;
import net.minecraft.DetectedVersion;
import net.minecraft.SharedConstants;
import net.minecraft.server.Bootstrap;
import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import org.junit.Test;

import java.util.Map;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.CoreMatchers.sameInstance;
import static org.junit.Assert.assertThat;

/**
 * Unit tests for {@link SimpleInventoryCommon}.
 *
 * Inventories are mutable and must be identified by instance.
 * Item transfer APIs cache their wrappers in maps keyed on the container,
 * so comparing inventories by contents makes two separate chests share one wrapper,
 * and a hash code that changes on every modification makes such a map lose its entries.
 *
 * @author rubensworks
 */
public class TestSimpleInventoryCommon {

    static {
        SharedConstants.setVersion(DetectedVersion.BUILT_IN);
        Bootstrap.bootStrap();
    }

    @Test
    public void testEqualsIsIdentityBased() {
        SimpleInventoryCommon inv1 = new SimpleInventoryCommon(5, 64);
        SimpleInventoryCommon inv2 = new SimpleInventoryCommon(5, 64);

        assertThat("Inventory does not equal itself", inv1.equals(inv1), is(true));
        assertThat("Two empty inventories of equal size are equal", inv1.equals(inv2), is(false));

        inv1.setItem(0, new ItemStack(Items.APPLE));
        inv2.setItem(0, new ItemStack(Items.APPLE));
        assertThat("Two inventories with equal contents are equal", inv1.equals(inv2), is(false));
    }

    @Test
    public void testHashCodeIsStableAcrossModifications() {
        SimpleInventoryCommon inv = new SimpleInventoryCommon(5, 64);

        int hashCodeBefore = inv.hashCode();
        inv.setItem(0, new ItemStack(Items.APPLE));
        inv.setItem(1, new ItemStack(Items.DIRT));
        inv.setItem(0, ItemStack.EMPTY);

        assertThat("Hash code changed after modifying the inventory", inv.hashCode(), is(hashCodeBefore));
    }

    @Test
    public void testStateStillChangesOnModification() {
        SimpleInventoryCommon inv = new SimpleInventoryCommon(5, 64);

        int stateBefore = inv.getState();
        inv.setItem(0, new ItemStack(Items.APPLE));

        assertThat("State did not change after modifying the inventory", inv.getState(), is(not(stateBefore)));
    }

    @Test
    public void testDistinctInventoriesGetDistinctMapEntries() {
        SimpleInventoryCommon inv1 = new SimpleInventoryCommon(5, 64);
        SimpleInventoryCommon inv2 = new SimpleInventoryCommon(5, 64);

        // This is how item transfer APIs such as Fabric's InventoryStorage cache their wrappers
        Map<Container, Object> wrappers = Maps.newHashMap();
        Object wrapper1 = wrappers.computeIfAbsent(inv1, i -> new Object());
        Object wrapper2 = wrappers.computeIfAbsent(inv2, i -> new Object());
        assertThat("Two inventories share a single cached wrapper", wrapper1, is(not(sameInstance(wrapper2))));

        // Looking up an inventory again after it changed must still find its own wrapper
        inv1.setItem(0, new ItemStack(Items.APPLE));
        assertThat("Inventory lost its cached wrapper after being modified",
                wrappers.computeIfAbsent(inv1, i -> new Object()), is(sameInstance(wrapper1)));
    }
}
