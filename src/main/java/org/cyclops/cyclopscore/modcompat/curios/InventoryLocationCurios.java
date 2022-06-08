package org.cyclops.cyclopscore.modcompat.curios;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandlerModifiable;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.inventory.IInventoryLocation;
import top.theillusivec4.curios.api.CuriosApi;

/**
 * @author rubensworks
 */
public class InventoryLocationCurios implements IInventoryLocation {

    @Override
    public ResourceLocation getUniqueName() {
        return new ResourceLocation(Reference.MOD_ID, "curios");
    }

    @Override
    public IItemHandlerModifiable getInventory(Player player) {
        return CuriosApi.getCuriosHelper().getEquippedCurios(player).orElse(null);
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
