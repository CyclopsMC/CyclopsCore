package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.storage.loot.Serializer;
import net.minecraft.world.level.storage.loot.predicates.LootItemConditionType;
import net.minecraft.world.level.storage.loot.functions.LootItemFunctionType;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.world.level.storage.loot.functions.LootItemFunction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.storage.loot.LootPool;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.entries.LootTableReference;
import net.minecraft.core.Registry;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;

/**
 * Helpers related to loot stuff.
 * @author rubensworks
 */
public class LootHelpers {

    private static final LootHelpers INSTANCE = new LootHelpers();
    private static final Multimap<ResourceLocation, ResourceLocation> INJECT_LOOTTABLES = MultimapBuilder.ListMultimapBuilder.hashKeys().arrayListValues().build();

    private LootHelpers() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLootTableLoad(LootTableLoadEvent event) {
        // Inject loot tables from resource location
        for (ResourceLocation injectTable : INJECT_LOOTTABLES.get(event.getName())) {
            injectLootTableDirect(event.getTable(), injectTable);
        }
    }

    /**
     * Inject the given loot table from a loot table file
     * into the given targets.
     * @param source The source loot table file to inject to the targets.
     * @param targets The targets to inject to.
     */
    public static void injectLootTable(ResourceLocation source, ResourceLocation... targets) {
        for (ResourceLocation target : targets) {
            INJECT_LOOTTABLES.put(target, source);
        }
    }

    public static void injectLootTableDirect(LootTable target, ResourceLocation source) {
        target.addPool(new LootPool.Builder()
                .add(LootTableReference.lootTableReference(source))
                .name(source.toString())
                .build());
    }

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
