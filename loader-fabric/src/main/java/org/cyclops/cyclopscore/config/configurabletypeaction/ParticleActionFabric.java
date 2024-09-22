package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.particle.v1.FabricSpriteProvider;
import net.fabricmc.fabric.api.client.particle.v1.ParticleFactoryRegistry;
import net.minecraft.client.particle.ParticleEngine;
import net.minecraft.client.particle.ParticleProvider;
import net.minecraft.core.particles.ParticleOptions;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigCommon;
import org.cyclops.cyclopscore.config.extendedconfig.ParticleConfigComponentClient;
import org.cyclops.cyclopscore.init.ModBaseFabric;

/**
 * @author rubensworks
 */
public class ParticleActionFabric<T extends ParticleOptions, M extends ModBaseFabric<?>> extends ParticleActionCommon<T, M> {

    @Override
    public void onRegisterModInit(ParticleConfigCommon<T, M> eConfig) {
        super.onRegisterModInit(eConfig);

        if (eConfig.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            handleClientSideRegistration(eConfig);
        }
    }

    @Environment(EnvType.CLIENT)
    public static <T extends ParticleOptions, M extends ModBaseFabric<?>> void handleClientSideRegistration(ParticleConfigCommon<T, M> eConfig) {
        ParticleConfigComponentClient<T, M> clientComponent = eConfig.getClientComponent();
        ParticleProvider<T> factory = clientComponent.getParticleFactory();
        if (factory != null) {
            ParticleFactoryRegistry.getInstance().register(eConfig.getInstance(), factory);
        }
        ParticleEngine.SpriteParticleRegistration<T> metaFactory = clientComponent.getParticleMetaFactory();
        if (metaFactory != null) {
            ParticleFactoryRegistry.getInstance().register(eConfig.getInstance(), new ParticleFactoryRegistry.PendingParticleFactory<T>() {
                @Override
                public ParticleProvider<T> create(FabricSpriteProvider provider) {
                    return metaFactory.create(provider);
                }
            });
        }
    }
}
