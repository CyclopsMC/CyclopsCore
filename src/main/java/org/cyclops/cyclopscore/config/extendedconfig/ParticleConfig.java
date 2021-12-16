package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.Minecraft;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ParticleFactoryRegisterEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
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
        FMLJavaModLoadingContext.get().getModEventBus().register(this);
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
    public IForgeRegistry<? super ParticleType<T>> getRegistry() {
        return ForgeRegistries.PARTICLE_TYPES;
    }

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public abstract ParticleProvider<T> getParticleFactory();

    @OnlyIn(Dist.CLIENT)
    @Nullable
    public abstract ParticleEngine.SpriteParticleRegistration<T> getParticleMetaFactory();

    @OnlyIn(Dist.CLIENT)
    @SubscribeEvent
    public void onParticleFactoryRegister(ParticleFactoryRegisterEvent event) {
        ParticleProvider<T> factory = getParticleFactory();
        if (factory != null) {
            Minecraft.getInstance().particleEngine.register(getInstance(), factory);
        }
        ParticleEngine.SpriteParticleRegistration<T> metaFactory = getParticleMetaFactory();
        if (metaFactory != null) {
            Minecraft.getInstance().particleEngine.register(getInstance(), metaFactory);
        }
    }

}
