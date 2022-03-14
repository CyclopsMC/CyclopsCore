package org.cyclops.cyclopscore.recipe.type;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.CraftingInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.fluids.capability.templates.FluidTank;

/**
 * Default implementation of {@link IInventoryFluid}.
 * @author rubensworks
 */
public class InventoryFluid extends CraftingInventory implements IInventoryFluid {

    private final IFluidHandler fluidHandler;

    public InventoryFluid(NonNullList<ItemStack> itemStacks, NonNullList<FluidStack> fluidStacks) {
        super(new Container(null, 0) {
            @Override
            public boolean canInteractWith(PlayerEntity playerIn) {
                return false;
            }
        }, itemStacks.size(), 1);
        int slot = 0;
        for (ItemStack itemStack : itemStacks) {
            setInventorySlotContents(slot++, itemStack);
        }
        if (fluidStacks.size() == 1) {
            this.fluidHandler = new FluidTank(Integer.MAX_VALUE);
            this.fluidHandler.fill(fluidStacks.get(0), IFluidHandler.FluidAction.EXECUTE);
        } else {
            this.fluidHandler = new FluidHandlerListReadOnly(fluidStacks);
        }
    }

    @Override
    public IFluidHandler getFluidHandler() {
        return this.fluidHandler;
    }
}
