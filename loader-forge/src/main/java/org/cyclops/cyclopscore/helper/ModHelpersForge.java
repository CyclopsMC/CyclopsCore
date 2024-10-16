package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public class ModHelpersForge extends ModHelpersCommon implements IModHelpersForge {

    public static final ModHelpersForge INSTANCE = new ModHelpersForge();

    private ModHelpersForge() {}

    @Override
    public IRenderHelpersForge getRenderHelpers() {
        return new RenderHelpersForge(this);
    }

    @Override
    public IRegistrationHelpers getRegistrationHelpers() {
        return new RegistrationHelpersForge();
    }

    @Override
    public IMinecraftHelpers getMinecraftHelpers() {
        return new MinecraftHelpersForge();
    }

    @Override
    public IItemStackHelpers getItemStackHelpers() {
        return new ItemStackHelpersForge();
    }

    @Override
    public ICapabilityHelpersForge getCapabilityHelpers() {
        return new CapabilityHelpersForge(this);
    }

    @Override
    public IFluidHelpersForge getFluidHelpers() {
        return new FluidHelpersForge();
    }

    @Override
    public IGuiHelpersForge getGuiHelpers() {
        return new GuiHelpersForge(this);
    }
}
