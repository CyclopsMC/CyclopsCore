package org.cyclops.cyclopscore.network;

import com.google.common.collect.Lists;
import io.netty.channel.ChannelHandler.Sharable;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlerEvent;
import net.neoforged.neoforge.network.handling.PlayPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.List;
import java.util.function.Supplier;

/**
 * Advanced packet handler of {@link PacketBase} instances.
 * @author rubensworks
 */
@Sharable
public final class PacketHandler {

    private final ModBase mod;
    private final List<Pair<ResourceLocation, Supplier<? extends PacketBase>>> pendingPacketRegistrations;

    public PacketHandler(ModBase mod) {
        this.mod = mod;
        this.pendingPacketRegistrations = Lists.newArrayList();
        mod.getModEventBus().addListener(this::init);
    }

    protected void init(RegisterPayloadHandlerEvent event) {
        IPayloadRegistrar registrar = event.registrar(mod.getModId())
                .versioned("1.0.0")
                .optional();

        for (Pair<ResourceLocation, Supplier<? extends PacketBase>> pendingPacketRegistration : this.pendingPacketRegistrations) {
            this.registerActual(registrar, pendingPacketRegistration.getLeft(), pendingPacketRegistration.getRight());
        }
    }

    public <P extends PacketBase> void register(ResourceLocation id, Supplier<P> packetSupplier) {
        this.pendingPacketRegistrations.add(Pair.of(id, packetSupplier));
    }

    protected <P extends PacketBase> void registerActual(IPayloadRegistrar registrar, ResourceLocation id, Supplier<P> packetSupplier) {
        registrar.play(
                id,
                buffer -> {
                    P packet = packetSupplier.get();
                    try {
                        packet.decode(buffer);
                    } catch (Throwable e) {
                        throw new PacketCodecException("An exception occurred during decoding of packet " + packet.toString(), e);
                    }
                    return packet;
                },
                handler -> {
                    handler.client((packet, ctx) -> {
                        if (packet.isAsync()) {
                            handlePacketClient(ctx, packet);
                        } else {
                            ctx.workHandler().submitAsync(() -> handlePacketClient(ctx, packet));
                        }
                    });
                    handler.server((packet, ctx) -> {
                        if (packet.isAsync()) {
                            handlePacketServer(ctx, packet);
                        } else {
                            ctx.workHandler().submitAsync(() -> handlePacketServer(ctx, packet));
                        }
                    });
                });
    }

    @OnlyIn(Dist.CLIENT)
    public void handlePacketClient(PlayPayloadContext context, PacketBase packet) {
        packet.actionClient(context.level().get(), Minecraft.getInstance().player);
    }

    public void handlePacketServer(PlayPayloadContext context, PacketBase packet) {
        packet.actionServer(context.level().get(), (ServerPlayer) context.player().get());
    }

    /**
     * Send a packet to the server.
     * @param packet The packet.
     */
    public void sendToServer(PacketBase packet) {
        PacketDistributor.SERVER.noArg().send(packet);
    }

    /**
     * Send a packet to the player.
     * @param packet The packet.
     * @param player The player.
     */
    public void sendToPlayer(PacketBase packet, ServerPlayer player) {
        PacketDistributor.PLAYER.with(player).send(packet);
    }

    /**
     * Send a packet to all in the target range.
     * @param packet The packet.
     * @param point The area to send to.
     */
    public void sendToAllAround(PacketBase packet, PacketDistributor.TargetPoint point) {
        PacketDistributor.NEAR.with(point).send(packet);
    }

    /**
     * Send a packet to everything in the given dimension.
     * @param packet The packet.
     * @param dimension The dimension to send to.
     */
    public void sendToDimension(PacketBase packet, ResourceKey<Level> dimension) {
        PacketDistributor.DIMENSION.with(dimension).send(packet);
    }

    /**
     * Send a packet to everything.
     * @param packet The packet.
     */
    public void sendToAll(PacketBase packet) {
        PacketDistributor.ALL.noArg().send(packet);
    }

    public static class PacketCodecException extends RuntimeException {
        public PacketCodecException(String message, Throwable cause) {
            super(message, cause);
        }
    }

}
