package org.cyclops.cyclopscore.potion;

import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.EntityTickEvent;
import org.cyclops.cyclopscore.config.extendedconfig.EffectConfig;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * A configurable potion effect.
 * @author rubensworks
 */
public abstract class CyclopsEffect extends MobEffect {

    private final ResourceLocation resource;

    protected EffectConfig eConfig = null;

    public CyclopsEffect(MobEffectCategory type, int liquidColor) {
        super(type, liquidColor);
        this.resource = ResourceLocation.fromNamespaceAndPath(eConfig.getMod().getModId(), eConfig.getMod().getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_GUI) + "potions.png");
        NeoForge.EVENT_BUS.register(this);
    }

    public boolean isActiveOn(LivingEntity entity) {
        return isActiveOn(entity, Holder.direct(this));
    }

    public boolean isActiveOn(LivingEntity entity, Holder<MobEffect> potion) {
        return entity.getEffect(potion) != null;
    }

    public int getAmplifier(LivingEntity entity, Holder<MobEffect> potion) {
        return entity != null ? entity.getEffect(potion).getAmplifier() : 0;
    }

    public int getAmplifier(LivingEntity entity) {
        return getAmplifier(entity, Holder.direct(this));
    }

    protected abstract void onUpdate(LivingEntity entity);

    @SubscribeEvent
    public void onEntityUpdate(EntityTickEvent.Post event) {
        Entity entity = event.getEntity();
        if (entity instanceof LivingEntity livingEntity && isActiveOn(livingEntity)) {
            onUpdate(livingEntity);
        }
    }

}
