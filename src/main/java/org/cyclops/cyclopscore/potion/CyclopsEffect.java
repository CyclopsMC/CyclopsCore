package org.cyclops.cyclopscore.potion;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.entity.LivingEntity;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
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
        this.resource = new ResourceLocation(eConfig.getMod().getModId(), eConfig.getMod().getReferenceValue(ModBase.REFKEY_TEXTURE_PATH_GUI) + "potions.png");
        MinecraftForge.EVENT_BUS.register(this);
    }

    public boolean isActiveOn(LivingEntity entity) {
        return isActiveOn(entity, this);
    }

    public boolean isActiveOn(LivingEntity entity, MobEffect potion) {
        return entity.getEffect(potion) != null;
    }

    public int getAmplifier(LivingEntity entity, MobEffect potion) {
        return entity != null ? entity.getEffect(potion).getAmplifier() : 0;
    }

    public int getAmplifier(LivingEntity entity) {
        return getAmplifier(entity, this);
    }

    protected abstract void onUpdate(LivingEntity entity);

    @SubscribeEvent
    public void onEntityUpdate(LivingEvent.LivingTickEvent event) {
        LivingEntity entity = event.getEntity();
        if (isActiveOn(entity)) {
            onUpdate(entity);
        }
    }

}
