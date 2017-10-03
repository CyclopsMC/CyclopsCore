package org.cyclops.cyclopscore.recipe.custom.component;

import lombok.Data;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;

/**
 * A {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe} component (input, output or properties) that holds an
 * {@link net.minecraftforge.fluids.FluidStack}.
 *
 * @author immortaleeb
 */
@Data
public class FluidStackRecipeComponent implements IRecipeInput, IRecipeOutput, IRecipeProperties, IFluidStackRecipeComponent {
    private final FluidStack fluidStack;
    private float chance;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof FluidStackRecipeComponent)) return false;
        FluidStackRecipeComponent that = (FluidStackRecipeComponent)object;

        if (this.fluidStack != null) {
            return this.fluidStack.getFluid() != null && that.fluidStack != null && this.fluidStack.getFluid().equals(that.fluidStack.getFluid());
        }

        return that.fluidStack == null;
    }

    @Override
    public int hashCode() {
        return fluidStack != null ? fluidStack.getFluid().hashCode() + 90 : 0;
    }
}
