package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public class ModHelpersForge implements IModHelpers {

    public static final ModHelpersForge INSTANCE = new ModHelpersForge();

    private ModHelpersForge() {}

    @Override
    public IMinecraftHelpers getMinecraftHelpers() {
        return new MinecraftHelpersForge();
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
