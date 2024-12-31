package org.cyclops.cyclopscore.proxy;

import com.mojang.blaze3d.vertex.VertexConsumer;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.particle.Particle;
import org.cyclops.cyclopscore.CyclopsCoreFabric;
import org.cyclops.cyclopscore.client.gui.GuiMainMenuExtensionDevWorldFabricRegistrar;
import org.cyclops.cyclopscore.client.particle.ParticleBlur;
import org.cyclops.cyclopscore.events.IParticleEngineRenderEvent;
import org.cyclops.cyclopscore.init.ModBaseFabric;
import org.cyclops.cyclopscore.item.ItemInformationProviderFabric;

import java.util.Queue;

/**
 * Proxy for the client side.
 *
 * @author rubensworks
 *
 */
public class ClientProxyFabric extends ClientProxyComponentFabric {

    public ClientProxyFabric() {
        super(new CommonProxyFabric());
    }

    @Override
    public ModBaseFabric<?> getMod() {
        return CyclopsCoreFabric._instance;
    }

    @Override
    public void registerEventHooks() {
        super.registerEventHooks();

        ItemTooltipCallback.EVENT.register(ItemInformationProviderFabric::onTooltip);
        ScreenEvents.AFTER_INIT.register(GuiMainMenuExtensionDevWorldFabricRegistrar::afterInit);
        IParticleEngineRenderEvent.EVENT.register((particleEngine, camera, partialTick, bufferSource) -> {
            Queue<Particle> queue = particleEngine.particles.get(ParticleBlur.PARTICLE_RENDER_TYPE);
            if (queue != null && !queue.isEmpty()) {
                /* Below is copied and adapted from ParticleEngine */
                VertexConsumer vertexConsumer = bufferSource.getBuffer(ParticleBlur.PARTICLE_RENDER_TYPE.renderType());
                for (Particle particle : queue) {
                    try {
                        particle.render(vertexConsumer, camera, partialTick);
                    } catch (Throwable throwable) {
                        CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering Cyclops Core Particle");
                        CrashReportCategory crashreportcategory = crashreport.addCategory("Particle being rendered");
                        crashreportcategory.setDetail("Particle", particle::toString);
                        crashreportcategory.setDetail("Particle Type", ParticleBlur.PARTICLE_RENDER_TYPE.renderType()::toString);
                        throw new ReportedException(crashreport);
                    }
                }

                bufferSource.endBatch();
            }
        });
    }
}
