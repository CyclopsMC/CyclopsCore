package org.cyclops.cyclopscore.command.argument;

import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.config.extendedconfig.ArgumentTypeConfig;

public class ArgumentTypeDebugPacketConfig extends ArgumentTypeConfig<ArgumentTypeDebugPacket, SingletonArgumentInfo<ArgumentTypeDebugPacket>.Template> {
    public ArgumentTypeDebugPacketConfig() {
        super(CyclopsCore._instance, "debug_packet", SingletonArgumentInfo.contextFree(() -> ArgumentTypeDebugPacket.INSTANCE), ArgumentTypeDebugPacket.class);
    }
}
