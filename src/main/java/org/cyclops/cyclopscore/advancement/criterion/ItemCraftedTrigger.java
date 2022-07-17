package org.cyclops.cyclopscore.advancement.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.Reference;

/**
 * Trigger for when items are crafted.
 * @author rubensworks
 */
public class ItemCraftedTrigger extends SimpleCriterionTrigger<ItemCraftedTrigger.Instance> {
    private final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "item_crafted");

    public ItemCraftedTrigger() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject json, EntityPredicate.Composite entityPredicate, DeserializationContext conditionsParser) {
        return new Instance(getId(), entityPredicate, ItemPredicate.fromJsonArray(json.get("items")));
    }

    @SubscribeEvent
    public void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof ServerPlayer) {
            this.trigger((ServerPlayer) event.getEntity(),
                    (i) -> i.test((ServerPlayer) event.getEntity(), event));
        }
    }

    public static class Instance extends AbstractCriterionTriggerInstance implements ICriterionInstanceTestable<PlayerEvent.ItemCraftedEvent> {
        private final ItemPredicate[] itemPredicates;

        public Instance(ResourceLocation criterionIn, EntityPredicate.Composite player, ItemPredicate[] itemPredicates) {
            super(criterionIn, player);
            this.itemPredicates = itemPredicates;
        }

        @Override
        public boolean test(ServerPlayer player, PlayerEvent.ItemCraftedEvent criterionData) {
            for (ItemPredicate itemPredicate : this.itemPredicates) {
                if (itemPredicate.matches(criterionData.getCrafting())) {
                    return true;
                }
            }
            return false;
        }
    }

}
