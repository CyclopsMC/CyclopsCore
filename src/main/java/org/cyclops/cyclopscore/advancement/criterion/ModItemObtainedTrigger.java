package org.cyclops.cyclopscore.advancement.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.registries.ForgeRegistries;
import org.cyclops.cyclopscore.Reference;

/**
 * Trigger for when an item of a given mod is obtained.
 * @author rubensworks
 */
public class ModItemObtainedTrigger extends AbstractCriterionTrigger<ModItemObtainedTrigger.Instance> {
    private final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "mod_item_obtained");

    public ModItemObtainedTrigger() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        return new Instance(getId(), entityPredicate, json.get("mod_id").getAsString());
    }

    @SubscribeEvent
    public void onPickup(EntityItemPickupEvent event) {
        if (event.getPlayer() != null && event.getPlayer() instanceof ServerPlayerEntity) {
            this.triggerListeners((ServerPlayerEntity) event.getPlayer(),
                    (i) -> i.test((ServerPlayerEntity) event.getPlayer(), event.getItem().getItem()));
        }
    }

    @SubscribeEvent
    public void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getPlayer() != null && event.getPlayer() instanceof ServerPlayerEntity) {
            this.triggerListeners((ServerPlayerEntity) event.getPlayer(),
                    (i) -> i.test((ServerPlayerEntity) event.getPlayer(), event.getCrafting()));
        }
    }

    @SubscribeEvent
    public void onSmelted(PlayerEvent.ItemSmeltedEvent event) {
        if (event.getPlayer() != null && event.getPlayer() instanceof ServerPlayerEntity) {
            this.triggerListeners((ServerPlayerEntity) event.getPlayer(),
                    (i) -> i.test((ServerPlayerEntity) event.getPlayer(), event.getSmelting()));
        }
    }

    public static class Instance extends CriterionInstance implements ICriterionInstanceTestable<ItemStack> {
        private final String modId;

        public Instance(ResourceLocation criterionIn, EntityPredicate.AndPredicate player, String modId) {
            super(criterionIn, player);
            this.modId = modId;
        }

        @Override
        public boolean test(ServerPlayerEntity player, ItemStack itemStack) {
            return !itemStack.isEmpty()
                    && ForgeRegistries.ITEMS.getKey(itemStack.getItem()).getNamespace().equals(this.modId);
        }
    }

}
