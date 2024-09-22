package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * @author rubensworks
 */
public class ParticleActionForge<T extends ParticleOptions, M extends ModBaseForge<?>> extends ParticleActionCommon<T, M> {

    @Override
    public void onRegisterModInit(ParticleConfigCommon<T, M> eConfig) {
        super.onRegisterModInit(eConfig);

        if (eConfig.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            eConfig.getMod().getModEventBus().addListener((RegisterParticleProvidersEvent event) -> handleClientSideRegistration(eConfig, event));
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static <T extends ParticleOptions, M extends ModBaseForge<?>> void handleClientSideRegistration(ParticleConfigCommon<T, M> eConfig, RegisterParticleProvidersEvent event) {
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
