package org.cyclops.cyclopscore.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.ItemPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ExtraCodecs;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;

import java.util.Optional;

/**
 * Trigger for when items are crafted.
 * @author rubensworks
 */
public class ItemCraftedTrigger extends SimpleCriterionTrigger<ItemCraftedTrigger.Instance> {

    public static final Codec<Instance> CODEC = RecordCodecBuilder.create(
            p_311401_ -> p_311401_.group(
                            ExtraCodecs.strictOptionalField(EntityPredicate.ADVANCEMENT_CODEC, "player").forGetter(ItemCraftedTrigger.Instance::player),
                            ItemPredicate.CODEC.fieldOf("item").forGetter(ItemCraftedTrigger.Instance::itemPredicate)
                    )
                    .apply(p_311401_, ItemCraftedTrigger.Instance::new)
    );

    public ItemCraftedTrigger() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.getEntity() != null && event.getEntity() instanceof ServerPlayer) {
            this.trigger((ServerPlayer) event.getEntity(),
                    (i) -> i.test((ServerPlayer) event.getEntity(), event));
        }
    }

    @Override
    public Codec<Instance> codec() {
        return CODEC;
    }

    public static record Instance(
            Optional<ContextAwarePredicate> player,
            ItemPredicate itemPredicate
    ) implements SimpleCriterionTrigger.SimpleInstance, ICriterionInstanceTestable<PlayerEvent.ItemCraftedEvent> {

        @Override
        public boolean test(ServerPlayer player, PlayerEvent.ItemCraftedEvent criterionData) {
            return itemPredicate.matches(criterionData.getCrafting());
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return player;
        }
    }

}
