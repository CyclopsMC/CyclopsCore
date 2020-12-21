package org.cyclops.cyclopscore.advancement.criterion;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.criterion.AbstractCriterionTrigger;
import net.minecraft.advancements.criterion.CriterionInstance;
import net.minecraft.advancements.criterion.EntityPredicate;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.inventory.container.Container;
import net.minecraft.loot.ConditionArrayParser;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.Reference;

import javax.annotation.Nullable;

/**
 * Trigger for when a container gui is opened.
 * @author rubensworks
 */
public class GuiContainerOpenTrigger extends AbstractCriterionTrigger<GuiContainerOpenTrigger.Instance> {
    private final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "container_gui_open");

    public GuiContainerOpenTrigger() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance deserializeTrigger(JsonObject json, EntityPredicate.AndPredicate entityPredicate, ConditionArrayParser conditionsParser) {
        JsonElement jsonElement = json.get("container_class");
        String className = jsonElement != null && !jsonElement.isJsonNull() ? jsonElement.getAsString() : null;
        Class<?> clazz = null;
        if (className != null) {
            try {
                clazz = Class.forName(className);
            } catch (ClassNotFoundException e) {
                throw new JsonSyntaxException("Could not find the container class with name '" + className + "'");
            }
        }
        return new Instance(getId(), entityPredicate, clazz);
    }

    @SubscribeEvent
    public void onEvent(PlayerContainerEvent.Open event) {
        if (event.getPlayer() != null && event.getPlayer() instanceof ServerPlayerEntity) {
            this.triggerListeners((ServerPlayerEntity) event.getPlayer(),
                    (i) -> i.test((ServerPlayerEntity) event.getPlayer(), event.getContainer()));
        }
    }

    public static class Instance extends CriterionInstance implements ICriterionInstanceTestable<Container> {
        private final Class<?> clazz;

        public Instance(ResourceLocation criterionIn, EntityPredicate.AndPredicate player, @Nullable Class<?> clazz) {
            super(criterionIn, player);
            this.clazz = clazz;
        }

        @Override
        public boolean test(ServerPlayerEntity player, Container container) {
            return clazz != null && clazz.isInstance(container);
        }
    }

}
