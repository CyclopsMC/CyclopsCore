package org.cyclops.cyclopscore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.network.packet.ReloadResourcesPacket;

/**
 * Command for reloading the current resourcepack
 * @author rubensworks
 *
 */
public class CommandReloadResources implements Command<CommandSource> {

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        CyclopsCore._instance.getPacketHandler().sendToPlayer(new ReloadResourcesPacket(), context.getSource().getPlayerOrException());
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSource> make() {
        return Commands.literal("reloadresources")
                .executes(new CommandReloadResources());
    }
}
