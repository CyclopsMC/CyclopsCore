package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.cyclops.cyclopscore.config.extendedconfig.ConditionConfig;

/**
 * The action used for {@link ConditionConfig}.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
// TODO: append NeoForge to name in next major
public class ConditionAction<T extends ICondition> extends ConfigurableTypeActionForge<ConditionConfig<T>, MapCodec<T>> {

}
