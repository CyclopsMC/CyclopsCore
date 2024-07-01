package org.cyclops.cyclopscore.recipe;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;

import javax.annotation.Nullable;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

/**
 * Helper class that can return a first item based on an ingredient.
 *
 * The first item is calculated lazily to be able to cope with late tag resolution in MC.
 *
 * @author rubensworks
 */
public class ItemStackFromIngredient {
    private final List<String> modPriorities;
    private final String tag;
    private final Ingredient ingredient;
    private final int count;

    @Nullable
    private ItemStack firstItemStack;

    public ItemStackFromIngredient(List<String> modPriorities, String tag, Ingredient ingredient, int count) {
        this.modPriorities = modPriorities;
        this.tag = tag;
        this.ingredient = ingredient;
        this.count = count;
    }

    public String getTag() {
        return tag;
    }

    public Ingredient getIngredient() {
        return ingredient;
    }

    public int getCount() {
        return count;
    }

    public ItemStack getFirstItemStack() {
        if (firstItemStack != null) {
            return firstItemStack;
        }

        // Obtain all stacks for the given tag
        ItemStack[] matchingStacks = ingredient.getItems();

        // Create a mod id to order index map
        Map<String, Integer> modPriorityIndex = Maps.newHashMap();
        for (int i = 0; i < modPriorities.size(); i++) {
            modPriorityIndex.put(modPriorities.get(i), i);
        }

        // Sort stacks by mod id, and take first
        ItemStack outputStack = Arrays.stream(matchingStacks)
                .min(Comparator.comparingInt(e -> modPriorityIndex.getOrDefault(
                        BuiltInRegistries.ITEM.getKey(e.getItem()).getNamespace(),
                        Integer.MAX_VALUE
                )))
                .orElseThrow(() -> new IllegalStateException("No tag value found for " + tag))
                .copy();

        firstItemStack = outputStack.copy();
        firstItemStack.setCount(count);
        return firstItemStack;
    }

    public void writeToPacket(RegistryFriendlyByteBuf buf) {
        buf.writeVarInt(modPriorities.size());
        for (String modPriority : modPriorities) {
            buf.writeUtf(modPriority);
        }
        buf.writeUtf(tag);
        Ingredient.CONTENTS_STREAM_CODEC.encode(buf, ingredient);
        buf.writeVarInt(count);
    }

    public static ItemStackFromIngredient readFromPacket(RegistryFriendlyByteBuf buf) {
        List<String> modPriorities = Lists.newArrayList();
        int modPrioritiesSize = buf.readVarInt();
        for (int i = 0; i < modPrioritiesSize; i++) {
            modPriorities.add(buf.readUtf());
        }
        String key = buf.readUtf();
        Ingredient ingredient = Ingredient.CONTENTS_STREAM_CODEC.decode(buf);
        int count = buf.readVarInt();

        return new ItemStackFromIngredient(modPriorities, key, ingredient, count);
    }
}
