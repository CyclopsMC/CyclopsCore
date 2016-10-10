package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.CyclopsCoreConfigException;
import org.cyclops.cyclopscore.init.IObjectReference;

/**
 * Reference to an item.
 * @author rubensworks
 */
public class ItemConfigReference implements IObjectReference<Item> {

    private final Class<? extends ItemConfig> itemConfigClass;
    private ItemConfig itemConfig = null;

    public ItemConfigReference(Class<? extends ItemConfig> itemConfigClass) {
        this.itemConfigClass = itemConfigClass;
    }

    @Override
    public Item getObject() {
        if(itemConfig == null) {
            try {
                itemConfig = (ItemConfig) itemConfigClass.getField("_instance").get(null);
            } catch (IllegalAccessException | NoSuchFieldException | ClassCastException e) {
                e.printStackTrace();
                throw new CyclopsCoreConfigException("Something went wrong while materializating the reference to " +
                        itemConfigClass.getName());
            }
        }
        return itemConfig != null ? itemConfig.getItemInstance() : null;
    }
}
