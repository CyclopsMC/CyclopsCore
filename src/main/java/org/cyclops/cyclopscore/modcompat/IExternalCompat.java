package org.cyclops.cyclopscore.modcompat;

/**
 * Interface for external compatibilities.
 * Add instances to the {@link ModCompatLoader#compats} list.
 * Note that classes implementing this interface can NOT use classes
 * from the targeted mod, since an instance of the Compat will be
 * created anyways, and otherwise certain class definitions won't be found.
 * @author rubensworks
 *
 */
public interface IExternalCompat {

    /**
     * @return The unique id of this compat, such as the mod id.
     */
    public String getId();
    /**
     * @return If this mod compat is enabled by default.
     */
    public boolean isEnabledDefault();
    
    /**
     * @return The comment of this mod compat in the config file.
     */
    public String getComment();

    /**
     * Create a new compat intializer.
     * This should contain all logic to initialize the compat,
     * and will only be loaded if the compat *can* be loaded,
     * so you can safely refer to third-party mod classes in this initializer.
     * @return A new compat initializer instance.
     */
    public ICompatInitializer createInitializer();
    
}
