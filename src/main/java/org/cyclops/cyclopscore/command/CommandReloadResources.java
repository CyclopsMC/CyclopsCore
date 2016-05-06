package org.cyclops.cyclopscore.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.packet.ReloadResourcesPacket;

import java.util.List;

/**
 * Command for reloading the current resourcepack
 * @author rubensworks
 *
 */
public class CommandReloadResources extends CommandMod {

    public static final String NAME = "reloadresources";

    public CommandReloadResources(ModBase mod) {
        super(mod, NAME);
    }

    @Override
    public List getTabCompletionOptions(MinecraftServer server, ICommandSender icommandsender, String[] astring, BlockPos blockPos) {
        return null;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayerMP;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] parts) {
        CyclopsCore._instance.getPacketHandler().sendToPlayer(new ReloadResourcesPacket(), (EntityPlayerMP) sender);
    }
}
