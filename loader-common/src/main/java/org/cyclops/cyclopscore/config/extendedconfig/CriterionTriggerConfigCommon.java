package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Config for recipe conditions.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class CriterionTriggerConfigCommon<T extends CriterionTriggerInstance, M extends IModBase> extends ExtendedConfigRegistry<CriterionTriggerConfigCommon<T, M>, CriterionTrigger<T>, M> {

    public CriterionTriggerConfigCommon(M mod, String namedId, CriterionTrigger<T> criterionTrigger) {
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
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.CRITERION_TRIGGER;
    }

    @Override
    public Registry<? super CriterionTrigger<T>> getRegistry() {
        return BuiltInRegistries.TRIGGER_TYPES;
    }
}
