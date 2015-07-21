package org.cyclops.cyclopscore.command;

import com.google.common.collect.Maps;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.LinkedList;
import java.util.List;
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
        super(mod);
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
    protected List<String> getAliases() {
        List<String> list = new LinkedList<String>();
        list.add(NAME);
        return list;
    }

    @Override
    protected Map<String, ICommand> getSubcommands() {
        Map<String, ICommand> map = Maps.newHashMap();
        map.put(NAME, this);
        return map;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) throws CommandException {
        StringBuilder builder = new StringBuilder(NAME);
        recursionDepth = 0;

        // Count the recursion depth
        while (recursionDepth < astring.length && NAME.equals(astring[recursionDepth]))
            recursionDepth++;

        CyclopsCore.clog("Depth: " + recursionDepth);

        processCommandHelp(icommandsender, astring);
    }
}
