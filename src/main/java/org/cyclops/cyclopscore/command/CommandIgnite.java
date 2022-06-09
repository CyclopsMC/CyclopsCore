package org.cyclops.cyclopscore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.Util;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.ChatType;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;

import java.util.Collection;

/**
 * Command for igniting players by name.
 * @author rubensworks
 */
public class CommandIgnite implements Command<CommandSourceStack> {

    private final boolean durationParam;

    public CommandIgnite(boolean durationParam) {
        this.durationParam = durationParam;
    }

    @Override
    public int run(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgument.getEntities(context, "targets");
        int duration = this.durationParam ? IntegerArgumentType.getInteger(context, "duration") : 2;
        for (Entity entity : entities) {
            entity.setSecondsOnFire(duration);
            context.getSource().getPlayerOrException().sendSystemMessage(Component.translatable(
                    "chat.cyclopscore.command.ignitedPlayer", entity.getDisplayName(), duration), ChatType.SYSTEM);
        }
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSourceStack> make() {
        return Commands.literal("ignite")
                .requires((commandSource) -> commandSource.hasPermission(2))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(new CommandIgnite(false))
                        .then(Commands.argument("duration", IntegerArgumentType.integer())
                                .executes(new CommandIgnite(true))));
    }

}
