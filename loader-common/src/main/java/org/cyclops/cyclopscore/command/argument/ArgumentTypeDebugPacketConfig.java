package org.cyclops.cyclopscore.command.argument;

import net.minecraft.commands.synchronization.SingletonArgumentInfo;
import org.cyclops.cyclopscore.config.extendedconfig.ArgumentTypeConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

public class ArgumentTypeDebugPacketConfig<M extends IModBase> extends ArgumentTypeConfigCommon<ArgumentTypeDebugPacket, SingletonArgumentInfo<ArgumentTypeDebugPacket>.Template, M> {
    public ArgumentTypeDebugPacketConfig(M mod) {
        super(mod, "debug_packet", SingletonArgumentInfo.contextFree(() -> ArgumentTypeDebugPacket.INSTANCE), ArgumentTypeDebugPacket.class);
    }
}
