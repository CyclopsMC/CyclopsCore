package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for recipe conditions.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class ConditionConfig<T extends ICondition> extends ExtendedConfigForge<ConditionConfig<T>, MapCodec<T>> {

    public ConditionConfig(ModBase mod, String namedId, MapCodec<T> conditionSerializer) {
        super(mod, namedId, (eConfig) -> conditionSerializer);
    }

    @Override
    public String getTranslationKey() {
        return "recipecondition." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.CONDITION;
    }

    @Override
    public Registry<? super MapCodec<T>> getRegistry() {
        return NeoForgeRegistries.CONDITION_SERIALIZERS;
    }
}
