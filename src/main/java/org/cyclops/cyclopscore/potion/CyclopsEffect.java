package org.cyclops.cyclopscore.potion;

import net.minecraft.entity.LivingEntity;
import net.minecraft.potion.Effect;
import net.minecraft.potion.EffectType;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.cyclops.cyclopscore.config.extendedconfig.EffectConfig;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * A configurable potion effect.
 * @author rubensworks
 */
public abstract class CyclopsEffect extends Effect {

    private final ResourceLocation resource;

    protected EffectConfig eConfig = null;

    public CyclopsEffect(EffectType type, int liquidColor) {
        super(type, liquidColor);
        this.resource = new ResourceLocation(eConfig.getMod().getModId(), eConfig.getMod().getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_GUI) + "potions.png");
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean isActiveOn(LivingEntity entity) {
        return isActiveOn(entity, this);
    }

    public boolean isActiveOn(LivingEntity entity, Effect potion) {
        return entity.getActivePotionEffect(potion) != null;
    }

    public int getAmplifier(LivingEntity entity, Effect potion) {
        return entity != null ? entity.getActivePotionEffect(potion).getAmplifier() : 0;
    }

    public int getAmplifier(LivingEntity entity) {
        return getAmplifier(entity, this);
    }

    protected abstract void onUpdate(LivingEntity entity);

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingUpdateEvent event) {
        LivingEntity entity = event.getEntityLiving();
        if (isActiveOn(entity)) {
            onUpdate(entity);
        }
    }

}
