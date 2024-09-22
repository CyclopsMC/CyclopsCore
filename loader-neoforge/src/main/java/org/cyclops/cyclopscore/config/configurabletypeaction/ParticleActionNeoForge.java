package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * @author rubensworks
 */
public class ParticleActionNeoForge<T extends ParticleOptions, M extends ModBase<?>> extends ParticleActionCommon<T, M> {

    @Override
    public void onRegisterModInit(ParticleConfigCommon<T, M> eConfig) {
        super.onRegisterModInit(eConfig);

        if (eConfig.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            eConfig.getMod().getModEventBus().addListener((RegisterParticleProvidersEvent event) -> handleClientSideRegistration(eConfig, event));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static <T extends ParticleOptions, M extends ModBase<?>> void handleClientSideRegistration(ParticleConfigCommon<T, M> eConfig, RegisterParticleProvidersEvent event) {
        ParticleConfigComponentClient<T, M> clientComponent = eConfig.getClientComponent();
        ParticleProvider<T> factory = clientComponent.getParticleFactory();
        if (factory != null) {
            event.registerSpecial(eConfig.getInstance(), factory);
        }
        ParticleEngine.SpriteParticleRegistration<T> metaFactory = clientComponent.getParticleMetaFactory();
        if (metaFactory != null) {
            event.registerSpriteSet(eConfig.getInstance(), metaFactory);
        }
    }
}
