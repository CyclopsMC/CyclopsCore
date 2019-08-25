package org.cyclops.cyclopscore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.text.StringTextComponent;
import org.cyclops.cyclopscore.helper.L10NHelpers;

import java.util.Collection;

/**
 * Command for igniting players by name.
 * @author rubensworks
 *
 */
public class CommandIgnite implements Command<CommandSource> {

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Collection<ServerPlayerEntity> players = EntityArgument.getPlayers(context, "targets");
        int duration = IntegerArgumentType.getInteger(context, "duration");
        if (duration == 0) {
            duration = 2;
        }
        for (ServerPlayerEntity player : players) {
            player.setFire(duration);
            context.getSource().asPlayer()
                    .sendMessage(new StringTextComponent(L10NHelpers.localize("chat.cyclopscore.command.ignitedPlayer", player.getDisplayName(), duration)));
        }
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSource> make() {
        return Commands.literal("ignite")
                .requires((commandSource) -> commandSource.hasPermissionLevel(2))
                .then(Commands.argument("targets", EntityArgument.players()))
                .then(Commands.argument("duration", IntegerArgumentType.integer()))
                .executes(new CommandIgnite());
    }

}
