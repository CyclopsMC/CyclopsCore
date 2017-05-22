package org.cyclops.cyclopscore.recipe;

import net.minecraft.block.Block;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.util.Iterator;
import java.util.List;

/**
 * An NBT-sensitive variant of {@link ShapedOreRecipe}
 * @author rubensworks
 */
public class ShapedOreRecipeNbtSensitive extends ShapedOreRecipe {

    private final boolean nbtSensitive;

    public ShapedOreRecipeNbtSensitive(Block result, boolean nbtSensitive, Object... recipe) {
        super(result, recipe);
        this.nbtSensitive = nbtSensitive;
    }

    public ShapedOreRecipeNbtSensitive(Item result, boolean nbtSensitive, Object... recipe) {
        super(result, recipe);
        this.nbtSensitive = nbtSensitive;
    }

    public ShapedOreRecipeNbtSensitive(ItemStack result, boolean nbtSensitive, Object... recipe) {
        super(result, recipe);
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
                Object target = null;

                if (subX >= 0 && subY >= 0 && subX < width && subY < height)
                {
                    if (mirror)
                    {
                        target = input[width - subX - 1 + subY * width];
                    }
                    else
                    {
                        target = input[subX + subY * width];
                    }
                }

                ItemStack slot = inv.getStackInRowAndColumn(x, y);

                if (target instanceof ItemStack)
                {
                    if (!ShapelessOreRecipeNbtSensitive.matches(nbtSensitive, (ItemStack) target, slot))
                    {
                        return false;
                    }
                }
                else if (target instanceof List)
                {
                    boolean matched = false;

                    Iterator<ItemStack> itr = ((List<ItemStack>)target).iterator();
                    while (itr.hasNext() && !matched)
                    {
                        matched = ShapelessOreRecipeNbtSensitive.matches(nbtSensitive, itr.next(), slot);
                    }

                    if (!matched)
                    {
                        return false;
                    }
                }
                else if (target == null && slot != null)
                {
                    return false;
                }
            }
        }

        return true;
    }
}
