package org.cyclops.cyclopscore.command;

import com.google.common.collect.Maps;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.Map;

/**
 * Command for doing some recursion.
 * @author rubensworks
 *
 */
public class CommandRecursion extends CommandMod {
    
    public static final String NAME = "recursion";

    private int recursionDepth = 0;

    public CommandRecursion(ModBase mod) {
        super(mod, NAME);
    }

    @Override
    public String getFullCommand() {
        StringBuilder builder = new StringBuilder();
        builder.append(super.getFullCommand());
        for (int i = 0; i <= recursionDepth; i++) {
            builder.append(" ");
            builder.append(NAME);
        }

        return builder.toString();
    }

    @Override
    protected Map<String, ICommand> getSubcommands() {
        Map<String, ICommand> map = Maps.newHashMap();
        map.put(NAME, this);
        return map;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) throws CommandException {
        recursionDepth = 0;

        // Count the recursion depth
        while (recursionDepth < astring.length && NAME.equals(astring[recursionDepth]))
            recursionDepth++;

        if(recursionDepth == 42 - 1) {
            icommandsender.addChatMessage(new ChatComponentText("You just lost The Game"));
        }
        processCommandHelp(icommandsender, astring);
    }
}
