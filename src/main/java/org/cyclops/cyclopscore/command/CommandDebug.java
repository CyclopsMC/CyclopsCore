package org.cyclops.cyclopscore.command;

import com.google.common.collect.Maps;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.command.argument.ArgumentTypeDebugPacket;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketAsync;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketComplexAsync;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketComplexSync;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketSync;

import java.util.Map;

/**
 * Command debugging.
 * @author rubensworks
 *
 */
public class CommandDebug implements Command<CommandSource> {

    private static final int AMOUNT = 3;
    public static final Map<String, PacketCodec> PACKETS = Maps.newHashMap();
    static {
        PACKETS.put("simple_async", new PingPongPacketAsync(AMOUNT));
        PACKETS.put("simple_sync", new PingPongPacketSync(AMOUNT));
        PACKETS.put("complex_async", new PingPongPacketComplexAsync(AMOUNT, "abc", "def"));
        PACKETS.put("complex_sync", new PingPongPacketComplexSync(AMOUNT, "abc", "def"));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        PacketCodec packet = context.getArgument("packet", PacketCodec.class);
        context.getSource().getPlayerOrException().sendMessage(new StringTextComponent(String.format("Sending %s from client to server...", packet.getClass())), Util.NIL_UUID);
        CyclopsCore._instance.getPacketHandler().sendToPlayer(packet, context.getSource().getPlayerOrException());
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSource> make() {
        return Commands.literal("debug")
                .requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.argument("packet", ArgumentTypeDebugPacket.INSTANCE)
                        .executes(new CommandDebug()));
    }

}
