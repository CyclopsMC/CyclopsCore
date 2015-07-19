package org.cyclops.cyclopscore.command;

import com.google.common.collect.Maps;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * The mod base command.
 * @author rubensworks
 *
 */
public class CommandMod implements ICommand {

    private final ModBase mod;
    private final Map<String, ICommand> subCommands;

    public CommandMod(ModBase mod, Map<String, ICommand> subCommands) {
        this.mod = mod;
        this.subCommands = subCommands;
        this.subCommands.put(CommandConfig.NAME, new CommandConfig(mod));
        this.subCommands.put(CommandVersion.NAME, new CommandVersion(mod));
    }

    public CommandMod(ModBase mod) {
        this.mod = mod;
        this.subCommands = Maps.newHashMap();
    }

    protected ModBase getMod() {
        return this.mod;
    }
    
    protected List<String> getAliases() {
        List<String> list = new LinkedList<String>();
        list.add(mod.getModId());
        return list;
    }
    
    protected Map<String, ICommand> getSubcommands() {
        return subCommands;
    }
    
    private List<String> getSubCommands(String cmd) {
        List<String> completions = new LinkedList<String>();
        for(String full : getSubcommands().keySet()) {
            if(full.startsWith(cmd)) {
                completions.add(full);
            }
        }
        return completions;
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return mod.getModId();
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        String possibilities = "";
        for(String full : getSubcommands().keySet()) {
            possibilities += full + " ";
        }
        return mod.getModId() + " " + possibilities;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getCommandAliases() {
        return this.getAliases();
    }
    
    protected String[] shortenArgumentList(String[] astring) {
        String[] asubstring = new String[astring.length - 1];
        System.arraycopy(astring, 1, asubstring, 0, astring.length - 1);
        return asubstring;
    }

    @Override
    public void processCommand(ICommandSender icommandsender, String[] astring) throws CommandException {
        if(astring.length == 0) {
            icommandsender.addChatMessage(new ChatComponentText(L10NHelpers.localize("chat.cyclopscore.command.invalidArguments")));
        } else {
            ICommand subcommand = getSubcommands().get(astring[0]);
            if(subcommand != null) {
                String[] asubstring = shortenArgumentList(astring);
                subcommand.processCommand(icommandsender, asubstring);
            } else {
                icommandsender.addChatMessage(new ChatComponentText(L10NHelpers.localize("chat.cyclopscore.command.invalidSubcommand")));
            }
        }
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender icommandsender) {
        return icommandsender.canCommandSenderUseCommand(MinecraftServer.getServer().getOpPermissionLevel(), getCommandName());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List addTabCompletionOptions(ICommandSender icommandsender,
            String[] astring, BlockPos blockPos) {
        if(astring.length != 0) {
            ICommand subcommand = getSubcommands().get(astring[0]);
            if(subcommand != null) {
                String[] asubstring = shortenArgumentList(astring);
                return subcommand.addTabCompletionOptions(icommandsender, asubstring, blockPos);
            } else {
                return getSubCommands(astring[0]);
            }
        } else {
            return getSubCommands("");
        }
    }

    @Override
    public boolean isUsernameIndex(String[] astring, int i) {
        return false;
    }
    
}
