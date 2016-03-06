package org.cyclops.cyclopscore.network;

import io.netty.channel.ChannelHandler.Sharable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.Packet;
import net.minecraft.util.IThreadListener;
import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.network.NetworkRegistry;
import net.minecraftforge.fml.common.network.simpleimpl.IMessage;
import net.minecraftforge.fml.common.network.simpleimpl.IMessageHandler;
import net.minecraftforge.fml.common.network.simpleimpl.MessageContext;
import net.minecraftforge.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.Helpers.IDType;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Advanced packet handler of {@link PacketBase} instances.
 * An alternative would be {@link SimpleNetworkWrapper}.
 * Partially based on the SecretRooms mod packet handling:
 * https://github.com/AbrarSyed/SecretRoomsMod-forge
 * @author rubensworks
 *
 */
@Sharable
public final class PacketHandler {

    private SimpleNetworkWrapper networkWrapper = null;
    @SideOnly(Side.CLIENT)
    private HandlerClient handlerClient;
    private HandlerServer handlerServer;
    private final ModBase mod;
	
    public PacketHandler(ModBase mod) {
        this.mod = mod;
    }

    public void init() {
        if(networkWrapper == null) {
            networkWrapper = NetworkRegistry.INSTANCE.newSimpleChannel(mod.getModId());
            if(MinecraftHelpers.isClientSide()) {
                handlerClient = new HandlerClient();
            }
            handlerServer = new HandlerServer();
        }
    }
    
    /**
     * Register a new packet.
     * @param packetType The class of the packet.
     */
    public void register(Class<? extends PacketBase> packetType) {
        int discriminator = Helpers.getNewId(mod, IDType.PACKET);
        if(MinecraftHelpers.isClientSide()) {
            networkWrapper.registerMessage(handlerClient, packetType, discriminator, Side.CLIENT);
        }
        networkWrapper.registerMessage(handlerServer, packetType, discriminator, Side.SERVER);
    }
    
    /**
     * Send a packet to the server.
     * @param packet The packet.
     */
    public void sendToServer(PacketBase packet) {
        networkWrapper.sendToServer(packet);
    }
    
    /**
     * Send a packet to the player.
     * @param packet The packet.
     * @param player The player.
     */
    public void sendToPlayer(PacketBase packet, EntityPlayerMP player) {
        networkWrapper.sendTo(packet, player);
    }

    /**
     * Send a packet to all in the target range.
     * @param packet The packet.
     * @param point The area to send to.
     */
    public void sendToAllAround(PacketBase packet, NetworkRegistry.TargetPoint point) {
        networkWrapper.sendToAllAround(packet, point);
    }

    /**
     * Send a packet to everything in the given dimension.
     * @param packet The packet.
     * @param dimension The dimension to send to.
     */
    public void sendToDimension(PacketBase packet, int dimension) {
        networkWrapper.sendToDimension(packet, dimension);
    }
    
    /**
     * Send a packet to everything.
     * @param packet The packet.
     */
    public void sendToAll(PacketBase packet) {
        networkWrapper.sendToAll(packet);
    }
    
    /**
     * Convert the given packet to a minecraft packet.
     * @param packet The packet.
     * @return The minecraft packet.
     */
    public Packet toMcPacket(PacketBase packet) {
        return networkWrapper.getPacketFrom(packet);
    }
    
    @Sharable
    @SideOnly(Side.CLIENT)
    private static final class HandlerClient implements IMessageHandler<PacketBase, IMessage> {

        @Override
        public IMessage onMessage(final PacketBase packet, MessageContext ctx) {
            final Minecraft mc = Minecraft.getMinecraft();
            IThreadListener thread = FMLCommonHandler.instance().getWorldThread(ctx.getClientHandler());
            if (packet.isAsync()) {
                packet.actionClient(mc.theWorld, mc.thePlayer);
            } else {
                thread.addScheduledTask(new Runnable() {
                    public void run() {
                        packet.actionClient(mc.theWorld, mc.thePlayer);
                    }
                });
            }
            return null;
        }
    }

    @Sharable
    private static final class HandlerServer implements IMessageHandler<PacketBase, IMessage> {

        @Override
        public IMessage onMessage(PacketBase packet, MessageContext ctx) {
            if (ctx.side == Side.CLIENT) {
                // nothing on the client thread
                return null;
            }

            EntityPlayerMP player = ctx.getServerHandler().playerEntity;
            packet.actionServer(player.worldObj, player);
            return null;
        }
    }
    
}
