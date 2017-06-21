package org.cyclops.cyclopscore.recipe;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.NonNullList;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.Iterator;

/**
 * An NBT-sensitive variant of {@link ShapelessOreRecipe}
 * @author rubensworks
 */
public class ShapelessOreRecipeNbtSensitive extends ShapelessOreRecipe {

    private final boolean nbtSensitive;

    public ShapelessOreRecipeNbtSensitive(ResourceLocation group, Block result, boolean nbtSensitive, Object... recipe) {
        super(group, result, recipe);
        this.nbtSensitive = nbtSensitive;
    }

    public ShapelessOreRecipeNbtSensitive(ResourceLocation group, Item result, boolean nbtSensitive, Object... recipe) {
        super(group, result, recipe);
        this.nbtSensitive = nbtSensitive;
    }

    public ShapelessOreRecipeNbtSensitive(ResourceLocation group, ItemStack result, boolean nbtSensitive, Object... recipe) {
        super(group, result, recipe);
        this.nbtSensitive = nbtSensitive;
    }

    // Copied from superclass, and modified
    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(InventoryCrafting var1, World world) {
        NonNullList<Ingredient> required = NonNullList.create();
        required.addAll(input);

        for (int x = 0; x < var1.getSizeInventory(); x++)
        {
            ItemStack slot = var1.getStackInSlot(x);

            if (!slot.isEmpty())
            {
                boolean inRecipe = false;
                Iterator<Ingredient> req = required.iterator();

                while (req.hasNext())
                {
                    Ingredient ingredient = req.next();
                    if (ingredient.apply(slot) && matches(nbtSensitive, ingredient, slot))
                    {
                        inRecipe = true;
                        req.remove();
                        break;
                    }
                }

                if (!inRecipe)
                {
                    return false;
                }
            }
        }

        return required.isEmpty();
    }

    public static boolean matches(boolean nbtSensitive, Ingredient target, ItemStack input) {
        for (ItemStack itemStack : target.getMatchingStacks()) {
            if ((!nbtSensitive || (input.hasTagCompound() == itemStack.hasTagCompound()
                    && (!input.hasTagCompound() || input.getTagCompound().equals(itemStack.getTagCompound()))))) {
                return true;
            }
        }
        return false;
    }
}
