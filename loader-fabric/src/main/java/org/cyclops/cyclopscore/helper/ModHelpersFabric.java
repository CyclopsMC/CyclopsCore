package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public class ModHelpersFabric extends ModHelpersCommon implements IModHelpersFabric {

    public static final ModHelpersFabric INSTANCE = new ModHelpersFabric();

    private ModHelpersFabric() {}

    @Override
    public IMinecraftHelpers getMinecraftHelpers() {
        return new MinecraftHelpersFabric();
    }
}
