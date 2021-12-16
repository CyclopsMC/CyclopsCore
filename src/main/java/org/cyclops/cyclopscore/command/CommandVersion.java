package org.cyclops.cyclopscore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.Util;
import net.minecraft.network.chat.TextComponent;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Command for checking the version.
 * @author rubensworks
 *
 */
public class CommandVersion implements Command<CommandSourceStack> {

    private final ModBase<?> mod;

    public CommandVersion(ModBase<?> mod) {
        this.mod = mod;
    }

    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        context.getSource().getPlayerOrException()
                .sendMessage(new TextComponent(this.mod.getContainer().getModInfo().getVersion().toString()), Util.NIL_UUID);
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSourceStack> make(ModBase mod) {
        return Commands.literal("version")
                .executes(new CommandVersion(mod));
    }

}
