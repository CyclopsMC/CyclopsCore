package org.cyclops.cyclopscore.neywork;

import com.google.common.collect.Lists;
import io.netty.channel.ChannelHandler.Sharable;
import net.minecraft.client.Minecraft;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.network.CustomPayloadEvent;
import net.minecraftforge.network.Channel;
import net.minecraftforge.network.ChannelBuilder;
import net.minecraftforge.network.PacketDistributor;
import net.minecraftforge.network.SimpleChannel;
import net.minecraftforge.network.simple.SimpleFlow;
import org.apache.commons.lang3.tuple.Triple;
import org.cyclops.cyclopscore.init.ModBaseForge;
import org.cyclops.cyclopscore.network.IPacketHandler;
import org.cyclops.cyclopscore.network.PacketBase;
import org.jetbrains.annotations.Nullable;

import java.util.List;

/**
 * Advanced packet handler of {@link PacketBase} instances.
 * @author rubensworks
 */
@Sharable
public final class PacketHandlerForge implements IPacketHandler {

    private final ModBaseForge<?> mod;
    private final List<Triple<Class<?>, CustomPacketPayload.Type<?>, StreamCodec<? super RegistryFriendlyByteBuf, ? extends PacketBase>>> pendingPacketRegistrations;
    private SimpleChannel networkChannel = null;

    public PacketHandlerForge(ModBaseForge<?> mod) {
        this.mod = mod;
        this.pendingPacketRegistrations = Lists.newArrayList();
    }

    public void init() {
        if(networkChannel == null) {
            SimpleFlow<RegistryFriendlyByteBuf, Object> channelBuilder = ChannelBuilder
                    .named(ResourceLocation.fromNamespaceAndPath(mod.getModId(), "channel_main"))
                    .clientAcceptedVersions(Channel.VersionTest.exact(1))
                    .serverAcceptedVersions(Channel.VersionTest.exact(1))
                    .networkProtocolVersion(1)
                    .simpleChannel()
                        .play()
                            .bidirectional();
            for (Triple<Class, CustomPacketPayload.Type, StreamCodec> pendingPacketRegistration : (List<Triple<Class, CustomPacketPayload.Type, StreamCodec>>) (List) this.pendingPacketRegistrations) {
                channelBuilder = registerActual(channelBuilder, pendingPacketRegistration.getLeft(), pendingPacketRegistration.getMiddle(), pendingPacketRegistration.getRight());
            }
            networkChannel = channelBuilder.build();
        }
    }

    protected <P extends PacketBase> SimpleFlow<RegistryFriendlyByteBuf, Object> registerActual(SimpleFlow<RegistryFriendlyByteBuf, Object> channelBuilder, Class<P> clazz, CustomPacketPayload.Type<P> type, StreamCodec<RegistryFriendlyByteBuf, P> codec) {
        return channelBuilder.add(
                clazz,
                codec,
                (packet, ctx) -> {
                    if (ctx.isClientSide()) {
                        if (packet.isAsync()) {
                            handlePacketClient(ctx, packet);
                        } else {
                            ctx.enqueueWork(() -> handlePacketClient(ctx, packet));
                        }
                        ctx.setPacketHandled(true);
                    } else {
                        if (packet.isAsync()) {
                            handlePacketServer(ctx, packet);
                        } else {
                            ctx.enqueueWork(() -> handlePacketServer(ctx, packet));
                        }
                        ctx.setPacketHandled(true);
                    }
                });
    }

    @OnlyIn(Dist.CLIENT)
    public void handlePacketClient(CustomPayloadEvent.Context context, PacketBase<?> packet) {
        packet.actionClient(Minecraft.getInstance().player != null ? Minecraft.getInstance().player.level() : null, Minecraft.getInstance().player);
    }

    public void handlePacketServer(CustomPayloadEvent.Context context, PacketBase<?> packet) {
        packet.actionServer(context.getSender().level(), context.getSender());
    }

    @Override
    public <P extends PacketBase> void register(Class<P> clazz, CustomPacketPayload.Type<P> type, StreamCodec<? super RegistryFriendlyByteBuf, P> codec) {
        this.pendingPacketRegistrations.add(Triple.of(clazz, type, codec));
    }

    @Override
    public void sendToServer(PacketBase packet) {
        networkChannel.send(packet, PacketDistributor.SERVER.noArg());
    }

    @Override
    public void sendToPlayer(PacketBase packet, ServerPlayer player) {
        networkChannel.send(packet, PacketDistributor.PLAYER.with(player));
    }

    @Override
    public void sendToAllAroundPoint(PacketBase packet, IPacketHandler.TargetPoint point) {
        networkChannel.send(packet, PacketDistributor.NEAR.with(new PacketDistributor.TargetPoint(point.excluded(), point.x(), point.y(), point.z(), point.radius(), point.level().dimension())));
    }

    @Override
    public void sendToDimension(PacketBase packet, ServerLevel dimension) {
        networkChannel.send(packet, PacketDistributor.DIMENSION.with(dimension.dimension()));
    }

    @Override
    public void sendToAll(PacketBase packet) {
        networkChannel.send(packet, PacketDistributor.ALL.noArg());
    }

    @Deprecated // TODO: rm in next major
    public static class PacketCodecException extends RuntimeException {
        public PacketCodecException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    @Deprecated // TODO: rm in next major
    public static record TargetPoint(ServerLevel level, double x, double y, double z, double radius, @Nullable ServerPlayer excluded) {}

}
