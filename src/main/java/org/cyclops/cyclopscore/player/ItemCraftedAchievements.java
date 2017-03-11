package org.cyclops.cyclopscore.player;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.base.Supplier;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stats.Achievement;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.apache.commons.lang3.tuple.Pair;

import java.util.Collection;
import java.util.Set;

/**
 * Registry for achievements that can be obtained when crafting an event.
 * @author rubensworks
 */
public class ItemCraftedAchievements {

    private static final ItemCraftedAchievements _INSTANCE = new ItemCraftedAchievements();
    private static final Multimap<Item, Pair<Predicate<ItemStack>, Achievement>> REGISTRY = Multimaps
            .newSetMultimap(Maps.<Item, Collection<Pair<Predicate<ItemStack>, Achievement>>>newIdentityHashMap(),
                    new Supplier<Set<Pair<Predicate<ItemStack>, Achievement>>>() {
                        @Override
                        public Set<Pair<Predicate<ItemStack>, Achievement>> get() {
                            return Sets.newIdentityHashSet();
                        }
                    });

    private ItemCraftedAchievements() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        for (Pair<Predicate<ItemStack>, Achievement> pair : REGISTRY.get(event.crafting.getItem())) {
            if (pair.getLeft().apply(event.crafting)) {
                event.player.addStat(pair.getRight());
            }
        }
    }

    /**
     * Register the given achievement to be received when the given item is crafted.
     * @param item The item.
     * @param achievement The achievement.
     */
    public static void register(Item item, Achievement achievement) {
        register(item, achievement, Predicates.<ItemStack>alwaysTrue());
    }

    /**
     * Register the given achievement to be received when the given item is crafted
     * and the predicate applies to the stack.
     * @param item The item.
     * @param achievement The achievement.
     * @param predicate An additional predicate that should be checked before the achievement will be received.
     */
    public static void register(Item item, Achievement achievement, Predicate<ItemStack> predicate) {
        REGISTRY.put(item, Pair.of(predicate, achievement));
    }

}
