package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public interface IModHelpersNeoForge extends IModHelpers {

    /**
     * @return The helpers owned by the CyclopsCore mod.
     */
    public static IModHelpersNeoForge get() {
        return (IModHelpersNeoForge) CyclopsCoreInstance.MOD.getModHelpers();
    }

    public IRenderHelpersNeoForge getRenderHelpers();

}
