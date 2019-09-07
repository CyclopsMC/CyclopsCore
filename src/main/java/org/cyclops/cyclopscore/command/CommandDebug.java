package org.cyclops.cyclopscore.command;

import com.google.common.collect.Maps;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.text.StringTextComponent;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketAsync;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketComplexAsync;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketComplexSync;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketSync;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

/**
 * Command debugging.
 * @author rubensworks
 *
 */
public class CommandDebug implements Command<CommandSource> {

    private static final int AMOUNT = 3;
    private static final Map<String, PacketCodec> PACKETS = Maps.newHashMap();
    static {
        PACKETS.put("simple_async", new PingPongPacketAsync(AMOUNT));
        PACKETS.put("simple_sync", new PingPongPacketSync(AMOUNT));
        PACKETS.put("complex_async", new PingPongPacketComplexAsync(AMOUNT, "abc", "def"));
        PACKETS.put("complex_sync", new PingPongPacketComplexSync(AMOUNT, "abc", "def"));
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        PacketCodec packet = context.getArgument("packet", PacketCodec.class);
        context.getSource().asPlayer().sendMessage(new StringTextComponent(String.format("Sending %s from client to server...", packet.getClass())));
        CyclopsCore._instance.getPacketHandler().sendToPlayer(packet, context.getSource().asPlayer());
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSource> make() {
        return Commands.literal("debug")
                .requires((commandSource) -> commandSource.hasPermissionLevel(2))
                .then(Commands.argument("packet", ArgumentTypePacket.INSTANCE)
                        .executes(new CommandDebug()));
    }

    public static class ArgumentTypePacket implements ArgumentType<PacketCodec> {

        public static final ArgumentTypePacket INSTANCE = new ArgumentTypePacket();

        private ArgumentTypePacket() {

        }

        @Override
        public PacketCodec parse(StringReader reader) throws CommandSyntaxException {
            PacketCodec packet = PACKETS.get(reader.readString());
            if (packet == null) {
                throw new SimpleCommandExceptionType(new StringTextComponent("Invalid packet type")).create();
            }
            return packet;
        }

        @Override
        public Collection<String> getExamples() {
            return PACKETS.keySet();
        }

        @Override
        public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
            return ISuggestionProvider.suggest(PACKETS.keySet(), builder);
        }
    }

}
