package org.cyclops.cyclopscore.advancement.criterion;

import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.resources.ResourceLocation;
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
public class ModItemObtainedTrigger extends SimpleCriterionTrigger<ModItemObtainedTrigger.Instance> {
    private final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "mod_item_obtained");

    public ModItemObtainedTrigger() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject json, EntityPredicate.Composite entityPredicate, DeserializationContext conditionsParser) {
        return new Instance(getId(), entityPredicate, json.get("mod_id").getAsString());
    }

    @SubscribeEvent
    public void onPickup(EntityItemPickupEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof ServerPlayer) {
            this.trigger((ServerPlayer) event.getEntity(),
                    (i) -> i.test((ServerPlayer) event.getEntity(), event.getItem().getItem()));
        }
    }

    @SubscribeEvent
    public void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof ServerPlayer) {
            this.trigger((ServerPlayer) event.getEntity(),
                    (i) -> i.test((ServerPlayer) event.getEntity(), event.getCrafting()));
        }
    }

    @SubscribeEvent
    public void onSmelted(PlayerEvent.ItemSmeltedEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof ServerPlayer) {
            this.trigger((ServerPlayer) event.getEntity(),
                    (i) -> i.test((ServerPlayer) event.getEntity(), event.getSmelting()));
        }
    }

    public static class Instance extends AbstractCriterionTriggerInstance implements ICriterionInstanceTestable<ItemStack> {
        private final String modId;

        public Instance(ResourceLocation criterionIn, EntityPredicate.Composite player, String modId) {
            super(criterionIn, player);
            this.modId = modId;
        }

        @Override
        public boolean test(ServerPlayer player, ItemStack itemStack) {
            return !itemStack.isEmpty()
                    && ForgeRegistries.ITEMS.getKey(itemStack.getItem()).getNamespace().equals(this.modId);
        }
    }

}
