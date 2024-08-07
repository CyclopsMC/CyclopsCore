package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public class ModHelpersForge extends ModHelpersCommon {

    public static final ModHelpersForge INSTANCE = new ModHelpersForge();

    private ModHelpersForge() {}

    @Override
    public IMinecraftHelpers getMinecraftHelpers() {
        return new MinecraftHelpersForge();
    }
}
