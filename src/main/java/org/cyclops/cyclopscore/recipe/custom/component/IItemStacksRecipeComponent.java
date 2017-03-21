package org.cyclops.cyclopscore.recipe.custom.component;

import net.minecraft.item.ItemStack;

import java.util.List;

/**
 * Interface for recipe components that hold a {@link ItemStack}s.
 * @author immortaleeb
 */
public interface IItemStacksRecipeComponent {
    /**
     * @return Returns the ItemStack held by this recipe component.
     */
    public List<ItemStack> getItemStacks();
}
