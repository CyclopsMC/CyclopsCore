package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.advancements.CriterionTriggerInstance;
import org.cyclops.cyclopscore.config.extendedconfig.CriterionTriggerConfig;

/**
 * The action used for {@link CriterionTriggerConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class CriterionTriggerAction<T extends CriterionTriggerInstance> extends ConfigurableTypeActionForge<CriterionTriggerConfig<T>, CriterionTrigger<T>> {

}
