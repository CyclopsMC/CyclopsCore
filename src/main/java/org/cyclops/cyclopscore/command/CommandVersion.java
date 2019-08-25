package org.cyclops.cyclopscore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Command for checking the version.
 * @author rubensworks
 *
 */
public class CommandVersion implements Command<CommandSource> {

    private final ModBase mod;

    public CommandVersion(ModBase mod) {
        this.mod = mod;
    }

    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        context.getSource().asPlayer()
                .sendMessage(new StringTextComponent(this.mod.getReferenceValue(ModBase.REFKEY_MOD_VERSION)));
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSource> make(ModBase mod) {
        return Commands.literal("version")
                .executes(new CommandVersion(mod));
    }

}
