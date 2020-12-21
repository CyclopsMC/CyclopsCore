package org.cyclops.cyclopscore.advancement.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.Reference;

/**
 * Trigger for when items are crafted.
 * @author rubensworks
 */
public class ItemCraftedTrigger extends AbstractCriterionTrigger<ItemCraftedTrigger.Instance> {
    private final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "item_crafted");

    public ItemCraftedTrigger() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        return new Instance(getId(), entityPredicate, ItemPredicate.deserializeArray(json.get("items")));
    }

    @SubscribeEvent
    public void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getPlayer() != null && event.getPlayer() instanceof ServerPlayerEntity) {
            this.triggerListeners((ServerPlayerEntity) event.getPlayer(),
                    (i) -> i.test((ServerPlayerEntity) event.getPlayer(), event));
        }
    }

    public static class Instance extends CriterionInstance implements ICriterionInstanceTestable<PlayerEvent.ItemCraftedEvent> {
        private final ItemPredicate[] itemPredicates;

        public Instance(ResourceLocation criterionIn, EntityPredicate.AndPredicate player, ItemPredicate[] itemPredicates) {
            super(criterionIn, player);
            this.itemPredicates = itemPredicates;
        }

        @Override
        public boolean test(ServerPlayerEntity player, PlayerEvent.ItemCraftedEvent criterionData) {
            for (ItemPredicate itemPredicate : this.itemPredicates) {
                if (itemPredicate.test(criterionData.getCrafting())) {
                    return true;
                }
            }
            return false;
        }
    }

}
