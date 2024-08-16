package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.UnaryOperator;

/**
 * Config for enchantment effect components.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfig
 */
public class EnchantmentEffectComponentConfigCommon<T, M extends IModBase> extends ExtendedConfigRegistry<EnchantmentEffectComponentConfigCommon<T, M>, DataComponentType<T>, M> {

    public EnchantmentEffectComponentConfigCommon(M mod, String namedId, UnaryOperator<DataComponentType.Builder<T>> builder) {
        super(mod, namedId, (eConfig) -> builder.apply(DataComponentType.builder()).build());

    }

    @Override
    public String getTranslationKey() {
        return "enchantmenteffectcomponent." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.ENCHANTMENT_ENTITY_EFFECT;
    }

    @Override
    public Registry<DataComponentType<?>> getRegistry() {
        return BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE;
    }
}
