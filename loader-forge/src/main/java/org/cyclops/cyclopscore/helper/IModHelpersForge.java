package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public interface IModHelpersForge extends IModHelpers {

    /**
     * @return The helpers owned by the CyclopsCore mod.
     */
    public static IModHelpersForge get() {
        return (IModHelpersForge) CyclopsCoreInstance.MOD.getModHelpers();
    }

    public IRenderHelpersForge getRenderHelpers();

    public ICapabilityHelpersForge getCapabilityHelpers();

    public IFluidHelpersForge getFluidHelpers();

}
