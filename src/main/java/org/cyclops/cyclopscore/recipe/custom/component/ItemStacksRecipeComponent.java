package org.cyclops.cyclopscore.recipe.custom.component;

import com.google.common.base.Function;
import com.google.common.collect.Lists;
import lombok.Data;
import net.minecraft.item.ItemStack;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeInput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeOutput;
import org.cyclops.cyclopscore.recipe.custom.api.IRecipeProperties;

import javax.annotation.Nullable;
import java.util.List;

/**
 * A {@link org.cyclops.cyclopscore.recipe.custom.api.IRecipe} component (input, output or properties) that holds
 * a list of {@link ItemStack}s.
 *
 * @author immortaleeb
 */
@Data
public class ItemStacksRecipeComponent implements IRecipeInput, IRecipeOutput, IRecipeProperties, IItemStacksRecipeComponent {

    private final List<ItemStackRecipeComponent> itemStacks;

    public ItemStacksRecipeComponent(List<Object> itemStacks) {
        this.itemStacks = Lists.transform(itemStacks, new Function<Object, ItemStackRecipeComponent>() {
            @Nullable
            @Override
            public ItemStackRecipeComponent apply(Object input) {
                return input instanceof String
                        ? new OreDictItemStackRecipeComponent((String) input)
                        : new ItemStackRecipeComponent((ItemStack) input);
            }
        });
    }

    @Override
    public boolean equals(Object object) {
        if (!(object instanceof ItemStacksRecipeComponent)) return false;
        ItemStacksRecipeComponent that = (ItemStacksRecipeComponent)object;
        return equals(this.itemStacks, that.itemStacks);
    }

    protected boolean equals(List<ItemStackRecipeComponent> a, List<ItemStackRecipeComponent> b) {
        if (a.size() == b.size()) {
            for (int i = 0; i < a.size(); i++) {
                if (!a.get(i).equals(b.get(i))) {
                    return false;
                }
            }
            return true;
        }
        return false;
    }

    @Override
    public int hashCode() {
        int hash = 876;
        for (ItemStackRecipeComponent itemStack : itemStacks) {
            hash |= itemStack.hashCode();
        }
        return hash;
    }

    public List<ItemStack> getItemStacks() {
        return Lists.transform(itemStacks, new Function<ItemStackRecipeComponent, ItemStack>() {
            @Nullable
            @Override
            public ItemStack apply(@Nullable ItemStackRecipeComponent input) {
                return input.getItemStack();
            }
        });
    }
}
