package org.cyclops.cyclopscore.recipe.type;

import net.minecraft.core.NonNullList;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.TransientCraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.fluid.FluidStacksResourceHandler;

/**
 * Default implementation of {@link IInventoryFluid}.
 * @author rubensworks
 */
public class InventoryFluid extends TransientCraftingContainer implements IInventoryFluid {

    private final ResourceHandler<FluidResource> fluidHandler;

    public InventoryFluid(NonNullList<ItemStack> itemStacks, NonNullList<FluidStack> fluidStacks) {
        super(new AbstractContainerMenu(null, 0) {
            @Override
            public ItemStack quickMoveStack(Player p_38941_, int p_38942_) {
                return ItemStack.EMPTY;
            }

            @Override
            public boolean stillValid(Player playerIn) {
                return false;
            }
        }, itemStacks.size(), 1);
        int slot = 0;
        for (ItemStack itemStack : itemStacks) {
            setItem(slot++, itemStack);
        }
        this.fluidHandler = new FluidStacksResourceHandler(fluidStacks, Integer.MAX_VALUE);
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < getFluidHandler().size(); i++) {
            if (!getFluidHandler().getResource(i).isEmpty()) {
                return false;
            }
        }
        return super.isEmpty();
    }

    @Override
    public ResourceHandler<FluidResource> getFluidHandler() {
        return this.fluidHandler;
    }

    @Override
    public int size() {
        return getContainerSize();
    }
}
