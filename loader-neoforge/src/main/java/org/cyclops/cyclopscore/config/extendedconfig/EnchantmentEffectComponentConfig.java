package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.UnaryOperator;

/**
 * Config for enchantment effect components.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public class EnchantmentEffectComponentConfig<T> extends ExtendedConfigForge<EnchantmentEffectComponentConfig<T>, DataComponentType<T>>{
    public EnchantmentEffectComponentConfig(ModBase<?> mod, String namedId, UnaryOperator<DataComponentType.Builder<T>> builder) {
        super(mod, namedId, (eConfig) -> builder.apply(DataComponentType.builder()).build());
    }

    @Override
    public String getTranslationKey() {
        return "enchantmenteffectcomponent." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableTypesNeoForge.D_ENCHANTMENT_EFFECT_COMPONENT;
    }

    @Override
    public Registry<DataComponentType<?>> getRegistry() {
        return BuiltInRegistries.ENCHANTMENT_EFFECT_COMPONENT_TYPE;
    }
}
