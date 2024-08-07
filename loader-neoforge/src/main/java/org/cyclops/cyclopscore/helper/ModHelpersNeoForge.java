package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public class ModHelpersNeoForge extends ModHelpersCommon {

    public static final ModHelpersNeoForge INSTANCE = new ModHelpersNeoForge();

    private ModHelpersNeoForge() {}

    @Override
    public IMinecraftHelpers getMinecraftHelpers() {
        return new MinecraftHelpersNeoForge();
    }
}
