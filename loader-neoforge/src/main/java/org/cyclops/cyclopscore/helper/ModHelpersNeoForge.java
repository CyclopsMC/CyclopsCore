package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public class ModHelpersNeoForge implements IModHelpers {

    public static final ModHelpersNeoForge INSTANCE = new ModHelpersNeoForge();

    private ModHelpersNeoForge() {}

    @Override
    public IMinecraftHelpers getMinecraftHelpers() {
        return new MinecraftHelpersNeoForge();
    }

    @Override
    public IMinecraftClientHelpers getMinecraftClientHelpers() {
        return new MinecraftClientHelpersCommon();
    }

    @Override
    public IL10NHelpers getL10NHelpers() {
        return new L10NHelpersCommon(this);
    }
}
