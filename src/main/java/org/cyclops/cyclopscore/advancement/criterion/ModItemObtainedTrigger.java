package org.cyclops.cyclopscore.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.EntityItemPickupEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Optional;

/**
 * Trigger for when an item of a given mod is obtained.
 * @author rubensworks
 */
public class ModItemObtainedTrigger extends SimpleCriterionTrigger<ModItemObtainedTrigger.Instance> {

    public static final Codec<ModItemObtainedTrigger.Instance> CODEC = RecordCodecBuilder.create(
            p_311401_ -> p_311401_.group(
                            ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(ModItemObtainedTrigger.Instance::player),
                            Codec.STRING.fieldOf("mod_id").forGetter(ModItemObtainedTrigger.Instance::modId)
                    )
                    .apply(p_311401_, ModItemObtainedTrigger.Instance::new)
    );

    public ModItemObtainedTrigger() {
        NeoForge.EVENT_BUS.register(this);
    }

    @Override
    public Codec<ModItemObtainedTrigger.Instance> codec() {
        return CODEC;
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

    public static record Instance(
            Optional<ContextAwarePredicate> player,
            String modId
    ) implements SimpleCriterionTrigger.SimpleInstance, ICriterionInstanceTestable<ItemStack> {

        @Override
        public boolean test(ServerPlayer player, ItemStack itemStack) {
            return !itemStack.isEmpty()
                    && BuiltInRegistries.ITEM.getKey(itemStack.getItem()).getNamespace().equals(this.modId);
        }
    }

}
