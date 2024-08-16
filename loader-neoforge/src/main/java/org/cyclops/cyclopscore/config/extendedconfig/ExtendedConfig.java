package org.cyclops.cyclopscore.config.extendedconfig;

import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * @author rubensworks
 */
@Deprecated // TODO: rm in next major
public abstract class ExtendedConfig<C extends ExtendedConfig<C, I>, I> extends ExtendedConfigCommon<C, I, ModBase<?>> {
    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public ExtendedConfig(ModBase<?> mod, String namedId, Function<C, ? extends I> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }
}
