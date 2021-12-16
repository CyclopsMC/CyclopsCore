package org.cyclops.cyclopscore.modcompat;

/**
 * Interface for external mod compatibilities.
 * @see IExternalCompat
 * @author rubensworks
 *
 */
public interface IModCompat extends IExternalCompat {

    /**
     * Get the unique mod ID.
     * @return The mod ID.
     */
    public default String getModId() {
        return this.getId();
    }

}
