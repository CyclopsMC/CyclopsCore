package org.cyclops.cyclopscore.config.extendedconfig;

import org.cyclops.cyclopscore.config.ConfigurableProperty;
import org.cyclops.cyclopscore.config.ConfigurablePropertyCommon;
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

    @Override
    public ModBase<?> getMod() {
        return super.getMod();
    }

    @Override
    public String getConfigPropertyPrefix(ConfigurablePropertyCommon annotation) {
        return super.getConfigPropertyPrefix(annotation);
    }

    public String getConfigPropertyPrefixPublic(ConfigurableProperty annotation) {
        return getConfigPropertyPrefix(annotation);
    }

    /**
     * @param annotation The annotation to define the prefix for.
     * @return The prefix that will be used inside the config file for {@link ConfigurableProperty}'s.
     */
    protected String getConfigPropertyPrefix(ConfigurableProperty annotation) {
        return annotation.namedId().isEmpty() ? this.getNamedId() : annotation.namedId();
    }
}
