package org.cyclops.cyclopscore.event;

import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Event hook for {@link ConfigChangedEvent}.
 * @author rubensworks
 *
 */
public class ConfigChangedEventHook {

    private final ModBase mod;

    public ConfigChangedEventHook(ModBase mod) {
        this.mod = mod;
    }
    
    /**
     * Update the configurables when the options are changed from the config gui.
     * @param eventArgs The Forge event required for this.
     */
    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.modID.equals(mod.getModId())) {
        	mod.getConfigHandler().syncProcessedConfigs();
        }
    }
}
