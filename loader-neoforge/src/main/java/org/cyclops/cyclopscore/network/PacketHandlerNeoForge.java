package org.cyclops.cyclopscore.network;

import com.google.common.collect.Lists;
import io.netty.channel.ChannelHandler.Sharable;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.client.network.ClientPacketDistributor;
import net.neoforged.neoforge.client.network.event.RegisterClientPayloadHandlersEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.PayloadRegistrar;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

import java.util.List;

/**
 * Advanced packet handler of {@link PacketBase} instances.
 * @author rubensworks
 */
@Sharable
public final class PacketHandlerNeoForge implements IPacketHandler {

    private final ModBaseNeoForge mod;
    private final List<Pair<CustomPacketPayload.Type<?>, StreamCodec<? super RegistryFriendlyByteBuf, ? extends PacketBase>>> pendingPacketRegistrations;

    public PacketHandlerNeoForge(ModBaseNeoForge mod) {
        this.mod = mod;
        this.pendingPacketRegistrations = Lists.newArrayList();
        mod.getModEventBus().addListener(this::init);
        mod.getModEventBus().addListener(this::initClient);
    }

    protected void init(RegisterPayloadHandlersEvent event) {
        PayloadRegistrar registrar = event.registrar(mod.getModId())
                .versioned("1.0.0")
                .optional();

        for (Pair<CustomPacketPayload.Type, StreamCodec> pendingPacketRegistration : (List<Pair<CustomPacketPayload.Type, StreamCodec>>) (List) this.pendingPacketRegistrations) {
            this.registerActual(registrar, pendingPacketRegistration.getLeft(), pendingPacketRegistration.getRight());
        }
    }

    protected void initClient(RegisterClientPayloadHandlersEvent event) {
        for (Pair<CustomPacketPayload.Type, StreamCodec> pendingPacketRegistration : (List<Pair<CustomPacketPayload.Type, StreamCodec>>) (List) this.pendingPacketRegistrations) {
            this.registerActualClient(event, pendingPacketRegistration.getLeft(), pendingPacketRegistration.getRight());
        }
    }

    @Override
    public <P extends PacketBase> void register(Class<P> clazz, CustomPacketPayload.Type<P> type, StreamCodec<? super RegistryFriendlyByteBuf, P> codec) {
        this.pendingPacketRegistrations.add(Pair.of(type, codec));
    }

    protected <P extends PacketBase> void registerActual(PayloadRegistrar registrar, CustomPacketPayload.Type<P> type, StreamCodec<? super RegistryFriendlyByteBuf, P> codec) {
        registrar.playBidirectional(
                type,
                codec,
                (packet, ctx) -> {
                    if (packet.isAsync()) {
                        handlePacketServer(ctx, packet);
                    } else {
                        ctx.enqueueWork(() -> handlePacketServer(ctx, packet));
                    }
                });
    }

    protected <P extends PacketBase> void registerActualClient(RegisterClientPayloadHandlersEvent event, CustomPacketPayload.Type<P> type, StreamCodec<? super RegistryFriendlyByteBuf, P> codec) {
        event.register(type, (packet, ctx) -> {
            if (packet.isAsync()) {
                handlePacketClient(ctx, packet);
            } else {
                ctx.enqueueWork(() -> handlePacketClient(ctx, packet));
            }
        });
    }

    public void handlePacketClient(IPayloadContext context, PacketBase packet) {
        packet.actionClient(context.player().level(), context.player());
    }

    public void handlePacketServer(IPayloadContext context, PacketBase packet) {
        packet.actionServer(context.player().level(), (ServerPlayer) context.player());
    }

    @Override
    public void sendToServer(PacketBase packet) {
        ClientPacketDistributor.sendToServer(packet);
    }

    @Override
    public void sendToPlayer(PacketBase packet, ServerPlayer player) {
        PacketDistributor.sendToPlayer(player, packet);
    }

    @Override
    public void sendToAllAroundPoint(PacketBase packet, IPacketHandler.TargetPoint point) {
        PacketDistributor.sendToPlayersNear(point.level(), point.excluded(), point.x(), point.y(), point.z(), point.radius(), packet);
    }

    @Override
    public void sendToDimension(PacketBase packet, ServerLevel dimension) {
        PacketDistributor.sendToPlayersInDimension(dimension, packet);
    }

    @Override
    public void sendToAll(PacketBase packet) {
        PacketDistributor.sendToAllPlayers(packet);
    }

}
