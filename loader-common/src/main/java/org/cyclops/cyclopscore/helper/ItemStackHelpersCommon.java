package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.cyclops.cyclopscore.inventory.PlayerExtendedInventoryIterator;

import java.util.Random;

/**
 * @author rubensworks
 */
public abstract class ItemStackHelpersCommon implements IItemStackHelpers {

    private static final Random RANDOM = new Random();

    @Override
    public void spawnItemStack(Level world, BlockPos pos, ItemStack itemStack) {
        spawnItemStack(world, pos.getX(), pos.getY(), pos.getZ(), itemStack);
    }

    @Override
    public void spawnItemStack(Level world, double x, double y, double z, ItemStack itemStack) {
        float offsetX = RANDOM.nextFloat() * 0.8F + 0.1F;
        float offsetY = RANDOM.nextFloat() * 0.8F + 0.1F;
        float offsetZ = RANDOM.nextFloat() * 0.8F + 0.1F;

        while (itemStack.getCount() > 0) {
            int i = RANDOM.nextInt(21) + 10;

            if (i > itemStack.getCount()) {
                i = itemStack.getCount();
            }

            ItemStack dropStack = itemStack.copy();
            itemStack.shrink(i);
            dropStack.setCount(i);
            ItemEntity entityitem = new ItemEntity(world, x + (double)offsetX, y + (double)offsetY,
                    z + (double)offsetZ, dropStack);

            float motion = 0.05F;
            entityitem.setDeltaMovement(
                    RANDOM.nextGaussian() * (double)motion,
                    RANDOM.nextGaussian() * (double)motion + 0.2D,
                    RANDOM.nextGaussian() * (double)motion);
            world.addFreshEntity(entityitem);
        }
    }

    @Override
    public void spawnItemStackToPlayer(Level world, BlockPos pos, ItemStack stack, Player player) {
        if (!world.isClientSide() && !stack.isEmpty()) {
            float f = 0.5F;

            double xo = (double)(world.getRandom().nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double yo = (double)(world.getRandom().nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            double zo = (double)(world.getRandom().nextFloat() * f) + (double)(1.0F - f) * 0.5D;
            ItemEntity entityitem = new ItemEntity(world, (double)pos.getX() + xo, (double)pos.getY() + yo, (double)pos.getZ() + zo, stack);

            double d0 = 8.0D;
            double d1 = (player.getX() - entityitem.getX()) / d0;
            double d2 = (player.getY() + (double)player.getEyeHeight() - entityitem.getY()) / d0;
            double d3 = (player.getZ() - entityitem.getZ()) / d0;

            entityitem.setDeltaMovement(entityitem.getDeltaMovement().add(d1, d2, d3));
            entityitem.setNoPickUpDelay();
            world.addFreshEntity(entityitem);
        }
    }

    @Override
    public boolean hasPlayerItem(Player player, Item item) {
        for (PlayerExtendedInventoryIterator it = new PlayerExtendedInventoryIterator(player); it.hasNext();) {
            ItemStack itemStack = it.next();
            if (itemStack != null && itemStack.getItem() == item) {
                return true;
            }
        }
        return false;
    }

    @Override
    public ItemStack parseItemStack(String itemStackString) {
        String[] split = itemStackString.split(":");
        String itemName = split[0] + ":" + split[1];
        Item item =  BuiltInRegistries.ITEM.getValue(Identifier.parse(itemName));
        if(item == null) {
            throw new IllegalArgumentException("Invalid ItemStack item: " + itemName);
        }
        int amount = 1;
        if(split.length > 2) {
            try {
                amount = Integer.parseInt(split[2]);
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Invalid ItemStack amount: " + split[2]);
            }
        }
        return new ItemStack(item, amount);
    }

    @Override
    public int getItemStackHashCode(ItemStack stack) {
        if(stack.isEmpty()) return ItemStack.EMPTY.hashCode();
        int result = 1;
        result = 37 * result + stack.getCount();
        result = 37 * result + stack.getItem().hashCode();
        // Data components have to be part of the hash, because equality compares them.
        // Leaving them out makes every stack of the same item hash alike, so hash-based
        // ingredient collections collapse into one bucket per item and every lookup turns
        // into a scan doing full component comparisons. This is what made large storage
        // networks scale quadratically. The exclusion dates from NBT tags, which were
        // expensive to hash; component maps are not.
        //
        // Only stacks carrying a patch need it. A component map hashes its prototype alongside its
        // patch, and the prototype is the item's defaults, which the item hashed above already
        // stands for. Hashing it again distinguishes nothing, and it is the dominant cost for the
        // plain stacks that most of a storage network consists of.
        //
        // This stays consistent with equality because the patch is kept sanitized: setting a
        // component to its default removes it from the patch rather than storing it, so two stacks
        // of one item have equal components exactly when they have equal patches.
        if (hasComponentPatch(stack)) {
            result = 37 * result + stack.getComponents().hashCode();
        }
        // Not factoring in capability compatibility. Doing so would require either reflection (slow)
        // or an access transformer, it's highly unlikely that it'd be the only difference between
        // many ItemStacks in practice, and occasional hash code collisions are okay.
        return result;
    }

    /**
     * If the given stack carries data components that differ from its item's defaults.
     *
     * Only those have to take part in {@link #getItemStackHashCode(ItemStack)}.
     *
     * @param stack A non-empty stack.
     * @return If the stack has a non-empty component patch.
     */
    protected boolean hasComponentPatch(ItemStack stack) {
        // Vanilla exposes the patch only by building one. That costs nothing for the stacks this
        // is here to catch, as an empty patch yields the shared empty instance. Loaders that can
        // answer without building anything should override this.
        return !stack.getComponentsPatch().isEmpty();
    }
}
