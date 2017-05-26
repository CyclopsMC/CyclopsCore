package org.cyclops.cyclopscore.recipe;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.world.World;
import net.minecraftforge.oredict.OreDictionary;
import net.minecraftforge.oredict.ShapelessOreRecipe;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An NBT-sensitive variant of {@link ShapelessOreRecipe}
 * @author rubensworks
 */
public class ShapelessOreRecipeNbtSensitive extends ShapelessOreRecipe {

    private final boolean nbtSensitive;

    public ShapelessOreRecipeNbtSensitive(Block result, boolean nbtSensitive, Object... recipe) {
        super(result, recipe);
        this.nbtSensitive = nbtSensitive;
    }

    public ShapelessOreRecipeNbtSensitive(Item result, boolean nbtSensitive, Object... recipe) {
        super(result, recipe);
        this.nbtSensitive = nbtSensitive;
    }

    public ShapelessOreRecipeNbtSensitive(ItemStack result, boolean nbtSensitive, Object... recipe) {
        super(result, recipe);
        this.nbtSensitive = nbtSensitive;
    }

    // Copied from superclass, and modified
    @SuppressWarnings("unchecked")
    @Override
    public boolean matches(InventoryCrafting var1, World world) {
        NonNullList<Object> required = NonNullList.create();
        required.addAll(input);

        for (int x = 0; x < var1.getSizeInventory(); x++)
        {
            ItemStack slot = var1.getStackInSlot(x);

            if (!slot.isEmpty())
            {
                boolean inRecipe = false;
                Iterator<Object> req = required.iterator();

                while (req.hasNext())
                {
                    boolean match = false;

                    Object next = req.next();

                    if (next instanceof ItemStack)
                    {
                        match = ShapelessOreRecipeNbtSensitive.matches(nbtSensitive, (ItemStack) next, slot);
                    }
                    else if (next instanceof List)
                    {
                        Iterator<ItemStack> itr = ((List<ItemStack>)next).iterator();
                        while (itr.hasNext() && !match)
                        {
                            match = ShapelessOreRecipeNbtSensitive.matches(nbtSensitive, itr.next(), slot);
                        }
                    }

                    if (match)
                    {
                        inRecipe = true;
                        required.remove(next);
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

    public static boolean matches(boolean nbtSensitive, ItemStack target, ItemStack input) {
        return OreDictionary.itemMatches(target, input, false)
                && (!nbtSensitive || (target.hasTagCompound() == input.hasTagCompound()
                    && (!target.hasTagCompound() || target.getTagCompound().equals(input.getTagCompound()))));
    }
}
