package org.cyclops.cyclopscore.init;

/**
 * Reference to an object that might not have been initialized already.
 * @author rubensworks
 */
public interface IObjectReference<O> {

    /**
     * @return Materialization of this reference.
     */
    public O getObject();

}
