package org.cyclops.cyclopscore.modcompat.waila;

import mcp.mobius.waila.api.IWailaRegistrar;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.config.configurable.ConfigurableBlockLog;
import org.cyclops.cyclopscore.helper.L10NHelpers;

/**
 * Waila support class.
 * @author rubensworks
 *
 */
public class Waila {
    
    /**
     * Waila callback.
     * @param registrar The Waila registrar.
     */
    public static void callbackRegister(IWailaRegistrar registrar){
        registrar.addConfig(Reference.MOD_NAME, getBlockLogConfigId(), L10NHelpers.localize("gui.waila.cyclopscore.logConfig"));
        registrar.registerStackProvider(new BlockLogDataProvider(), ConfigurableBlockLog.class);
    }
    
    /**
     * Config ID.
     * @return The config ID.
     */
    public static String getBlockLogConfigId() {
        return Reference.MOD_NAME + ".tank";
    }
    
}
