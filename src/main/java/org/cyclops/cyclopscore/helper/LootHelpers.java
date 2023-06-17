package org.cyclops.cyclopscore.helper;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;

/**
 * Helpers related to loot stuff.
 * @author rubensworks
 */
public class LootHelpers {

    /**
     * Register a new loot function.
     * @param id The loot function id.
     * @param serializer The loot function serializer.
     * @return The created loot function type
     */
    public static LootItemFunctionType registerFunction(ResourceLocation id, Serializer<? extends LootItemFunction> serializer) {
        return Registry.register(BuiltInRegistries.LOOT_FUNCTION_TYPE, id, new LootItemFunctionType(serializer));
    }

    /**
     * Register a new loot condition.
     * @param id The loot condition id.
     * @param serializer The loot condition serializer.
     * @return The created loot condition type
     */
    public static LootItemConditionType registerCondition(ResourceLocation id, Serializer<? extends LootItemCondition> serializer) {
        return Registry.register(BuiltInRegistries.LOOT_CONDITION_TYPE, id, new LootItemConditionType(serializer));
    }

}
