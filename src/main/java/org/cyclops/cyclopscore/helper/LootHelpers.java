package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.ItemLootEntry;
import net.minecraft.world.storage.loot.LootEntry;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraft.world.storage.loot.RandomValueRange;
import net.minecraft.world.storage.loot.TableLootEntry;
import net.minecraft.world.storage.loot.conditions.ILootCondition;
import net.minecraft.world.storage.loot.functions.ILootFunction;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.LootTableLoadEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.obfuscation.ObfuscationHelpers;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;

/**
 * Helpers related to loot stuff.
 * @author rubensworks
 */
public class LootHelpers {

    private static final LootHelpers INSTANCE = new LootHelpers();
    private static final Multimap<ResourceLocation, LootPool> INJECT_LOOTPOOLS = MultimapBuilder.ListMultimapBuilder.hashKeys().arrayListValues().build();
    private static final Multimap<Pair<ResourceLocation, String>, ItemLootEntry> INJECT_LOOTENTRIES = MultimapBuilder.ListMultimapBuilder.hashKeys().arrayListValues().build();
    private static final Multimap<ResourceLocation, ResourceLocation> INJECT_LOOTTABLES = MultimapBuilder.ListMultimapBuilder.hashKeys().arrayListValues().build();

    private LootHelpers() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    // TODO: enable when ATs are fixed
//    @SubscribeEvent
//    public void onLootTableLoad(LootTableLoadEvent event) {
//        // Inject pools into tables
//        for (Map.Entry<ResourceLocation, LootPool> poolEntry : INJECT_LOOTPOOLS.entries()) {
//            ResourceLocation resourceLocation = poolEntry.getKey();
//            LootPool pool = poolEntry.getValue();
//            if (event.getName().equals(resourceLocation)) {
//                event.getTable().addPool(pool);
//            }
//        }
//
//        // Inject entries into pools
//        for (Map.Entry<Pair<ResourceLocation, String>, ItemLootEntry> entryItemEntry : INJECT_LOOTENTRIES.entries()) {
//            ResourceLocation resourceLocation = entryItemEntry.getKey().getKey();
//            String poolName = entryItemEntry.getKey().getValue();
//            ItemLootEntry entryItem = entryItemEntry.getValue();
//            if (event.getName().equals(resourceLocation)) {
//                LootTable lootTable = event.getTable();
//                if (poolName == null) {
//                    for (LootPool pool : lootTable.pools) {
//                        if (!pool.isFrozen()) {
//                            pool.lootEntries.add(entryItem);
//                        }
//                    }
//                } else {
//                    LootPool lootPool = lootTable.getPool(poolName);
//                    if (lootPool == null) {
//                        throw new RuntimeException(String.format("Could not find loot pool %s in loot table %s.", poolName, resourceLocation));
//                    }
//                    if (!lootPool.isFrozen()) {
//                        lootPool.lootEntries.add(entryItem);
//                    }
//                }
//            }
//        }
//
//        // Inject loot tables from resource location
//        for (ResourceLocation injectTable : INJECT_LOOTTABLES.get(event.getName())) {
//            injectLootTableDirect(event.getTable(), injectTable);
//        }
//    }

    /**
     * Add entries to the given loot table.
     * @param lootTable The loot table location.
     * @param poolName The name, or null for all pools.
     * @param lootEntryItems The new entries.
     */
    public static void addLootEntry(ResourceLocation lootTable, @Nullable String poolName, ItemLootEntry... lootEntryItems) {
        for (ItemLootEntry lootEntryItem : lootEntryItems) {
            INJECT_LOOTENTRIES.put(Pair.of(lootTable, poolName), lootEntryItem);
        }
    }

    /**
     * Add a loot pool to the given loot table.
     * @param lootTable The loot table location.
     * @param lootPool The new pool.
     */
    public static void addLootPool(ResourceLocation lootTable, LootPool lootPool) {
        INJECT_LOOTPOOLS.put(lootTable, lootPool);
    }

    /**
     * Inject the given loot table from a loot table file
     * into the given targets.
     * @param source The source loot table file to inject to the targets.
     * @param targets The targets to inject to.
     */
    public static void injectLootTable(ResourceLocation source, ResourceLocation... targets) {
        LootTables.register(source);
        for (ResourceLocation target : targets) {
            INJECT_LOOTTABLES.put(target, source);
        }
    }

    // TODO: enable when ATs are fixed
//    public static void injectLootTableDirect(LootTable target, ResourceLocation source) {
//        target.addPool(new LootPool(new LootEntry[]{
//                new TableLootEntry(source, 1, 0, new ILootCondition[0], new ILootFunction[0])},
//                new ILootCondition[0],
//                new ILootFunction[0],
//                new RandomValueRange(1),
//                new RandomValueRange(0),
//                source.toString()
//        ));
//    }

}
