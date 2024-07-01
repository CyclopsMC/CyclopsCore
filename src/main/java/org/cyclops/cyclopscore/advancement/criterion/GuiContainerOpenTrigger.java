package org.cyclops.cyclopscore.advancement.criterion;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import net.minecraft.advancements.critereon.ContextAwarePredicate;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.player.PlayerContainerEvent;

import java.util.Optional;

/**
 * Trigger for when a container gui is opened.
 * @author rubensworks
 */
public class GuiContainerOpenTrigger extends SimpleCriterionTrigger<GuiContainerOpenTrigger.Instance> {

    public static final Codec<GuiContainerOpenTrigger.Instance> CODEC = RecordCodecBuilder.create(
            p_311401_ -> p_311401_.group(
                            EntityPredicate.ADVANCEMENT_CODEC.optionalFieldOf("player").forGetter(GuiContainerOpenTrigger.Instance::player),
                            Codec.STRING.optionalFieldOf("container_class").forGetter(GuiContainerOpenTrigger.Instance::containerClass)
                    )
                    .apply(p_311401_, GuiContainerOpenTrigger.Instance::new)
    );

    public GuiContainerOpenTrigger() {
        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onEvent(PlayerContainerEvent.Open event) {
        if (event.getEntity() != null && event.getEntity() instanceof ServerPlayer) {
            this.trigger((ServerPlayer) event.getEntity(),
                    (i) -> i.test((ServerPlayer) event.getEntity(), event.getContainer()));
        }
    }

    @Override
    public Codec<Instance> codec() {
        return CODEC;
    }

    public static record Instance(
            Optional<ContextAwarePredicate> player,
            Optional<String> containerClass
    ) implements SimpleCriterionTrigger.SimpleInstance, ICriterionInstanceTestable<AbstractContainerMenu> {
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
