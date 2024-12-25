package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.cyclops.cyclopscore.config.extendedconfig.ConditionConfigNeoForge;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

/**
 * The action used for {@link ConditionConfigNeoForge}.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
public class ConditionActionNeoForge<T extends ICondition> extends ConfigurableTypeActionRegistry<ConditionConfigNeoForge<T>, MapCodec<T>, ModBaseNeoForge<?>> {

}
