package org.cyclops.cyclopscore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.network.chat.TextComponent;
import org.cyclops.cyclopscore.metadata.RegistryExportables;

import java.io.IOException;

/**
 * Command for dumping the registries to JSON files.
 * @author rubensworks
 *
 */
public class CommandDumpRegistries implements Command<CommandSourceStack> {


    @Override
    public int run(CommandContext<CommandSourceStack> context) {
        try {
            RegistryExportables.REGISTRY.export(context.getSource().getServer()
                    .getServerDirectory().toPath().resolve("cyclops_registries"));
        } catch (IOException e) {
            context.getSource().sendFailure(new TextComponent(e.getMessage()));
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSourceStack> make() {
        return Commands.literal("dumpregistries")
                .executes(new CommandDumpRegistries());
    }

}
