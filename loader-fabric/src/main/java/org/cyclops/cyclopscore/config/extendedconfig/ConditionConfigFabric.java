package org.cyclops.cyclopscore.config.extendedconfig;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.config.ConfigurableTypesFabric;
import org.cyclops.cyclopscore.init.ModBaseFabric;

/**
 * Config for recipe conditions.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
public abstract class ConditionConfigFabric<T extends ResourceCondition> extends ExtendedConfigCommon<ConditionConfigFabric<T>, ResourceConditionType<T>, ModBaseFabric<?>> {

    public ConditionConfigFabric(ModBaseFabric<?> mod, String namedId, ResourceConditionType<T> type) {
        super(mod, namedId, (eConfig) -> type);
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
        return ConfigurableTypesFabric.CONDITION;
    }
}
