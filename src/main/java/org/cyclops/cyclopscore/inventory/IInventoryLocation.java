package org.cyclops.cyclopscore.inventory;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;

import javax.annotation.Nullable;

/**
 * A registerable inventory location.
 */
public interface IInventoryLocation {

    public ResourceLocation getUniqueName();

    @Nullable
    public IItemHandlerModifiable getInventory(Player player);

    public ItemStack getItemInSlot(Player player, int slot);

    public void setItemInSlot(Player player, int slot, ItemStack itemStack);

}
