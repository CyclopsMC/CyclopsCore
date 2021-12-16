package org.cyclops.cyclopscore.command.argument;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.TextComponent;
import org.cyclops.cyclopscore.command.CommandDebug;
import org.cyclops.cyclopscore.network.PacketCodec;

import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Argument type for debug packets.
 * @author rubensworks
 */
public class ArgumentTypeDebugPacket implements ArgumentType<PacketCodec> {

    public static final ArgumentTypeDebugPacket INSTANCE = new ArgumentTypeDebugPacket();

    private ArgumentTypeDebugPacket() {

    }

    @Override
    public PacketCodec parse(StringReader reader) throws CommandSyntaxException {
        PacketCodec packet = CommandDebug.PACKETS.get(reader.readString());
        if (packet == null) {
            throw new SimpleCommandExceptionType(new TextComponent("Invalid packet type")).create();
        }
        return packet;
    }

    @Override
    public Collection<String> getExamples() {
        return CommandDebug.PACKETS.keySet();
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(CommandDebug.PACKETS.keySet(), builder);
    }
}
