package org.cyclops.cyclopscore.recipe.custom.component;

import com.google.common.collect.Lists;
import lombok.Data;
import net.minecraft.item.ItemStack;
import net.minecraftforge.oredict.OreDictionary;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;

import java.util.List;

/**
 * A {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe} component (input, output or properties) that holds an
 * {@link net.minecraft.item.ItemStack}.
 *
 * @author immortaleeb
 */
@Data
public class ItemStackRecipeComponent implements IRecipeInput, IRecipeOutput, IRecipeProperties, IItemStackRecipeComponent {

    private static final int META_WILDCARD = OreDictionary.WILDCARD_VALUE;

    private final ItemStack itemStack;

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ItemStackRecipeComponent)) return false;
        ItemStackRecipeComponent that = (ItemStackRecipeComponent)object;
        return equals(this.itemStack, that.itemStack);
    }

    protected boolean equals(ItemStack a, ItemStack b) {
        return a != null && b != null && a.getItem().equals(b.getItem()) && (a.getItemDamage() == b.getItemDamage() ||
                a.getItemDamage() == META_WILDCARD || b.getItemDamage() == META_WILDCARD);
    }

    @Override
    public int hashCode() {
        return itemStack.getItem().hashCode() + 876;
    }

    public List<ItemStack> getItemStacks() {
        return Lists.newArrayList(getItemStack());
    }
}
