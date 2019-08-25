package org.cyclops.cyclopscore.modcompat;

/**
 * A compat initializer.
 *
 * This should contain all logic to initialize the compat,
 * and will only be loaded if the compat *can* be loaded,
 * so you can safely refer to third-party mod classes in this initializer.
 */
public interface ICompatInitializer {

    public void initialize();

}
