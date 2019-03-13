package org.cyclops.cyclopscore.advancement.criterion;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonObject;
import net.minecraft.advancements.critereon.AbstractCriterionInstance;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;
import org.cyclops.cyclopscore.Reference;

/**
 * Trigger for when an item of a given mod is obtained.
 * @author rubensworks
 */
public class ModItemObtainedTrigger extends BaseCriterionTrigger<ItemStack, ModItemObtainedTrigger.Instance> {
    public ModItemObtainedTrigger() {
        super(new ResourceLocation(Reference.MOD_ID, "mod_item_obtained"));
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public Instance deserializeInstance(JsonObject json, JsonDeserializationContext context) {
        return new Instance(getId(), json.get("mod_id").getAsString());
    }

    @SubscribeEvent
    public void onPickup(EntityItemPickupEvent event) {
        if (event.getEntityPlayer() != null && event.getEntityPlayer() instanceof EntityPlayerMP) {
            this.trigger((EntityPlayerMP) event.getEntityPlayer(), event.getItem().getItem());
        }
    }

    @SubscribeEvent
    public void onCrafted(PlayerEvent.ItemCraftedEvent event) {
        if (event.player != null && event.player instanceof EntityPlayerMP) {
            this.trigger((EntityPlayerMP) event.player, event.crafting);
        }
    }

    @SubscribeEvent
    public void onSmelted(PlayerEvent.ItemSmeltedEvent event) {
        if (event.player != null && event.player instanceof EntityPlayerMP) {
            this.trigger((EntityPlayerMP) event.player, event.smelting);
        }
    }

    public static class Instance extends AbstractCriterionInstance implements ICriterionInstanceTestable<ItemStack> {
        private final String modId;

        public Instance(ResourceLocation criterionIn, String modId) {
            super(criterionIn);
            this.modId = modId;
        }

        @Override
        public boolean test(EntityPlayerMP player, ItemStack itemStack) {
            return !itemStack.isEmpty()
                    && Item.REGISTRY.getNameForObject(itemStack.getItem()).getNamespace().equals(this.modId);
        }
    }

}
