package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.cyclops.cyclopscore.config.extendedconfig.ConditionConfig;

/**
 * The action used for {@link ConditionConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class ConditionAction<T extends ICondition> extends ConfigurableTypeActionForge<ConditionConfig<T>, Codec<T>> {

}
