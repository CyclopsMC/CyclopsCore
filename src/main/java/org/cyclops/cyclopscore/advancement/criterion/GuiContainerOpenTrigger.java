package org.cyclops.cyclopscore.advancement.criterion;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Container;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerContainerEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.cyclopscore.Reference;

import javax.annotation.Nullable;

/**
 * Trigger for when a container gui is opened.
 * @author rubensworks
 */
public class GuiContainerOpenTrigger extends BaseCriterionTrigger<Container, GuiContainerOpenTrigger.Instance> {
    public GuiContainerOpenTrigger() {
        super(new ResourceLocation(Reference.MOD_ID, "container_gui_open"));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
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
        return new Instance(getId(), clazz);
    }

    @SubscribeEvent
    public void onEvent(PlayerContainerEvent.Open event) {
        if (event.getEntityPlayer() != null && event.getEntityPlayer() instanceof EntityPlayerMP) {
            this.trigger((EntityPlayerMP) event.getEntityPlayer(), event.getContainer());
        }
    }

    public static class Instance extends AbstractCriterionInstance implements ICriterionInstanceTestable<Container> {
        private final Class<?> clazz;

        public Instance(ResourceLocation criterionIn, @Nullable Class<?> clazz) {
            super(criterionIn);
            this.clazz = clazz;
        }

        @Override
        public boolean test(EntityPlayerMP player, Container container) {
            return clazz != null && clazz.isInstance(container);
        }
    }

}
