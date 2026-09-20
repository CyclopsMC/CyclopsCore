package org.cyclops.cyclopscore.modcompat.curios;

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.EmptyResourceHandler;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.item.ItemUtil;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.inventory.IInventoryCommonModifiable;
import org.cyclops.cyclopscore.inventory.IInventoryLocation;
import org.cyclops.cyclopscore.inventory.InventoryModifiableResourceHandlerNeoForge;
import top.theillusivec4.curios.api.CuriosCapability;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class InventoryLocationCurios implements IInventoryLocation {

    @Override
    public Identifier getUniqueName() {
        return Identifier.fromNamespaceAndPath(Reference.MOD_ID, "curios");
    }

    @Nullable
    protected static ResourceHandler<ItemResource> getCuriosHandler(Player player) {
        return player.getCapability(CuriosCapability.ITEM_HANDLER, null);
    }

    @Override
    public IInventoryCommonModifiable getInventory(Player player) {
        ResourceHandler<ItemResource> handler = getCuriosHandler(player);
        return new InventoryModifiableResourceHandlerNeoForge(
                handler == null ? EmptyResourceHandler.instance() : handler);
    }

    @Override
    public ItemStack getItemInSlot(Player player, int slot) {
        ResourceHandler<ItemResource> handler = getCuriosHandler(player);
        return handler == null ? ItemStack.EMPTY : ItemUtil.getStack(handler, slot);
    }

    @Override
    public void setItemInSlot(Player player, int slot, ItemStack itemStack) {
        ResourceHandler<ItemResource> handler = getCuriosHandler(player);
        if (handler != null) {
            new InventoryModifiableResourceHandlerNeoForge(handler).setStackInSlot(slot, itemStack);
        }
    }

}
