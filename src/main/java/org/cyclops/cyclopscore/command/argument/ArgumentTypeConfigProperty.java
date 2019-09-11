package org.cyclops.cyclopscore.command.argument;

import com.google.common.collect.Lists;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.SimpleCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.command.ISuggestionProvider;
import net.minecraft.util.text.StringTextComponent;
import org.cyclops.cyclopscore.config.ConfigurablePropertyData;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Argument types for config properties.
 * @author rubensworks
 */
public class ArgumentTypeConfigProperty implements ArgumentType<ConfigurablePropertyData> {

    private final ModBase mod;

    public ArgumentTypeConfigProperty(ModBase mod) {
        this.mod = mod;
    }

    public ModBase getMod() {
        return mod;
    }

    @Override
    public ConfigurablePropertyData parse(StringReader reader) throws CommandSyntaxException {
        ConfigurablePropertyData property = mod.getConfigHandler().getCommandableProperties().get(reader.readString());
        if (property == null) {
            throw new SimpleCommandExceptionType(new StringTextComponent("Unknown property")).create();
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
        return ISuggestionProvider.suggest(mod.getConfigHandler().getCommandableProperties().keySet(), builder);
    }
}
