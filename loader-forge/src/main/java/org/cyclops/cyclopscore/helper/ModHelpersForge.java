package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public class ModHelpersForge extends ModHelpersCommon implements IModHelpersForge {

    public static final ModHelpersForge INSTANCE = new ModHelpersForge();

    private ModHelpersForge() {}

    @Override
    public IMinecraftHelpers getMinecraftHelpers() {
        return new MinecraftHelpersForge();
    }

    @Override
    public ICapabilityHelpersForge getCapabilityHelpers() {
        return new CapabilityHelpersForge(this);
    }
}
