package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public class ModHelpersNeoForge extends ModHelpersCommon implements IModHelpersNeoForge {

    public static final ModHelpersNeoForge INSTANCE = new ModHelpersNeoForge();

    private ModHelpersNeoForge() {}

    @Override
    public IMinecraftHelpers getMinecraftHelpers() {
        return new MinecraftHelpersNeoForge();
    }

    @Override
    public IRenderHelpersNeoForge getRenderHelpers() {
        return new RenderHelpersNeoForge(this);
    }
}
