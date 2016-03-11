package org.cyclops.cyclopscore.modcompat;

import org.cyclops.cyclopscore.init.IInitListener;

/**
 * Interface for external compatibilities.
 * Implement this on classes that require external functionality
 * that needs to be called in the preInit, init or postInit events.
 * Add instances to the {@link ModCompatLoader#compats} list.
 * Note that classes implementing this interface can NOT use classes
 * from the targeted mod, since an instance of the Compat will be
 * created anyways, and otherwise certain class definitions won't be found.
 * @author rubensworks
 *
 */
public interface IExternalCompat extends IInitListener {
    
    /**
     * @return If this mod compat is enabled by default.
     */
    public boolean isEnabled();
    
    /**
     * @return The comment of this mod compat in the config file.
     */
    public String getComment();
    
}
