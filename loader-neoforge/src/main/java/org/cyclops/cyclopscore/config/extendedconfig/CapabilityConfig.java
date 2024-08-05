package org.cyclops.cyclopscore.config.extendedconfig;

import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for capabilities.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public abstract class CapabilityConfig<T> extends CapabilityConfigCommon<T, ModBase<?>> {
    public CapabilityConfig(ModBase<?> mod, String namedId, Function<CapabilityConfigCommon<T, ModBase<?>>, T> registrar) {
        super(mod, namedId, null);
    }
}
