package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.neoforged.neoforge.common.conditions.ICondition;
import net.neoforged.neoforge.registries.NeoForgeRegistries;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

/**
 * Config for recipe conditions.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
public abstract class ConditionConfigNeoForge<T extends ICondition> extends ExtendedConfigRegistry<ConditionConfigNeoForge<T>, MapCodec<T>, ModBaseNeoForge<?>> {

    public ConditionConfigNeoForge(ModBaseNeoForge<?> mod, String namedId, MapCodec<T> conditionSerializer) {
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
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypesNeoForge.CONDITION;
    }

    @Override
    public Registry<? super MapCodec<T>> getRegistry() {
        return NeoForgeRegistries.CONDITION_SERIALIZERS;
    }
}
