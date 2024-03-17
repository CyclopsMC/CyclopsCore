package org.cyclops.cyclopscore.network.packet;

import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet for reloading resources for a player.
 * @author rubensworks
 *
 */
public class ReloadResourcesPacket extends PacketCodec {

    public static final ResourceLocation ID = new ResourceLocation(Reference.MOD_ID, "reload_resources");

    /**
     * Empty packet.
     */
    public ReloadResourcesPacket() {
        super(ID);
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void actionClient(Level level, Player player) {
        long start = System.currentTimeMillis();
        Minecraft.getInstance().reloadResourcePacks();
        long end = System.currentTimeMillis();
        player.sendSystemMessage(Component.literal(String.format("Reloaded all resources in %s ms", end - start)));
    }

    @Override
    public void actionServer(Level level, ServerPlayer player) {

    }

}
