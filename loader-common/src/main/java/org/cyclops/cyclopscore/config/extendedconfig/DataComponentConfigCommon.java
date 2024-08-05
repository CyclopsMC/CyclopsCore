package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.component.DataComponentType;
import net.minecraft.core.registries.BuiltInRegistries;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.UnaryOperator;

/**
 * Config for data component types.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class DataComponentConfigCommon<T, M extends IModBase> extends ExtendedConfigForge<DataComponentConfigCommon<T, M>, DataComponentType<T>, M> {

    public DataComponentConfigCommon(M mod, String namedId, UnaryOperator<DataComponentType.Builder<T>> builder) {
        super(mod, namedId, (eConfig) -> builder.apply(DataComponentType.builder()).build());
    }

    @Override
    public String getTranslationKey() {
        return "datacomponent." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.DATA_COMPONENT;
    }

    @Override
    public Registry<DataComponentType<?>> getRegistry() {
        return BuiltInRegistries.DATA_COMPONENT_TYPE;
    }
}
