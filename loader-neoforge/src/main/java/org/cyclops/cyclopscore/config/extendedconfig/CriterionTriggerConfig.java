package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for recipe conditions.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class CriterionTriggerConfig<T extends CriterionTriggerInstance> extends ExtendedConfigForge<CriterionTriggerConfig<T>, CriterionTrigger<T>> {

    public CriterionTriggerConfig(ModBase mod, String namedId, CriterionTrigger<T> criterionTrigger) {
        super(mod, namedId, (eConfig) -> criterionTrigger);
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
        return ConfigurableType.CRITERION_TRIGGER;
    }

    @Override
    public Registry<? super CriterionTrigger<T>> getRegistry() {
        return BuiltInRegistries.TRIGGER_TYPES;
    }
}
