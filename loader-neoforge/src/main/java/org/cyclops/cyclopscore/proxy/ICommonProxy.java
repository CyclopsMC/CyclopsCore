package org.cyclops.cyclopscore.proxy;

import net.neoforged.neoforge.client.event.RegisterKeyMappingsEvent;
import org.cyclops.cyclopscore.client.key.IKeyRegistry;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;


/**
 * Interface for common proxies.
 * @author rubensworks
 */
public interface ICommonProxy extends ICommonProxyCommon {

    /**
     * @return The mod for this proxy.
     */
    public ModBaseNeoForge getMod();

    /**
     * Register key bindings.
     * @param keyRegistry The key registry to register to.
     * @param event Register mappings event
     */
    public void registerKeyBindings(IKeyRegistry keyRegistry, RegisterKeyMappingsEvent event);

}
