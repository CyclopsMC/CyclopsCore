package org.cyclops.cyclopscore.command;

import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.util.ChatComponentText;
import org.cyclops.cyclopscore.config.ConfigProperty;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.*;
import java.util.Map.Entry;

/**
 * Command for selecting {@link ConfigProperty}.
 * @author rubensworks
 *
 */
public class CommandConfig extends CommandMod {
    
    public static final String NAME = "config";

    public CommandConfig(ModBase mod) {
        super(mod);
    }

    @Override
    public String getFullCommand() {
        return super.getFullCommand() + " " + NAME;
    }

    @Override
    protected List<String> getAliases() {
        List<String> list = new LinkedList<String>();
        list.add(NAME);
        return list;
    }
    
    @Override
    protected Map<String, ICommand> getSubcommands() {
        Map<String, ICommand> map = new HashMap<String, ICommand>();
        for(Entry<String, ConfigProperty> entry : getMod().getConfigHandler().getCommandableProperties().entrySet()) {
            String name = entry.getValue().getName();
            map.put(name, new CommandConfigSet(getMod(), name, entry.getValue()));
        }
        return map;
    }

    @Override
    public void processCommandHelp(ICommandSender icommandsender, String[] astring) throws CommandException {
        Iterator<String> it = getSubcommands().keySet().iterator();
        if (it.hasNext())
            icommandsender.addChatMessage(new ChatComponentText(joinStrings(it, ", ")));
        else
            throw new CommandException(L10NHelpers.localize("chat.cyclopscore.command.noConfigsFound"));
    }
}
