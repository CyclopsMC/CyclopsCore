package org.cyclops.cyclopscore.inventory;

import it.unimi.dsi.fastutil.ints.Int2ObjectMap;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

import java.util.Map;
import java.util.PrimitiveIterator;

/**
 * @author rubensworks
 */
public interface IInventoryIndexReference {

    public int getInventoryReferenceStackLimit(); // Named like this due to conflict with obfuscated vanilla method name

    public Map<Item, Int2ObjectMap<ItemStack>> getIndex();

    public PrimitiveIterator.OfInt getEmptySlots();

    public PrimitiveIterator.OfInt getNonEmptySlots();

}
