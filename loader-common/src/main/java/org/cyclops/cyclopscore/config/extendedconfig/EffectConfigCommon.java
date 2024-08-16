package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.effect.MobEffect;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for potion effects.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfig
 */
public abstract class EffectConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<EffectConfigCommon<M>, MobEffect, M> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public EffectConfigCommon(M mod, String namedId, Function<EffectConfigCommon<M>, ? extends MobEffect> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "potions." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.EFFECT;
    }

    @Override
    public Registry<? super MobEffect> getRegistry() {
        return BuiltInRegistries.MOB_EFFECT;
    }
}
