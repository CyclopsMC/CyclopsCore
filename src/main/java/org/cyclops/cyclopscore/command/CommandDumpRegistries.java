package org.cyclops.cyclopscore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.util.text.StringTextComponent;
import org.cyclops.cyclopscore.metadata.RegistryExportables;

import java.io.IOException;

/**
 * Command for dumping the registries to JSON files.
 * @author rubensworks
 *
 */
public class CommandDumpRegistries implements Command<CommandSource> {


    @Override
    public int run(CommandContext<CommandSource> context) {
        try {
            RegistryExportables.REGISTRY.export(context.getSource().getServer()
                    .getServerDirectory().toPath().resolve("cyclops_registries"));
        } catch (IOException e) {
            context.getSource().sendFailure(new StringTextComponent(e.getMessage()));
            e.printStackTrace();
            return 1;
        }
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSource> make() {
        return Commands.literal("dumpregistries")
                .executes(new CommandDumpRegistries());
    }

}
