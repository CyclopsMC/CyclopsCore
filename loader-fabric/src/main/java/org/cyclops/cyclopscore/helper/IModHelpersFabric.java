package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public interface IModHelpersFabric extends IModHelpers {

    /**
     * @return The helpers owned by the CyclopsCore mod.
     */
    public static IModHelpersFabric get() {
        return (IModHelpersFabric) CyclopsCoreInstance.MOD.getModHelpers();
    }

    public IRenderHelpersFabric getRenderHelpers();

    public IFluidHelpersFabric getFluidHelpers();

}
