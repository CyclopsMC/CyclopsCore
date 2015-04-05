package org.cyclops.cyclopscore.item;

import net.minecraft.block.Block;

/**
 * An extended {@link net.minecraft.item.ItemBlock} that will automatically add information to the blockState
 * item if that blockState implements {@link IInformationProvider}.
 * @author rubensworks
 *
 */
public class ItemBlockExtended extends ItemBlockMetadata {

    /**
     * Make a new instance.
     * @param block The blockState instance.
     */
    public ItemBlockExtended(Block block) {
    	super(block);

    }
    
}
