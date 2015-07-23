package org.cyclops.cyclopscore.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.util.BlockPos;
import net.minecraft.util.ChatComponentText;
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
    public List addTabCompletionOptions(ICommandSender sender, String[] parts, BlockPos blockPos) {
        return null;
    }

    @Override
    public void processCommand(ICommandSender sender, String[] parts) {
        sender.addChatMessage(new ChatComponentText(getMod().getReferenceValue(ModBase.REFKEY_MOD_VERSION)));
    }
}
