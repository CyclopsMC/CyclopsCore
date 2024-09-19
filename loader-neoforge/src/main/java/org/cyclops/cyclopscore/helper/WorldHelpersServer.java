package org.cyclops.cyclopscore.helper;

import net.minecraft.world.level.Level;
import net.neoforged.neoforge.server.ServerLifecycleHooks;

/**
 * @author rubensworks
 */
@Deprecated // TODO: remove in next major version
public class WorldHelpersServer {

    public static Level getActiveLevel() {
        return ServerLifecycleHooks.getCurrentServer().getLevel(Level.OVERWORLD);
    }

}
