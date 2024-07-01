package org.cyclops.cyclopscore.network;

import com.google.common.collect.Lists;
import io.netty.channel.ChannelHandler.Sharable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.PacketFlow;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.init.ModBase;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Advanced packet handler of {@link PacketBase} instances.
 * @author rubensworks
 */
@Sharable
public final class PacketHandler {

    private final ModBase mod;
    private final List<Pair<CustomPacketPayload.Type<?>, StreamCodec<? super RegistryFriendlyByteBuf, ? extends PacketBase>>> pendingPacketRegistrations;

    public PacketHandler(ModBase mod) {
        this.mod = mod;
        this.pendingPacketRegistrations = Lists.newArrayList();
        mod.getModEventBus().addListener(this::init);
    }

    protected void init(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(mod.getModId())
                .versioned("1.0.0")
                .optional();

        for (Pair<CustomPacketPayload.Type, StreamCodec> pendingPacketRegistration : (List<Pair<CustomPacketPayload.Type, StreamCodec>>) (List) this.pendingPacketRegistrations) {
            this.registerActual(registrar, pendingPacketRegistration.getLeft(), pendingPacketRegistration.getRight());
        }
    }

    public <P extends PacketBase> void register(CustomPacketPayload.Type<P> type, StreamCodec<? super RegistryFriendlyByteBuf, P> codec) {
        this.pendingPacketRegistrations.add(Pair.of(type, codec));
    }

    protected <P extends PacketBase> void registerActual(PayloadRegistrar registrar, CustomPacketPayload.Type<P> type, StreamCodec<? super RegistryFriendlyByteBuf, P> codec) {
        registrar.playBidirectional(
                type,
                codec,
                (packet, ctx) -> {
                    if (ctx.connection().getDirection() == PacketFlow.CLIENTBOUND) {
                        if (packet.isAsync()) {
                            handlePacketClient(ctx, packet);
                        } else {
                            ctx.enqueueWork(() -> handlePacketClient(ctx, packet));
                        }
                    } else {
                        if (packet.isAsync()) {
                            handlePacketServer(ctx, packet);
                        } else {
                            ctx.enqueueWork(() -> handlePacketServer(ctx, packet));
                        }
                    }
                });
    }

    @OnlyIn(Dist.CLIENT)
    public void handlePacketClient(IPayloadContext context, PacketBase packet) {
        packet.actionClient(Minecraft.getInstance().player != null ? Minecraft.getInstance().player.level() : null, Minecraft.getInstance().player);
    }

    public void handlePacketServer(IPayloadContext context, PacketBase packet) {
        packet.actionServer(context.player().level(), (ServerPlayer) context.player());
    }

    /**
     * Send a packet to the server.
     * @param packet The packet.
     */
    public void sendToServer(PacketBase packet) {
        PacketDistributor.sendToServer(packet);
    }

    /**
     * Send a packet to the player.
     * @param packet The packet.
     * @param player The player.
     */
    public void sendToPlayer(PacketBase packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    /**
     * Send a packet to all in the target range.
     * @param packet The packet.
     * @param point The point.
     */
    public void sendToAllAround(PacketBase packet, TargetPoint point) {
        PacketDistributor.sendToPlayersNear(point.level, point.excluded, point.x, point.y, point.z, point.radius, packet);
    }

    /**
     * Send a packet to everything in the given dimension.
     * @param packet The packet.
     * @param dimension The dimension to send to.
     */
    public void sendToDimension(PacketBase packet, ServerLevel dimension) {
        PacketDistributor.sendToPlayersInDimension(dimension, packet);
    }

    /**
     * Send a packet to everything.
     * @param packet The packet.
     */
    public void sendToAll(PacketBase packet) {
        PacketDistributor.sendToAllPlayers(packet);
    }

    public static class PacketCodecException extends RuntimeException {
        public PacketCodecException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    public static record TargetPoint(ServerLevel level, double x, double y, double z, double radius, @Nullable ServerPlayer excluded) {}

}
