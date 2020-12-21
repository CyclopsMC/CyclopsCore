package org.cyclops.cyclopscore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.Util;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import org.cyclops.cyclopscore.command.argument.ArgumentTypeConfigProperty;
import org.cyclops.cyclopscore.config.ConfigurablePropertyData;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Command for selecting and defining a {@link ConfigurablePropertyData}.
 * @author rubensworks
 *
 */
public class CommandConfig implements Command<CommandSource> {

    private final ModBase mod;
    private final boolean valueSet;

    public CommandConfig(ModBase mod, boolean valueSet) {
        this.mod = mod;
        this.valueSet = valueSet;
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        ConfigurablePropertyData property = context.getArgument("property", ConfigurablePropertyData.class);
        if (!this.valueSet) {
            context.getSource().asPlayer().sendMessage(new StringTextComponent(property.getConfigProperty().get().toString()), Util.DUMMY_UUID);
        } else {
            String value = context.getArgument("value", String.class);
            Object newValue = Helpers.tryParse(value, property.getConfigProperty().get());
            if(newValue != null) {
                property.getConfigProperty().set(newValue);
                property.getConfigProperty().save();
                context.getSource().asPlayer().sendMessage(new TranslationTextComponent("chat.cyclopscore.command.updatedValue",
                        property.getName(), newValue.toString()), Util.DUMMY_UUID);
            } else {
                context.getSource().asPlayer().sendMessage(new TranslationTextComponent("chat.cyclopscore.command.invalidNewValue"), Util.DUMMY_UUID);
                return 1;
            }
        }
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSource> make(ModBase mod) {
        return Commands.literal("config")
                .requires((commandSource) -> commandSource.hasPermissionLevel(2))
                .then(Commands.argument("property", new ArgumentTypeConfigProperty(mod))
                        .executes(new CommandConfig(mod, false))
                        .then(Commands.argument("value", StringArgumentType.string())
                                .executes(new CommandConfig(mod, true))));
    }

}
