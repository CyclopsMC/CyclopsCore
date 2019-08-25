package org.cyclops.cyclopscore.advancement.criterion;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.Reference;

/**
 * Trigger for when items are crafted.
 * @author rubensworks
 */
public class ItemCraftedTrigger extends BaseCriterionTrigger<PlayerEvent.ItemCraftedEvent, ItemCraftedTrigger.Instance> {
    public ItemCraftedTrigger() {
        super(new ResourceLocation(Reference.MOD_ID, "item_crafted"));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new Instance(getId(), ItemPredicate.deserializeArray(json.get("items")));
    }

    @SubscribeEvent
    public void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getPlayer() != null && event.getPlayer() instanceof ServerPlayerEntity) {
            this.trigger((ServerPlayerEntity) event.getPlayer(), event);
        }
    }

    public static class Instance extends CriterionInstance implements ICriterionInstanceTestable<PlayerEvent.ItemCraftedEvent> {
        private final ItemPredicate[] itemPredicates;

        public Instance(ResourceLocation criterionIn, ItemPredicate[] itemPredicates) {
            super(criterionIn);
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
