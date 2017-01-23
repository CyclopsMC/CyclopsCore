package org.cyclops.cyclopscore.command;

import com.google.common.collect.Maps;
import net.minecraft.command.CommandException;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.PacketCodec;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketAsync;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketComplexAsync;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketComplexSync;
import org.cyclops.cyclopscore.network.packet.debug.PingPongPacketSync;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Command debugging.
 * @author rubensworks
 *
 */
public class CommandDebug extends CommandMod {

    public static final String NAME = "debug";
    private static final int AMOUNT = 3;

    public CommandDebug(ModBase mod) {
        super(mod, NAME);
    }

    public CommandDebug(ModBase mod, String name) {
        super(mod, NAME);
    }

    @Override
    public String getFullCommand() {
        return super.getFullCommand() + " " + NAME;
    }

    @Override
    public List<String> getAliases() {
        List<String> list = new LinkedList<String>();
        list.add(NAME);
        return list;
    }
    
    @Override
    protected Map<String, ICommand> getSubcommands() {
        Map<String, ICommand> map = Maps.newHashMap();
        map.put("simple_async", new CommandDebugPacket(getMod(), new PingPongPacketAsync(AMOUNT)));
        map.put("simple_sync", new CommandDebugPacket(getMod(), new PingPongPacketSync(AMOUNT)));
        map.put("complex_async", new CommandDebugPacket(getMod(), new PingPongPacketComplexAsync(AMOUNT, "abc", "def")));
        map.put("complex_sync", new CommandDebugPacket(getMod(), new PingPongPacketComplexSync(AMOUNT, "abc", "def")));
        return map;
    }

    @Override
    public void processCommandHelp(ICommandSender icommandsender, String[] astring) throws CommandException {
        Iterator<String> it = getSubcommands().keySet().iterator();
        if (it.hasNext())
            icommandsender.sendMessage(new TextComponentString(joinStrings(it, ", ")));
        else
            throw new CommandException("empty");
    }

    public class CommandDebugPacket extends CommandMod {

        private final PacketCodec packet;

        public CommandDebugPacket(ModBase mod, PacketCodec packet) {
            super(mod);
            this.packet = packet;
        }

        @Override
        public List<String> getTabCompletions(MinecraftServer server, ICommandSender sender, String[] parts, BlockPos blockPos) {
            return null;
        }

        @Override
        public void execute(MinecraftServer server, ICommandSender sender, String[] parts) {
            sender.sendMessage(new TextComponentString(String.format("Sending %s from client to server...", packet.getClass())));
            CyclopsCore._instance.getPacketHandler().sendToPlayer(packet, (EntityPlayerMP) sender.getCommandSenderEntity());
        }
    }

}
