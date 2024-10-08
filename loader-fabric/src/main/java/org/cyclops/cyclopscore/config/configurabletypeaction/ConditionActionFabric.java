package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.fabricmc.fabric.api.resource.conditions.v1.ResourceCondition;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditionType;
import net.fabricmc.fabric.api.resource.conditions.v1.ResourceConditions;
import org.cyclops.cyclopscore.config.extendedconfig.ConditionConfigFabric;
import org.cyclops.cyclopscore.init.ModBaseFabric;

/**
 * The action used for {@link ConditionConfigFabric}.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
public class ConditionActionFabric<T extends ResourceCondition> extends ConfigurableTypeActionCommon<ConditionConfigFabric<T>, ResourceConditionType<T>, ModBaseFabric<?>> {

    @Override
    public void onRegisterForge(ConditionConfigFabric<T> eConfig) {
        super.onRegisterForge(eConfig);
        ResourceConditions.register(eConfig.getInstance());
    }
}
