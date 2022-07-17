package org.cyclops.cyclopscore.advancement.criterion;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.critereon.AbstractCriterionTriggerInstance;
import net.minecraft.advancements.critereon.DeserializationContext;
import net.minecraft.advancements.critereon.EntityPredicate;
import net.minecraft.advancements.critereon.SimpleCriterionTrigger;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.Reference;

import javax.annotation.Nullable;

/**
 * Trigger for when a container gui is opened.
 * @author rubensworks
 */
public class GuiContainerOpenTrigger extends SimpleCriterionTrigger<GuiContainerOpenTrigger.Instance> {
    private final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "container_gui_open");

    public GuiContainerOpenTrigger() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public ResourceLocation getId() {
        return ID;
    }

    @Override
    public Instance createInstance(JsonObject json, EntityPredicate.Composite entityPredicate, DeserializationContext conditionsParser) {
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
        if (event.getEntity() != null && event.getEntity() instanceof ServerPlayer) {
            this.trigger((ServerPlayer) event.getEntity(),
                    (i) -> i.test((ServerPlayer) event.getEntity(), event.getContainer()));
        }
    }

    public static class Instance extends AbstractCriterionTriggerInstance implements ICriterionInstanceTestable<AbstractContainerMenu> {
        private final Class<?> clazz;

        public Instance(ResourceLocation criterionIn, EntityPredicate.Composite player, @Nullable Class<?> clazz) {
            super(criterionIn, player);
            this.clazz = clazz;
        }

        @Override
        public boolean test(ServerPlayer player, AbstractContainerMenu container) {
            return clazz != null && clazz.isInstance(container);
        }
    }

}
