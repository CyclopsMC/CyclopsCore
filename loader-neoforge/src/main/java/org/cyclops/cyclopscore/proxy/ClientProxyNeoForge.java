package org.cyclops.cyclopscore.proxy;

import net.minecraft.resources.Identifier;
import net.neoforged.neoforge.client.event.RegisterItemModelsEvent;
import net.neoforged.neoforge.client.event.RegisterRenderPipelinesEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.client.particle.ParticleBlur;
import org.cyclops.cyclopscore.client.render.model.ItemDynamicItemAndBlockModel;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;
import org.cyclops.cyclopscore.item.ItemInformationProviderNeoForge;

/**
 * Proxy for the client side.
 *
 * @author rubensworks
 *
 */
public class ClientProxyNeoForge extends ClientProxyComponent {

    public ClientProxyNeoForge() {
        super(new CommonProxyNeoForge());

        NeoForge.EVENT_BUS.addListener(ItemInformationProviderNeoForge::onTooltip);
        getMod().getModEventBus().addListener((RegisterItemModelsEvent event) -> event.register(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "dynamic_item_and_block_model"), ItemDynamicItemAndBlockModel.Unbaked.MAP_CODEC));
        getMod().getModEventBus().addListener((RegisterRenderPipelinesEvent event) -> event.registerPipeline(ParticleBlur.RENDER_PIPELINE));
    }

    @Override
    public ModBaseNeoForge getMod() {
        return CyclopsCoreNeoForge._instance;
    }

    @Override
    public void registerRenderers() {
        super.registerRenderers();
    }

}
