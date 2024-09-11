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
    public ICapabilityHelpersNeoForge getCapabilityHelpers() {
        return new CapabilityHelpersNeoForge(this);
    }

    @Override
    public IFluidHelpersNeoForge getFluidHelpers() {
        return new FluidHelpersNeoForge();
    }

    @Override
    public IRenderHelpersNeoForge getRenderHelpers() {
        return new RenderHelpersNeoForge(this);
    }

    @Override
    public IRegistrationHelpers getRegistrationHelpers() {
        return new RegistrationHelpersNeoForge();
    }
}
