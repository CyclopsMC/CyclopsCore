package org.cyclops.cyclopscore.proxy;

import net.minecraft.client.renderer.RenderPipelines;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import org.cyclops.cyclopscore.CyclopsCoreForge;
import org.cyclops.cyclopscore.client.particle.ParticleBlur;
import org.cyclops.cyclopscore.init.ModBaseForge;
import org.cyclops.cyclopscore.item.ItemInformationProviderForge;

/**
 * Proxy for the client side.
 *
 * @author rubensworks
 *
 */
public class ClientProxyForge extends ClientProxyComponentForge {

    public ClientProxyForge() {
        super(new CommonProxyForge());

        ItemTooltipEvent.BUS.addListener(ItemInformationProviderForge::onTooltip);
        RenderPipelines.register(ParticleBlur.RENDER_PIPELINE);
    }

    @Override
    public ModBaseForge<?> getMod() {
        return CyclopsCoreForge._instance;
    }

}
