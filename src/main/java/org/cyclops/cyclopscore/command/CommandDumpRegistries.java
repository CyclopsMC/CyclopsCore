package org.cyclops.cyclopscore.command;

import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.math.BlockPos;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.metadata.RegistryExportables;

import java.io.IOException;
import java.util.List;

/**
 * Command for dumping the registries to JSON files.
 * @author rubensworks
 *
 */
public class CommandDumpRegistries extends CommandMod {

    public static final String NAME = "dumpregistries";

    public CommandDumpRegistries(ModBase mod) {
        super(mod, NAME);
    }

    @Override
    public List<String> getTabCompletions(MinecraftServer server, ICommandSender icommandsender, String[] astring, BlockPos blockPos) {
        return null;
    }

    @Override
    public boolean checkPermission(MinecraftServer server, ICommandSender sender) {
        return sender instanceof EntityPlayerMP;
    }

    @Override
    public void execute(MinecraftServer server, ICommandSender sender, String[] parts) {
        try {
            RegistryExportables.REGISTRY.export(server.getDataDirectory().toPath().resolve("cyclops_registries"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
