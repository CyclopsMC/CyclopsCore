package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraft.world.storage.loot.LootTables;
import net.minecraft.world.storage.loot.TableLootEntry;
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
        LootTables.register(source);
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

}
