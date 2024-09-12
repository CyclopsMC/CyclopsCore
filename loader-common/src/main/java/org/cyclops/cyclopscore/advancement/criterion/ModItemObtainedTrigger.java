package org.cyclops.cyclopscore.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Trigger for when an item of a given mod is obtained.
 * @author rubensworks
 */
public class ModItemObtainedTrigger extends SimpleCriterionTrigger<ModItemObtainedTrigger.Instance> {

    public static final Codec<Instance> CODEC = RecordCodecBuilder.create(
            p_311401_ -> p_311401_.group(
                            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
                            Codec.STRING.fieldOf("mod_id").forGetter(Instance::modId)
                    )
                    .apply(p_311401_, Instance::new)
    );

    @Override
    public Codec<Instance> codec() {
        return CODEC;
    }

    public void trigger(ServerPlayer pPlayer, Predicate<ModItemObtainedTrigger.Instance> pTestTrigger) {
        super.trigger(pPlayer, pTestTrigger);
    }

    public static record Instance(
            Optional<ContextAwarePredicate> player,
            String modId
    ) implements SimpleInstance, ICriterionInstanceTestable<ItemStack> {

        @Override
        public boolean test(ServerPlayer player, ItemStack itemStack) {
            return !itemStack.isEmpty()
                    && BuiltInRegistries.ITEM.getKey(itemStack.getItem()).getNamespace().equals(this.modId);
        }
    }

}
