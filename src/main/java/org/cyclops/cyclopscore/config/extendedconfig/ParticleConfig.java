package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.Registry;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Config for recipe serializers.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class ParticleConfig<T extends ParticleOptions> extends ExtendedConfigForge<ParticleConfig<T>, ParticleType<T>> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public ParticleConfig(ModBase mod, String namedId, Function<ParticleConfig<T>, ? extends ParticleType<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
        mod.getModEventBus().addListener(this::onParticleFactoryRegister);
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
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.PARTICLE;
    }

    @Override
    public Registry<? super ParticleType<T>> getRegistry() {
        return BuiltInRegistries.PARTICLE_TYPE;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public abstract ParticleProvider<T> getParticleFactory();

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public abstract ParticleEngine.SpriteParticleRegistration<T> getParticleMetaFactory();

    @OnlyIn(Dist.CLIENT)
    public void onParticleFactoryRegister(RegisterParticleProvidersEvent event) {
        ParticleProvider<T> factory = getParticleFactory();
        if (factory != null) {
            event.registerSpecial(getInstance(), factory);
        }
        ParticleEngine.SpriteParticleRegistration<T> metaFactory = getParticleMetaFactory();
        if (metaFactory != null) {
            event.registerSpriteSet(getInstance(), metaFactory);
        }
    }

}
