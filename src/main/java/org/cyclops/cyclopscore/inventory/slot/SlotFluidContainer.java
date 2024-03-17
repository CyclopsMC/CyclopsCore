package org.cyclops.cyclopscore.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidUtil;
import net.neoforged.neoforge.fluids.capability.IFluidHandlerItem;
import org.cyclops.cyclopscore.helper.FluidHelpers;

import javax.annotation.Nullable;
import java.util.Optional;

/**
 * Slots that will accept buckets and fluid containers.
 * @author rubensworks
 *
 */
public class SlotFluidContainer extends Slot {

    @Nullable
    private final Fluid fluid;

    public SlotFluidContainer(Container inventory, int index, int x, int y, @Nullable Fluid fluid) {
        super(inventory, index, x, y);
        this.fluid = fluid;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return checkIsItemValid(itemStack, fluid);
    }

    public static boolean checkIsItemValid(ItemStack itemStack, @Nullable Fluid fluid) {
        if (!itemStack.isEmpty()) {
            Optional<IFluidHandlerItem> fluidHandler = FluidUtil.getFluidHandler(itemStack);
            return fluidHandler.map(h -> {
                if (fluid == null) {
                    return true;
                }
                for (int i = 0; i < h.getTanks(); i++) {
                    if (h.isFluidValid(0, new FluidStack(fluid, 1))
                            || h.isFluidValid(0, new FluidStack(fluid, FluidHelpers.BUCKET_VOLUME))) {
                        return true;
                    }
                }
                return false;
            }).orElse(false);
        }

        return false;
    }

}
