package org.cyclops.cyclopscore.inventory.slot;

import net.minecraft.world.Container;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

/**
 * Slot that is used for only accepting an item matching the given ingredient.
 *
 * @author rubensworks
 */
public class SlotSingleIngredient extends SlotExtended {

    private Ingredient ingredient;

    /**
     * Make a new instance.
     *
     * @param inventory  The inventory this slot will be in.
     * @param index      The index of this slot.
     * @param x          X coordinate.
     * @param y          Y coordinate.
     * @param ingredient The ingredient to accept.
     */
    public SlotSingleIngredient(Container inventory, int index, int x, int y, Ingredient ingredient) {
        super(inventory, index, x, y);
        this.ingredient = ingredient;
    }

    @Override
    public boolean mayPlace(ItemStack itemStack) {
        return super.mayPlace(itemStack) && ingredient.test(itemStack);
    }

    public Ingredient getIngredientAllowed() {
        return ingredient;
    }
}
