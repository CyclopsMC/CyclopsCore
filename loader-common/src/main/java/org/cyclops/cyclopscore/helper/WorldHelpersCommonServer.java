package org.cyclops.cyclopscore.helper;

import net.minecraft.world.level.Level;

/**
 * @author rubensworks
 */
public class WorldHelpersCommonServer {

    public static Level getActiveLevel(IModHelpers modHelpers) {
        return modHelpers.getMinecraftHelpers().getCurrentServer().getLevel(Level.OVERWORLD);
    }

}
