package org.cyclops.cyclopscore.helper;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.Level;

/**
 * @author rubensworks
 */
public class WorldHelpersCommonClient {

    public static Level getActiveLevel() {
        return Minecraft.getInstance().level;
    }

}
