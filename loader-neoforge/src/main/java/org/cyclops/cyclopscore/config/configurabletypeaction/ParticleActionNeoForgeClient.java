package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.client.particle.ParticleResources;
import net.minecraft.core.particles.ParticleOptions;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

/**
 * @author rubensworks
 */
public class ParticleActionNeoForgeClient {
    public static <T extends ParticleOptions, M extends ModBaseNeoForge<?>> void handleClientSideRegistration(ParticleConfigCommon<T, M> eConfig, RegisterParticleProvidersEvent event) {
        ParticleConfigComponentClient<T, M> clientComponent = eConfig.getClientComponent();
        ParticleProvider<T> factory = clientComponent.getParticleFactory();
        if (factory != null) {
            event.registerSpecial(eConfig.getInstance(), factory);
        }
        ParticleResources.SpriteParticleRegistration<T> metaFactory = clientComponent.getParticleMetaFactory();
        if (metaFactory != null) {
            event.registerSpriteSet(eConfig.getInstance(), metaFactory);
        }
    }
}
