package org.cyclops.cyclopscore.command;

import com.mojang.brigadier.Command;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.command.CommandSource;
import net.minecraft.command.Commands;
import net.minecraft.command.arguments.EntityArgument;
import net.minecraft.entity.Entity;
import net.minecraft.util.Util;
import net.minecraft.util.text.TranslationTextComponent;

import java.util.Collection;

/**
 * Command for igniting players by name.
 * @author rubensworks
 */
public class CommandIgnite implements Command<CommandSource> {

    private final boolean durationParam;

    public CommandIgnite(boolean durationParam) {
        this.durationParam = durationParam;
    }

    @Override
    public int run(CommandContext<CommandSource> context) throws CommandSyntaxException {
        Collection<? extends Entity> entities = EntityArgument.getEntities(context, "targets");
        int duration = this.durationParam ? IntegerArgumentType.getInteger(context, "duration") : 2;
        for (Entity entity : entities) {
            entity.setFire(duration);
            context.getSource().asPlayer().sendMessage(new TranslationTextComponent(
                    "chat.cyclopscore.command.ignitedPlayer", entity.getDisplayName(), duration), Util.DUMMY_UUID);
        }
        return 0;
    }

    public static LiteralArgumentBuilder<CommandSource> make() {
        return Commands.literal("ignite")
                .requires((commandSource) -> commandSource.hasPermissionLevel(2))
                .then(Commands.argument("targets", EntityArgument.entities())
                        .executes(new CommandIgnite(false))
                        .then(Commands.argument("duration", IntegerArgumentType.integer())
                                .executes(new CommandIgnite(true))));
    }

}
