package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for capabilities.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public abstract class CapabilityConfig<T> extends ExtendedConfig<CapabilityConfig<T>, T> {

    private final Function<CapabilityConfig<T>, T> registrar;

    public CapabilityConfig(ModBase<?> mod, String namedId, Function<CapabilityConfig<T>, T> registrar) {
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
    public ConfigurableType getConfigurableType() {
        return ConfigurableTypesNeoForge.D_CAPABILITY;
    }

    public Function<CapabilityConfig<T>, T> getRegistrar() {
        return registrar;
    }

    public ResourceLocation getId() {
        return ResourceLocation.fromNamespaceAndPath(getMod().getModId(), getNamedId());
    }
}
