package org.cyclops.cyclopscore.proxy;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferUploader;
import com.mojang.blaze3d.vertex.MeshData;
import com.mojang.blaze3d.vertex.Tesselator;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.CrashReport;
import net.minecraft.CrashReportCategory;
import net.minecraft.ReportedException;
import net.minecraft.client.particle.Particle;
import net.minecraft.client.renderer.GameRenderer;
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
    }

    @Override
    public void registerRenderers() {
        super.registerRenderers();

        // Force our custom render type to be rendered
        IParticleEngineRenderEvent.EVENT.register((particleEngine, pLightTexture, pCamera, pPartialTick) -> {
            ParticleBlur.RenderType renderType = ParticleBlur.RENDER_TYPE;
            Queue<Particle> queue = particleEngine.particles.get(renderType);
            if (queue != null && !queue.isEmpty()) {
                /* Below is copied and adapted from ParticleEngine */
                pLightTexture.turnOnLightLayer();
                RenderSystem.enableDepthTest();

                RenderSystem.setShader(GameRenderer::getParticleShader);
                Tesselator tesselator = Tesselator.getInstance();
                BufferBuilder bufferbuilder = renderType.begin(tesselator, particleEngine.textureManager);
                if (bufferbuilder != null) {
                    for (Particle particle : queue) {
                        try {
                            particle.render(bufferbuilder, pCamera, pPartialTick);
                        } catch (Throwable throwable) {
                            CrashReport crashreport = CrashReport.forThrowable(throwable, "Rendering Cyclops Core Particle");
                            CrashReportCategory crashreportcategory = crashreport.addCategory("Particle being rendered");
                            crashreportcategory.setDetail("Particle", particle::toString);
                            crashreportcategory.setDetail("Particle Type", renderType::toString);
                            throw new ReportedException(crashreport);
                        }
                    }

                    MeshData meshdata = bufferbuilder.build();
                    if (meshdata != null) {
                        BufferUploader.drawWithShader(meshdata);
                    }
                }

                RenderSystem.depthMask(true);
                RenderSystem.disableBlend();
                pLightTexture.turnOffLightLayer();
            }
        });
    }
}
