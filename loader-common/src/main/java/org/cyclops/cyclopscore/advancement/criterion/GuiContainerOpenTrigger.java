package org.cyclops.cyclopscore.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;

import java.util.Optional;
import java.util.function.Predicate;

/**
 * Trigger for when a container gui is opened.
 * @author rubensworks
 */
public class GuiContainerOpenTrigger extends SimpleCriterionTrigger<GuiContainerOpenTrigger.Instance> {

    public static final Codec<Instance> CODEC = RecordCodecBuilder.create(
            p_311401_ -> p_311401_.group(
                            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(Instance::player),
                            Codec.STRING.optionalFieldOf("container_class").forGetter(Instance::containerClass)
                    )
                    .apply(p_311401_, Instance::new)
    );

    @Override
    public Codec<Instance> codec() {
        return CODEC;
    }

    public void trigger(ServerPlayer pPlayer, Predicate<GuiContainerOpenTrigger.Instance> pTestTrigger) {
        super.trigger(pPlayer, pTestTrigger);
    }

    public static record Instance(
            Optional<ContextAwarePredicate> player,
            Optional<String> containerClass
    ) implements SimpleInstance, ICriterionInstanceTestable<AbstractContainerMenu> {
        @Override
        public boolean test(ServerPlayer player, AbstractContainerMenu container) {
            return containerClass
                    .map(className -> {
                        Class<?> clazz;
                        try {
                            clazz = Class.forName(className);
                        } catch (ClassNotFoundException e) {
                            throw new IllegalStateException("Could not find the container class with name '" + className + "'");
                        }
                        return clazz != null && clazz.isInstance(container);
                    })
                    .orElse(true);
        }
    }

}
