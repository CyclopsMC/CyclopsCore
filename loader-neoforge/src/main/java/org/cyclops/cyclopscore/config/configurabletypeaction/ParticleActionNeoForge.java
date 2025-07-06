package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.core.particles.ParticleOptions;
import net.neoforged.neoforge.client.event.RegisterParticleProvidersEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

/**
 * @author rubensworks
 */
public class ParticleActionNeoForge<T extends ParticleOptions, M extends ModBaseNeoForge<?>> extends ParticleActionCommon<T, M> {

    @Override
    public void onRegisterModInit(ParticleConfigCommon<T, M> eConfig) {
        super.onRegisterModInit(eConfig);

        if (eConfig.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            eConfig.getMod().getModEventBus().addListener((RegisterParticleProvidersEvent event) -> ParticleActionNeoForgeClient.handleClientSideRegistration(eConfig, event));
        }
    }
}
