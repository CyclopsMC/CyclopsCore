package org.cyclops.cyclopscore.command;

import net.minecraft.command.ICommand;
import org.cyclops.cyclopscore.config.ConfigProperty;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
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
}
