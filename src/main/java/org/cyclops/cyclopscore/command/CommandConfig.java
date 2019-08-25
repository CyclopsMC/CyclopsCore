package org.cyclops.cyclopscore.command;

import com.google.common.collect.Lists;
import com.mojang.brigadier.Command;
import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
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
import org.cyclops.cyclopscore.config.ConfigurablePropertyData;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.CompletableFuture;

/**
 * Command for selecting and defining a {@link ConfigurablePropertyData}.
 * @author rubensworks
 *
 */
public class CommandConfig implements Command<CommandSource> {

    private final ModBase mod;

    public CommandConfig(ModBase mod) {
        this.mod = mod;
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ConfigurablePropertyData property = context.getArgument("property", ConfigurablePropertyData.class);
        String value = context.getArgument("value", String.class);
        if (value == null) {
            context.getSource().asPlayer().sendMessage(new StringTextComponent(property.getConfigProperty().get().toString()));
        } else {
            Object newValue = Helpers.tryParse(value, property.getConfigProperty().get());
            if(newValue != null) {
                property.getConfigProperty().set(newValue);
                property.getConfigProperty().save();
                context.getSource().asPlayer().sendMessage(new StringTextComponent(L10NHelpers.localize("chat.cyclopscore.command.updatedValue",
                        property.getName(), newValue.toString())));
            } else {
                context.getSource().asPlayer().sendMessage(new StringTextComponent(L10NHelpers.localize("chat.cyclopscore.command.invalidNewValue")));
                return 1;
            }
        }
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSource> make(ModBase mod) {
        return Commands.literal("config")
                .requires((commandSource) -> commandSource.hasPermissionLevel(2))
                .then(Commands.argument("property", new ArgumentTypeProperty(mod)))
                .then(Commands.argument("value", StringArgumentType.string()))
                .executes(new CommandConfig(mod));
    }

    public static class ArgumentTypeProperty implements ArgumentType<ConfigurablePropertyData> {

        private final ModBase mod;

        public ArgumentTypeProperty(ModBase mod) {
            this.mod = mod;
        }

        @Override
        public ConfigurablePropertyData parse(StringReader reader) throws CommandSyntaxException {
            ConfigurablePropertyData property = mod.getConfigHandler().getCommandableProperties().get(reader.getString());
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
}
