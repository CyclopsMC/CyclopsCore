package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for capabilities.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class CapabilityConfigCommon<T, M extends IModBase> extends ExtendedConfigCommon<CapabilityConfigCommon<T, M>, T, M> {

    private final Function<CapabilityConfigCommon<T, M>, T> registrar;

    /**
     * Make a new instance.
     * @param mod The mod
     * @param namedId The unique name ID for the configurable.
     * @param registrar The capability registrar.
     */
    public CapabilityConfigCommon(M mod, String namedId, Function<CapabilityConfigCommon<T, M>, T> registrar) {
        super(mod, namedId, null);
        this.registrar = registrar;
    }

    @Override
    public String getTranslationKey() {
        return "capability." + getMod().getModId() + "." + getNamedId();
    }

    //Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.CAPABILITY;
    }

    public Function<CapabilityConfigCommon<T, M>, T> getRegistrar() {
        return registrar;
    }

    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(getMod().getModId(), getNamedId());
    }
}
