package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.conditions.ICondition;
import org.cyclops.cyclopscore.config.extendedconfig.ConditionConfig;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * The action used for {@link ConditionConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
// TODO: append NeoForge to name in next major
public class ConditionAction<T extends ICondition> extends ConfigurableTypeActionRegistry<ConditionConfig<T>, MapCodec<T>, ModBase<?>> {

}
