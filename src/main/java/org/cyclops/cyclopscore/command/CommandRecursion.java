package org.cyclops.cyclopscore.command;

import com.google.common.collect.Maps;
import net.minecraft.command.ICommand;
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

    public CommandRecursion(ModBase mod) {
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
        Map<String, ICommand> map = Maps.newHashMap();
        map.put(NAME, this);
        return map;
    }
}
