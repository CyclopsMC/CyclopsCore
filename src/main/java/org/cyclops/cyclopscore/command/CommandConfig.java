package org.cyclops.cyclopscore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;
import org.cyclops.cyclopscore.command.argument.ArgumentTypeConfigProperty;
import org.cyclops.cyclopscore.config.ConfigurablePropertyData;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Command for selecting and defining a {@link ConfigurablePropertyData}.
 * @author rubensworks
 *
 */
public class CommandConfig implements Command<CommandSourceStack> {

    private final ModBase mod;
    private final boolean valueSet;

    public CommandConfig(ModBase mod, boolean valueSet) {
        this.mod = mod;
        this.valueSet = valueSet;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ConfigurablePropertyData property = context.getArgument("property", ConfigurablePropertyData.class);
        if (!this.valueSet) {
            context.getSource().getPlayerOrException().sendMessage(new TextComponent(property.getConfigProperty().get().toString()), Util.NIL_UUID);
        } else {
            String value = context.getArgument("value", String.class);
            Object newValue = Helpers.tryParse(value, property.getConfigProperty().get());
            if(newValue != null) {
                property.getConfigProperty().set(newValue);
                property.getConfigProperty().save();
                context.getSource().getPlayerOrException().sendMessage(new TranslatableComponent("chat.cyclopscore.command.updatedValue",
                        property.getName(), newValue.toString()), Util.NIL_UUID);
            } else {
                context.getSource().getPlayerOrException().sendMessage(new TranslatableComponent("chat.cyclopscore.command.invalidNewValue"), Util.NIL_UUID);
                return 1;
            }
        }
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSourceStack> make(ModBase mod) {
        return Commands.literal("config")
                .requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.argument("property", new ArgumentTypeConfigProperty(mod))
                        .executes(new CommandConfig(mod, false))
                        .then(Commands.argument("value", StringArgumentType.string())
                                .executes(new CommandConfig(mod, true))));
    }

}
