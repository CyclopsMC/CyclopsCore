package org.cyclops.cyclopscore.recipe;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.Ingredient;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.oredict.ShapedOreRecipe;

/**
 * An NBT-sensitive variant of {@link ShapedOreRecipe}
 * @author rubensworks
 */
public class ShapedOreRecipeNbtSensitive extends ShapedOreRecipe {

    private final boolean nbtSensitive;

    public ShapedOreRecipeNbtSensitive(ResourceLocation group, Block result, boolean nbtSensitive, Object... recipe) {
        super(group, result, recipe);
        this.nbtSensitive = nbtSensitive;
    }

    public ShapedOreRecipeNbtSensitive(ResourceLocation group, Item result, boolean nbtSensitive, Object... recipe) {
        super(group, result, recipe);
        this.nbtSensitive = nbtSensitive;
    }

    public ShapedOreRecipeNbtSensitive(ResourceLocation group, ItemStack result, boolean nbtSensitive, Object... recipe) {
        super(group, result, recipe);
        this.nbtSensitive = nbtSensitive;
    }

    @SuppressWarnings("unchecked")
    protected boolean checkMatch(InventoryCrafting inv, int startX, int startY, boolean mirror)
    {
        for (int x = 0; x < MAX_CRAFT_GRID_WIDTH; x++)
        {
            for (int y = 0; y < MAX_CRAFT_GRID_HEIGHT; y++)
            {
                int subX = x - startX;
                int subY = y - startY;
                Ingredient target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    if (mirror)
                    {
                        target = input.get(width - subX - 1 + subY * width);
                    }
                    else
                    {
                        target = input.get(subX + subY * width);
                    }

                    if (!target.apply(inv.getStackInRowAndColumn(x, y))
                            || !ShapelessOreRecipeNbtSensitive.matches(nbtSensitive, target, inv.getStackInRowAndColumn(x, y)))
                    {
                        return false;
                    }
                }
            }
        }

        return true;
    }
}
