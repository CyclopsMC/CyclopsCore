package org.cyclops.cyclopscore.command.argument;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.cyclops.cyclopscore.config.ConfigurablePropertyData;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Argument types for config properties.
 * @author rubensworks
 */
public class ArgumentTypeConfigProperty implements ArgumentType<ConfigurablePropertyData> {

    private final IModBase mod;

    public ArgumentTypeConfigProperty(IModBase mod) {
        this.mod = mod;
    }

    public IModBase getMod() {
        return mod;
    }

    @Override
    public ConfigurablePropertyData parse(StringReader reader) throws CommandSyntaxException {
        ConfigurablePropertyData property = mod.getConfigHandler().getCommandableProperties().get(reader.readString());
        if (property == null) {
            throw new SimpleCommandExceptionType(Component.literal("Unknown property")).create();
        }
        return property;
    }

    @Override
    public Collection<String> getExamples() {
        ArrayList<String> all = Lists.newArrayList(mod.getConfigHandler().getCommandableProperties().keySet());
        return all.subList(0, Math.min(all.size(), 3));
    }

    @Override
    public <S> CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(mod.getConfigHandler().getCommandableProperties().keySet(), builder);
    }
}
