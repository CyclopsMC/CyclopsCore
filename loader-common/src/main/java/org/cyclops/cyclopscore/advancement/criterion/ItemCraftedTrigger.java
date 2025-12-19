package org.cyclops.cyclopscore.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.criterion.ContextAwarePredicate;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.advancements.criterion.ItemPredicate;
import net.minecraft.advancements.criterion.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Trigger for when items are crafted.
 * @author rubensworks
 */
public class ItemCraftedTrigger extends SimpleCriterionTrigger<ItemCraftedTrigger.Instance> {

    public static final Codec<Instance> CODEC = RecordCodecBuilder.create(
            p_311401_ -> p_311401_.group(
                            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
                            ItemPredicate.CODEC.fieldOf("item").forGetter(Instance::itemPredicate)
                    )
                    .apply(p_311401_, Instance::new)
    );

    @Override
    public Codec<Instance> codec() {
        return CODEC;
    }

    public void trigger(ServerPlayer pPlayer, Predicate<ItemCraftedTrigger.Instance> pTestTrigger) {
        super.trigger(pPlayer, pTestTrigger);
    }

    public static record Instance(
            Optional<ContextAwarePredicate> player,
            ItemPredicate itemPredicate
    ) implements SimpleInstance, ICriterionInstanceTestable<ItemStack> {

        @Override
        public boolean test(ServerPlayer player, ItemStack craftingStack) {
            return itemPredicate.test(craftingStack);
        }

        @Override
        public Optional<ContextAwarePredicate> player() {
            return player;
        }
    }

}
