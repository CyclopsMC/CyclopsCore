package org.cyclops.cyclopscore.modcompat.baubles;

import lazy.baubles.api.BaublesAPI;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.inventory.IInventoryLocation;

import javax.annotation.Nullable;

/**
 * Extends the iteratable inventory with Baubles slots.
 */
public class InventoryLocationBaubles implements IInventoryLocation {

    @Override
    public ResourceLocation getUniqueName() {
        return new ResourceLocation(Reference.MOD_ID, "baubles");
    }

    @Override
    @Nullable
    public IItemHandlerModifiable getInventory(Player player) {
        return BaublesAPI.getBaublesHandler(player).orElse(null);
    }

    @Override
    public ItemStack getItemInSlot(Player player, int slot) {
        return BaublesAPI.getBaublesHandler(player)
                .map(handler -> handler.getStackInSlot(slot))
                .orElse(ItemStack.EMPTY);
    }

    @Override
    public void setItemInSlot(Player player, int slot, ItemStack itemStack) {
        BaublesAPI.getBaublesHandler(player)
                .ifPresent(handler -> handler.setStackInSlot(slot, itemStack));
    }
}
