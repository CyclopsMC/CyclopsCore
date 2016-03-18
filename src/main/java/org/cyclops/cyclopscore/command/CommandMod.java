package org.cyclops.cyclopscore.command;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.Iterator;
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
    private final List<String> aliases = Lists.newLinkedList();

    public CommandMod(ModBase mod, Map<String, ICommand> subCommands) {
        this.mod = mod;
        this.subCommands = subCommands;
        this.subCommands.put(CommandConfig.NAME, new CommandConfig(mod));
        this.subCommands.put(CommandVersion.NAME, new CommandVersion(mod));
        addAlias(mod.getModId());
    }

    public CommandMod(ModBase mod) {
        this.mod = mod;
        this.subCommands = Maps.newHashMap();
    }

    public CommandMod(ModBase mod, String name) {
        this(mod);
        addAlias(name);
    }

    public void addAlias(String alias) {
        this.aliases.add(alias);
    }

    protected ModBase getMod() {
        return this.mod;
    }
    
    protected List<String> getAliases() {
        return aliases;
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
    public int compareTo(ICommand o) {
        return 0;
    }

    @Override
    public String getCommandName() {
        return mod.getModId();
    }

    /**
     * @return Recursively returns the whole command string up to the current subcommand.
     */
    public String getFullCommand() {
        return getCommandName();
    }

    @Override
    public String getCommandUsage(ICommandSender icommandsender) {
        String command = "/" + getFullCommand();
        Iterator<String> it = getSubcommands().keySet().iterator();
        return it.hasNext() ?
                joinStrings(command + " <", it, " | ", ">") :
                command;
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List<String> getCommandAliases() {
        return this.getAliases();
    }
    
    protected String[] shortenArgumentList(String[] astring) {
        String[] asubstring = new String[astring.length - 1];
        System.arraycopy(astring, 1, asubstring, 0, astring.length - 1);
        return asubstring;
    }

    /**
     * This method is called when the user uses the command in an incorrect way. This command
     * should either print out a friendly message explaining how to use the given (sub)command,
     * or throw a CommandException with extra helpful information.
     *
     * @param icommandsender Use this commandsender to print chat message.
     * @param astring The list of strings that were entered as subcommands to the current command by the user.
     * @throws CommandException Thrown when the user entered something wrong, should contain some helpful information as
     *                          to what wrong.
     */
    public void processCommandHelp(ICommandSender icommandsender, String[] astring) throws CommandException {
        throw new WrongUsageException(getCommandUsage(icommandsender));
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender icommandsender, String[] astring) throws CommandException {
        if(astring.length == 0) {
            processCommandHelp(icommandsender, astring);
        } else {
            ICommand subcommand = getSubcommands().get(astring[0]);
            if(subcommand != null) {
                String[] asubstring = shortenArgumentList(astring);
                subcommand.execute(server, icommandsender, asubstring);
            } else {
                throw new WrongUsageException(L10NHelpers.localize("chat.cyclopscore.command.invalidSubcommand"));
            }
        }
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender icommandsender) {
        return icommandsender.canCommandSenderUseCommand(FMLCommonHandler.instance().getMinecraftServerInstance().getOpPermissionLevel(), getCommandName());
    }

    @SuppressWarnings("rawtypes")
    @Override
    public List getTabCompletionOptions(MinecraftServer server, ICommandSender icommandsender,
            String[] astring, BlockPos blockPos) {
        if(astring.length != 0) {
            ICommand subcommand = getSubcommands().get(astring[0]);
            if(subcommand != null) {
                String[] asubstring = shortenArgumentList(astring);
                return subcommand.getTabCompletionOptions(server, icommandsender, asubstring, blockPos);
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

    //== Helper functions ==//

    protected String joinStrings(Iterator<String> it, String delim) {
        return joinStrings("", it, delim, "");
    }

    protected String joinStrings(String prefix, Iterator<String> it, String delim, String suffix) {
        StringBuilder builder = new StringBuilder(prefix);

        if (it.hasNext()) {
            builder.append(it.next());
            while (it.hasNext()) {
                builder.append(delim);
                builder.append(it.next());
            }
        }

        builder.append(suffix);

        return builder.toString();
    }

    protected void printLineToChat(ICommandSender sender, String line) {
        sender.addChatMessage(new TextComponentString(line));
    }
}
