package org.cyclops.cyclopscore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import org.cyclops.cyclopscore.helper.CyclopsCoreInstance;
import org.cyclops.cyclopscore.network.packet.ReloadResourcesPacket;

/**
 * Command for reloading the current resourcepack
 * @author rubensworks
 *
 */
public class CommandReloadResources implements Command<CommandSourceStack> {

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        CyclopsCoreInstance.MOD.getPacketHandlerCommon().sendToPlayer(new ReloadResourcesPacket(), context.getSource().getPlayerOrException());
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSourceStack> make() {
        return Commands.literal("reloadresources")
                .executes(new CommandReloadResources());
    }
}
