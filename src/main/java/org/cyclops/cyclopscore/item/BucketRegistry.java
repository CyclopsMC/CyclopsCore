package org.cyclops.cyclopscore.item;

import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.RayTraceResult;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.FillBucketEvent;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fml.common.eventhandler.Event.Result;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import java.util.Map;

/**
 * This will take care of the logic of custom buckets, so they can be filled like other buckets.
 * @author rubensworks
 *
 */
public class BucketRegistry implements IBucketRegistry {

    private final Map<Block, Item> items = Maps.newHashMap();
    private final Map<Item, FluidStack> fluidStacks = Maps.newHashMap();

    @Override
    public void registerBucket(Block block, Item item) {
        items.put(block, item);
    }

    @Override
    public void registerBucket(Item item, FluidStack fluidStack) {
        fluidStacks.put(item, fluidStack);
    }

    @Override
    public Map<Block, Item> getBlockItem() {
        return items;
    }

    @Override
    public Map<Item, FluidStack> getItemFluidStack() {
        return fluidStacks;
    }

    @Override
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onBucketFill(FillBucketEvent event) {
        ItemStack result = fillCustomBucket(event.getWorld(), event.getTarget(), event.getEmptyBucket());
        if (result != null) {
            event.setFilledBucket(result);
            event.setResult(Result.ALLOW);
        }
    }

    private ItemStack fillCustomBucket(World world, RayTraceResult pos, ItemStack current) {
        Block block = world.getBlockState(pos.getBlockPos()).getBlock();

        Item bucket = items.get(block);
        if (bucket != null && world.getBlockState(pos.getBlockPos()) == block.getDefaultState() &&
                ItemStack.areItemStacksEqual(current, bucket.getContainerItem(current))) {
            world.setBlockToAir(pos.getBlockPos());
            return new ItemStack(bucket);
        } else {
            return null;
        }
    }
}
