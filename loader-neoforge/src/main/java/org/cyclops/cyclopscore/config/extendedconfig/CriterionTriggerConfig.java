package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for recipe conditions.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public abstract class CriterionTriggerConfig<T extends CriterionTriggerInstance> extends CriterionTriggerConfigCommon<T, ModBase<?>> {
    public CriterionTriggerConfig(ModBase<?> mod, String namedId, CriterionTrigger<T> criterionTrigger) {
        super(mod, namedId, criterionTrigger);
    }
}
