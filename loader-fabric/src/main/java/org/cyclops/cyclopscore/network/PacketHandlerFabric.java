package org.cyclops.cyclopscore.network;

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.PlayerLookup;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.Vec3;
import org.cyclops.cyclopscore.init.ModBaseFabric;

/**
 * @author rubensworks
 */
public class PacketHandlerFabric implements IPacketHandler {

    private final ModBaseFabric<?> mod;

    public PacketHandlerFabric(ModBaseFabric<?> mod) {
        this.mod = mod;
    }

    @Override
    public <P extends PacketBase> void register(Class<P> clazz, CustomPacketPayload.Type<P> type, StreamCodec<? super RegistryFriendlyByteBuf, P> codec) {
        PayloadTypeRegistry.playS2C().register(type, codec);
        if (this.mod.getModHelpers().getMinecraftHelpers().isClientSide()) {
            ClientPlayNetworking.registerGlobalReceiver(type, (packet, ctx) -> {
                if (packet.isAsync()) {
                    handlePacketClient(ctx, packet);
                } else {
                    ctx.client().execute(() -> handlePacketClient(ctx, packet));
                }
            });
        }

        PayloadTypeRegistry.playC2S().register(type, codec);
        ServerPlayNetworking.registerGlobalReceiver(type, (packet, ctx) -> {
            if (packet.isAsync()) {
                handlePacketServer(ctx, packet);
            } else {
                ctx.server().execute(() -> handlePacketServer(ctx, packet));
            }
        });
    }

    public void handlePacketClient(ClientPlayNetworking.Context context, PacketBase<?> packet) {
        packet.actionClient(context.player().level(), context.player());
    }

    public void handlePacketServer(ServerPlayNetworking.Context context, PacketBase<?> packet) {
        packet.actionServer(context.player().level(), context.player());
    }

    @Override
    public void sendToServer(PacketBase packet) {
        ClientPlayNetworking.send(packet);
    }

    @Override
    public void sendToPlayer(PacketBase packet, ServerPlayer player) {
        ServerPlayNetworking.send(player, packet);
    }

    @Override
    public void sendToAllAroundPoint(PacketBase packet, TargetPoint point) {
        for (ServerPlayer player : PlayerLookup.around(point.level(), new Vec3(point.x(), point.y(), point.z()), point.radius())) {
            if (point.excluded() == null || player != point.excluded()) {
                ServerPlayNetworking.send(player, packet);
            }
        }
    }

    @Override
    public void sendToDimension(PacketBase packet, ServerLevel dimension) {
        for (ServerPlayer player : dimension.players()) {
            ServerPlayNetworking.send(player, packet);
        }
    }

    @Override
    public void sendToAll(PacketBase packet) {
        for (ServerPlayer player : this.mod.getModHelpers().getMinecraftHelpers().getCurrentServer().getPlayerList().getPlayers()) {
            ServerPlayNetworking.send(player, packet);
        }

    }
}
