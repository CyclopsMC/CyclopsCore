package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraftforge.client.event.RegisterParticleProvidersEvent;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigCommon;
import org.cyclops.cyclopscore.init.ModBaseForge;

/**
 * @author rubensworks
 */
public class ParticleActionForge<T extends ParticleOptions, M extends ModBaseForge<?>> extends ParticleActionCommon<T, M> {

    @Override
    public void onRegisterModInit(ParticleConfigCommon<T, M> eConfig) {
        super.onRegisterModInit(eConfig);

        if (eConfig.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            RegisterParticleProvidersEvent.BUS.addListener((RegisterParticleProvidersEvent event) -> ParticleActionForgeClient.handleClientSideRegistration(eConfig, event));
        }
    }

}
