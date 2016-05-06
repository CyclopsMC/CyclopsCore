package org.cyclops.cyclopscore.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.BlockPos;
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
    public List addTabCompletionOptions(ICommandSender sender, String[] parts, BlockPos blockPos) {
        return null;
    }

    @Override
    public boolean canCommandSenderUseCommand(ICommandSender sender) {
        return sender instanceof EntityPlayerMP;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] parts) {
        CyclopsCore._instance.getPacketHandler().sendToPlayer(new ReloadResourcesPacket(), (EntityPlayerMP) sender);
    }
}
