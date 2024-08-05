package org.cyclops.cyclopscore.item;

import com.google.common.collect.Sets;
import net.minecraft.world.item.Item;

import java.util.Set;

/**
 * This is responsible for adding "show more information" tooltips to registered items.
 * @author rubensworks
 */
public class ItemInformationProviderCommon {

    protected static final Set<Item> ITEMS_INFO = Sets.newIdentityHashSet();

    public static void registerItem(Item item) {
        ITEMS_INFO.add(item);
    }

}
