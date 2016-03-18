package org.cyclops.cyclopscore.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TextComponentString;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.List;

/**
 * Command for checking the version.
 * @author rubensworks
 *
 */
public class CommandVersion extends CommandMod {

    public static final String NAME = "version";

    public CommandVersion(ModBase mod) {
        super(mod, NAME);
    }

    @Override
    public List<String> getTabCompletionOptions(MinecraftServer server, ICommandSender sender, String[] parts, BlockPos blockPos) {
        return null;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] parts) {
        sender.addChatMessage(new TextComponentString(getMod().getReferenceValue(ModBase.REFKEY_MOD_VERSION)));
    }
}
