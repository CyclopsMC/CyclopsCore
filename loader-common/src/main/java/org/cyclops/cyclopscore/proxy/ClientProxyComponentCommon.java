package org.cyclops.cyclopscore.proxy;

import com.google.common.collect.Maps;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderers;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.network.IPacketHandler;

import java.util.Map;

/**
 * Base proxy for the client side.
 *
 * @author rubensworks
 *
 */
public abstract class ClientProxyComponentCommon extends CommonProxyComponentCommon implements ICommonProxyCommon, IClientProxyCommon {

    private final CommonProxyComponentCommon commonProxyComponent;
    protected final Map<BlockEntityType, BlockEntityRendererProvider> blockEntityRenderers = Maps.newHashMap();
    private final KeyMapping.Category mainKeyCategory;

    public ClientProxyComponentCommon(CommonProxyComponentCommon commonProxyComponent) {
        this.commonProxyComponent = commonProxyComponent;
        this.mainKeyCategory = new KeyMapping.Category(ResourceLocation.fromNamespaceAndPath(commonProxyComponent.getMod().getModId(), "main"));
    }

    public CommonProxyComponentCommon getCommonProxyComponent() {
        return commonProxyComponent;
    }

    public Map<BlockEntityType, BlockEntityRendererProvider> getBlockEntityRenderers() {
        return blockEntityRenderers;
    }

    @Override
    public KeyMapping.Category getMainKeyCategory() {
        return mainKeyCategory;
    }

    @Override
    public <T extends BlockEntity, S extends BlockEntityRenderState> void registerRenderer(BlockEntityType<? extends T> blockEntityType, BlockEntityRendererProvider<T, S> rendererFactory) {
        blockEntityRenderers.put(blockEntityType, rendererFactory);
    }

    @Override
    public void registerRenderers() {
        // Special BlockEntity renderers
        for (Map.Entry<BlockEntityType, BlockEntityRendererProvider> entry : blockEntityRenderers.entrySet()) {
            BlockEntityRenderers.register(entry.getKey(), entry.getValue());
            getMod().getLoggerHelper().log(Level.TRACE, String.format("Registered %s special renderer %s", entry.getKey(), entry.getValue()));
        }
    }

    @Override
    public void registerTickHandlers() {
        commonProxyComponent.registerTickHandlers();
        getMod().getLoggerHelper().log(Level.TRACE, "Registered tick handlers");
    }

    @Override
    public void registerEventHooks() {
        commonProxyComponent.registerEventHooks();
        getMod().getLoggerHelper().log(Level.TRACE, "Registered event hooks");
    }

    @Override
    public void registerPackets(IPacketHandler packetHandler) {
        commonProxyComponent.registerPackets(packetHandler);
        getMod().getLoggerHelper().log(Level.TRACE, "Registered packet handlers");
    }
}
