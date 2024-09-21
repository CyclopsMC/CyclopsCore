package org.cyclops.cyclopscore.inventory;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.cyclops.cyclopscore.Reference;

/**
 * @author rubensworks
 */
public class InventoryLocationPlayer implements IInventoryLocation {

    private static InventoryLocationPlayer INSTANCE = new InventoryLocationPlayer();

    public static InventoryLocationPlayer getInstance() {
        return INSTANCE;
    }

    private InventoryLocationPlayer() {

    }

    @Override
    public ResourceLocation getUniqueName() {
        return ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "player");
    }
    @Override
    public IInventoryCommonModifiable getInventory(Player player) {
        return new InventoryCommonModifiableContainer(player.getInventory());
    }

    @Override
    public ItemStack getItemInSlot(Player player, int slot) {
        return player.getInventory().getItem(slot);
    }

    @Override
    public void setItemInSlot(Player player, int slot, ItemStack itemStack) {
        player.getInventory().setItem(slot, itemStack);
    }

    public ItemLocation handToLocation(Player player, InteractionHand hand, int selectedSlot) {
        int slot;
        if (hand == InteractionHand.MAIN_HAND) {
            slot = selectedSlot;
        } else {
            // Last slot in Inventory compartments
            slot = 36 + 4 + 1 - 1;
        }
        return new ItemLocation(this, slot);
    }

}
