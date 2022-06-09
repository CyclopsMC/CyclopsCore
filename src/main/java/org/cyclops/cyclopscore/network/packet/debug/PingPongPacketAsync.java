package org.cyclops.cyclopscore.network.packet.debug;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

import java.lang.reflect.Field;
import java.util.List;

/**
 * Debug ping pong packet
 * @author rubensworks
 *
 */
public class PingPongPacketAsync extends PacketCodec {

    @CodecField
    protected int remaining;

    /**
     * Empty packet.
     */
    public PingPongPacketAsync() {

    }

    public PingPongPacketAsync(int remaining) {
        this.remaining = remaining;
    }

    @Override
    public boolean isAsync() {
        return true;
    }

    protected void log(Player player, String message) {
        player.sendSystemMessage(Component.literal(message));
    }

    protected PingPongPacketAsync newPacket() {
        return new PingPongPacketSync(remaining - 1);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void actionClient(Level level, Player player) {
        if(remaining > 0) {
            CyclopsCore._instance.getPacketHandler().sendToServer(newPacket());
        }
        log(player, String.format("[PING %s] Fields: %s", remaining, toString()));
    }

    @Override
    public void actionServer(Level level, ServerPlayer player) {
        if(remaining > 0) {
            CyclopsCore._instance.getPacketHandler().sendToPlayer(newPacket(), player);
        }
        log(player, String.format("[PONG %s] Fields: %s", remaining, toString()));
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<Field> fields = fieldCache.get(null);
        for(Field field : fields) {
            sb.append(" ");
            sb.append(field);
            sb.append("=");
            try {
                sb.append(field.get(this));
            } catch (IllegalAccessException e) {
                e.printStackTrace();
                sb.append("ERROR(" + e.getMessage() + ")");
            }
        }
        return sb.toString();
    }

}
