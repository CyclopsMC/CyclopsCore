package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.component.DataComponentType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.UnaryOperator;

/**
 * Config for data component types.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public abstract class DataComponentConfig<T> extends DataComponentConfigCommon<T, ModBase<?>> {
    public DataComponentConfig(ModBase<?> mod, String namedId, UnaryOperator<DataComponentType.Builder<T>> builder) {
        super(mod, namedId, builder);
    }
}
