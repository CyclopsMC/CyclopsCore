package org.cyclops.cyclopscore.network;

import com.google.common.base.Predicates;
import io.netty.channel.ChannelHandler.Sharable;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.dimension.DimensionType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkDirection;
import net.minecraftforge.fml.network.NetworkEvent;
import net.minecraftforge.fml.network.NetworkRegistry;
import net.minecraftforge.fml.network.PacketDistributor;
import net.minecraftforge.fml.network.simple.SimpleChannel;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.Helpers.IDType;
import org.cyclops.cyclopscore.init.ModBase;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * Advanced packet handler of {@link PacketBase} instances.
 * An alternative would be {@link SimpleChannel}.
 * @author rubensworks
 */
@Sharable
public final class PacketHandler {

    private final ModBase mod;

    private SimpleChannel networkChannel = null;
	
    public PacketHandler(ModBase mod) {
        this.mod = mod;
    }

    public void init() {
        if(networkChannel == null) {
            networkChannel = NetworkRegistry.newSimpleChannel(
                    new ResourceLocation(mod.getModId(), "channel_main"), () -> "1.0.0",
                    Predicates.alwaysTrue(), Predicates.alwaysTrue());
        }
    }
    
    /**
     * Register a new packet.
     * @param packetType The class of the packet.
     * @param <P> The packet type.
     */
    public <P extends PacketBase> void register(Class<P> packetType) {
        int discriminator = Helpers.getNewId(mod, IDType.PACKET);
        try {
            Constructor<P> constructor = packetType.getConstructor();
            networkChannel.registerMessage(discriminator, packetType,
                    (packet, packetBuffer) -> packet.encode(packetBuffer),
                    (packetBuffer) -> {
                        P packet = null;
                        try {
                            packet = constructor.newInstance();
                        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                            e.printStackTrace();
                        }
                        packet.decode(packetBuffer);
                        return packet;
                    },
                    (packet, contextSupplier) -> {
                        NetworkEvent.Context context = contextSupplier.get();
                        if (context.getDirection().getReceptionSide().isClient()) {
                            if (packet.isAsync()) {
                                handlePacketClient(context, packet);
                            } else {
                                context.enqueueWork(() -> handlePacketClient(context, packet));
                            }
                        } else {
                            if (packet.isAsync()) {
                                handlePacketServer(context, packet);
                            } else {
                                context.enqueueWork(() -> handlePacketServer(context, packet));
                            }
                        }
                        context.setPacketHandled(true);
                    });
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
            CyclopsCore.clog(Level.ERROR, "Could not find a default constructor for packet " + packetType.getName());
        }
    }

    @OnlyIn(Dist.CLIENT)
    public void handlePacketClient(NetworkEvent.Context context, PacketBase packet) {
        packet.actionClient(Minecraft.getInstance().player.world, Minecraft.getInstance().player);
    }

    public void handlePacketServer(NetworkEvent.Context context, PacketBase packet) {
        packet.actionServer(context.getSender().getServerWorld(), context.getSender());
    }
    
    /**
     * Send a packet to the server.
     * @param packet The packet.
     */
    public void sendToServer(PacketBase packet) {
        networkChannel.sendToServer(packet);
    }
    
    /**
     * Send a packet to the player.
     * @param packet The packet.
     * @param player The player.
     */
    public void sendToPlayer(PacketBase packet, ServerPlayerEntity player) {
        networkChannel.sendTo(packet, player.connection.netManager, NetworkDirection.PLAY_TO_CLIENT);
    }

    /**
     * Send a packet to all in the target range.
     * @param packet The packet.
     * @param point The area to send to.
     */
    public void sendToAllAround(PacketBase packet, PacketDistributor.TargetPoint point) {
        PacketDistributor.PacketTarget target = PacketDistributor.NEAR.with(() -> point);
        target.send(networkChannel.toVanillaPacket(packet, target.getDirection()));
    }

    /**
     * Send a packet to everything in the given dimension.
     * @param packet The packet.
     * @param dimension The dimension to send to.
     */
    public void sendToDimension(PacketBase packet, DimensionType dimension) {
        PacketDistributor.PacketTarget target = PacketDistributor.DIMENSION.with(() -> dimension);
        target.send(networkChannel.toVanillaPacket(packet, target.getDirection()));
    }
    
    /**
     * Send a packet to everything.
     * @param packet The packet.
     */
    public void sendToAll(PacketBase packet) {
        PacketDistributor.PacketTarget target = PacketDistributor.ALL.with(() -> null);
        target.send(networkChannel.toVanillaPacket(packet, target.getDirection()));
    }
    
}
