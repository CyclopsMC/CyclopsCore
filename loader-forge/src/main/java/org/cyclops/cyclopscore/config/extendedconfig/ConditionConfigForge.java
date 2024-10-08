package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraftforge.common.crafting.conditions.ICondition;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.config.ConfigurableTypesForge;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * Config for recipe conditions.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
public abstract class ConditionConfigForge<T extends ICondition> extends ExtendedConfigCommon<ConditionConfigForge<T>, MapCodec<T>, ModBaseForge<?>> {

    public ConditionConfigForge(ModBaseForge<?> mod, String namedId, MapCodec<T> conditionSerializer) {
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
        return ConfigurableTypesForge.CONDITION;
    }

    public IForgeRegistry<? super MapCodec<T>> getRegistryForge() {
        return ForgeRegistries.CONDITION_SERIALIZERS.get();
    }
}
