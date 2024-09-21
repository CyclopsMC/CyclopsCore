package org.cyclops.cyclopscore.modcompat.curios;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.items.wrapper.EmptyItemHandler;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.inventory.IInventoryCommonModifiable;
import org.cyclops.cyclopscore.inventory.IInventoryLocation;
import org.cyclops.cyclopscore.inventory.InventoryCommonModifiableContainerNeoForge;
import top.theillusivec4.curios.api.CuriosApi;
import top.theillusivec4.curios.api.type.capability.ICuriosItemHandler;

/**
 * @author rubensworks
 */
public class InventoryLocationCurios implements IInventoryLocation {

    @Override
    public ResourceLocation getUniqueName() {
        return ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "curios");
    }

    @Override
    public IInventoryCommonModifiable getInventory(Player player) {
        return new InventoryCommonModifiableContainerNeoForge(CuriosApi.getCuriosInventory(player)
                .map(ICuriosItemHandler::getEquippedCurios)
                .orElseGet(EmptyItemHandler::new));
    }

    @Override
    public ItemStack getItemInSlot(Player player, int slot) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(player)
                .map(handler -> handler.getStackInSlot(slot))
                .orElse(ItemStack.EMPTY);
    }

    @Override
    public void setItemInSlot(Player player, int slot, ItemStack itemStack) {
        CuriosApi.getCuriosHelper().getEquippedCurios(player)
                .ifPresent(handler -> handler.setStackInSlot(slot, itemStack));
    }

}
