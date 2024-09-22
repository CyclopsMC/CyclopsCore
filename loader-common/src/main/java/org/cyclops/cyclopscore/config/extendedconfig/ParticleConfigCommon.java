package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for particle effects.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
public abstract class ParticleConfigCommon<T extends ParticleOptions, M extends IModBase> extends ExtendedConfigRegistry<ParticleConfigCommon<T, M>, ParticleType<T>, M> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public ParticleConfigCommon(M mod, String namedId, Function<ParticleConfigCommon<T, M>, ? extends ParticleType<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "gui." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.PARTICLE;
    }

    @Override
    public Registry<? super ParticleType<T>> getRegistry() {
        return BuiltInRegistries.PARTICLE_TYPE;
    }

    public abstract ParticleConfigComponentClient<T, M> getClientComponent();

}
