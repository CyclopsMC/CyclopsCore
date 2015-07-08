package org.cyclops.cyclopscore.item;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.cyclopscore.init.IRegistry;

import java.util.Map;

/**
 * This will take care of the logic of custom buckets, so they can be filled like other buckets.
 * @author rubensworks
 */
public interface IBucketRegistry extends IRegistry {

    /**
     * Register a fluid block to a bucket item.
     * @param block The fluid block.
     * @param item The bucket item.
     */
    public void registerBucket(Block block, Item item);

    /**
     * Register a bucket item to a fluidstack.
     * @param item The bucket item.
     * @param fluidStack The fluidstack.
     */
    public void registerBucket(Item item, FluidStack fluidStack);

    /**
     * @return The mapping from block to item.
     */
    public Map<Block, Item> getBlockItem();

    /**
     * @return The mapping from item to fluidstack.
     */
    public Map<Item, FluidStack> getItemFluidStack();

    /**
     * Called when player right clicks with an empty bucket on a fluid blockState.
     * Make sure to annotate this with {@link SubscribeEvent}
     * @param event The Forge event required for this.
     */
    public void onBucketFill(FillBucketEvent event);

}
