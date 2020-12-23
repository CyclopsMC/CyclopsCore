package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.loot.ILootSerializer;
import net.minecraft.loot.LootConditionType;
import net.minecraft.loot.LootFunctionType;
import net.minecraft.loot.conditions.ILootCondition;
import net.minecraft.loot.functions.ILootFunction;
import net.minecraft.util.ResourceLocation;
import net.minecraft.loot.LootPool;
import net.minecraft.loot.LootTable;
import net.minecraft.loot.LootTables;
import net.minecraft.loot.TableLootEntry;
import net.minecraft.util.registry.Registry;
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
                .addEntry(TableLootEntry.builder(source))
                .name(source.toString())
                .build());
    }

    /**
     * Register a new loot function.
     * @param id The loot function id.
     * @param serializer The loot function serializer.
     * @return The created loot function type
     */
    public static LootFunctionType registerFunction(ResourceLocation id, ILootSerializer<? extends ILootFunction> serializer) {
        return Registry.register(Registry.LOOT_FUNCTION_TYPE, id, new LootFunctionType(serializer));
    }

    /**
     * Register a new loot condition.
     * @param id The loot condition id.
     * @param serializer The loot condition serializer.
     * @return The created loot condition type
     */
    public static LootConditionType registerCondition(ResourceLocation id, ILootSerializer<? extends ILootCondition> serializer) {
        return Registry.register(Registry.LOOT_CONDITION_TYPE, id, new LootConditionType(serializer));
    }

}
