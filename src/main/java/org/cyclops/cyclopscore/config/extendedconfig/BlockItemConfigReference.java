package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.item.Item;
import org.cyclops.cyclopscore.config.CyclopsCoreConfigException;
import org.cyclops.cyclopscore.init.IObjectReference;

/**
 * Reference to an item.
 * @author rubensworks
 */
public class BlockItemConfigReference implements IObjectReference<Item> {

    private final Class<? extends BlockConfig> blockConfigClass;
    private BlockConfig blockConfig = null;

    public BlockItemConfigReference(Class<? extends BlockConfig> blockConfigClass) {
        this.blockConfigClass = blockConfigClass;
    }

    @Override
    public Item getObject() {
        if(blockConfig == null) {
            try {
                blockConfig = (BlockConfig) blockConfigClass.getField("_instance").get(null);
            } catch (IllegalAccessException | NoSuchFieldException | ClassCastException e) {
                e.printStackTrace();
                throw new CyclopsCoreConfigException("Something went wrong while materializating the reference to " +
                        blockConfigClass.getName());
            }
        }
        return blockConfig != null ? Item.getItemFromBlock(blockConfig.getBlockInstance()) : null;
    }
}
