package org.cyclops.cyclopscore.inventory;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;

/**
 * @author rubensworks
 */
public class TestIndexedInventoryData {

    public static final Item ITEM1 = new ItemDummy();
    public static final Item ITEM2 = new ItemDummy();
    public static final Item ITEM3 = new ItemDummy();

    static {
//        try {
//            Field field = MappedRegistry.class.getDeclaredField("unregisteredIntrusiveHolders");
//            field.setAccessible(true);
//            Map<Item, Holder.Reference<Item>> delegates = ((Map<Item, Holder.Reference<Item>>) field
//                    .get(BuiltInRegistries.ITEM));
//
//            delegates.put(ITEM1, Holder.Reference.createIntrusive(BuiltInRegistries.ITEM, ITEM1));
//            delegates.put(ITEM2, Holder.Reference.createIntrusive(BuiltInRegistries.ITEM, ITEM2));
//            delegates.put(ITEM3, Holder.Reference.createIntrusive(BuiltInRegistries.ITEM, ITEM3));
//        } catch (IllegalAccessException | NoSuchFieldException e) {
//            e.printStackTrace();
//        }
    }

    public static final ItemStack STACK_EMPTY = ItemStack.EMPTY;

    public static final ItemStack STACK1 = new ItemStack(ITEM1);
    public static final ItemStack STACK2 = new ItemStack(ITEM2);
    public static final ItemStack STACK3 = new ItemStack(ITEM3);

    public static final ItemStack STACK1_1 = new ItemStack(ITEM1);
    public static final ItemStack STACK1_2 = new ItemStack(ITEM1);
    public static final ItemStack STACK1_3 = new ItemStack(ITEM1);

}
