package org.cyclops.cyclopscore.advancement.criterion;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
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
        if (event.player != null && event.player instanceof EntityPlayerMP) {
            this.trigger((EntityPlayerMP) event.player, event);
        }
    }

    public static class Instance extends AbstractCriterionInstance implements ICriterionInstanceTestable<PlayerEvent.ItemCraftedEvent> {
        private final ItemPredicate[] itemPredicates;

        public Instance(ResourceLocation criterionIn, ItemPredicate[] itemPredicates) {
            super(criterionIn);
            this.itemPredicates = itemPredicates;
        }

        @Override
        public boolean test(EntityPlayerMP player, PlayerEvent.ItemCraftedEvent criterionData) {
            for (ItemPredicate itemPredicate : this.itemPredicates) {
                if (itemPredicate.test(criterionData.crafting)) {
                    return true;
                }
            }
            return false;
        }
    }

}
