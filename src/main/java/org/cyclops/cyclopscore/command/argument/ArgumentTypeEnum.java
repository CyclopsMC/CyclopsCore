package org.cyclops.cyclopscore.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.chat.TextComponent;
import org.cyclops.cyclopscore.network.PacketCodec;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * An argument type for a certain enum.
 * @author rubensworks
 */
public class ArgumentTypeEnum<T extends Enum<T>> implements ArgumentType<T> {

    private final Class<T> enumClass;

    public ArgumentTypeEnum(Class<T> enumClass) {
        this.enumClass = enumClass;
    }

    @Override
    public T parse(StringReader reader) throws CommandSyntaxException {
        try {
            return Enum.valueOf(this.enumClass, reader.readString().toUpperCase(Locale.ENGLISH));
        } catch (IllegalArgumentException e) {
            throw new SimpleCommandExceptionType(new TextComponent("Unknown value")).create();
        }
    }

    @Override
    public Collection<String> getExamples() {
        return Arrays.stream(this.enumClass.getEnumConstants())
                .map(Enum::name)
                .map(name -> name.toLowerCase(Locale.ENGLISH))
                .collect(Collectors.toList());
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(getExamples(), builder);
    }

    public static <T extends Enum<T>> T getValue(CommandContext<CommandSourceStack> context, String name, Class<T> enumClass) {
        return context.getArgument(name, enumClass);
    }

    public static class Serializer implements ArgumentSerializer<ArgumentTypeEnum<?>> {

        @Override
        public void serializeToNetwork(ArgumentTypeEnum argumentTypeEnum, FriendlyByteBuf packetBuffer) {
            packetBuffer.writeUtf(argumentTypeEnum.enumClass.getName());
        }

        @Override
        public ArgumentTypeEnum deserializeFromNetwork(FriendlyByteBuf packetBuffer) {
            try {
                return new ArgumentTypeEnum(Class.forName(packetBuffer.readUtf(PacketCodec.READ_STRING_MAX_LENGTH)));
            } catch (ClassNotFoundException e) {
                return null;
            }
        }

        @Override
        public void serializeToJson(ArgumentTypeEnum argumentTypeEnum, JsonObject jsonObject) {
            jsonObject.addProperty("class", argumentTypeEnum.enumClass.getName());
        }
    }

}
