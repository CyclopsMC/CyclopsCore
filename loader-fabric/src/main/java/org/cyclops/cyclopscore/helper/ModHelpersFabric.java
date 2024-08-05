package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public class ModHelpersFabric implements IModHelpers {

    public static final ModHelpersFabric INSTANCE = new ModHelpersFabric();

    private ModHelpersFabric() {}

    @Override
    public IMinecraftHelpers getMinecraftHelpers() {
        return new MinecraftHelpersFabric();
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
