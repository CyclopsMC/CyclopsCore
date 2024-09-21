package org.cyclops.cyclopscore.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

/**
 * Represents an item location relative to a certain inventory extender.
 * @author rubensworks
 */
public record ItemLocation(IInventoryLocation inventoryLocation, int slot) {
    public ItemStack getItemStack(Player player) {
        return this.inventoryLocation().getItemInSlot(player, slot);
    }

    public void setItemStack(Player player, ItemStack itemStack) {
        this.inventoryLocation().setItemInSlot(player, slot, itemStack);
    }

    public static void writeToPacketBuffer(FriendlyByteBuf packetBuffer, ItemLocation location) {
        packetBuffer.writeResourceLocation(location.inventoryLocation().getUniqueName());
        packetBuffer.writeInt(location.slot());
    }

    public static ItemLocation readFromPacketBuffer(FriendlyByteBuf packetBuffer) {
        IInventoryLocation inventoryLocation = InventoryLocations.REGISTRY.get(packetBuffer.readResourceLocation());
        int slot = packetBuffer.readInt();
        return new ItemLocation(inventoryLocation, slot);
    }

}
