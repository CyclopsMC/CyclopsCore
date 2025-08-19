package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public class ModHelpersForge extends ModHelpersCommon implements IModHelpersForge {

    public static final ModHelpersForge INSTANCE = new ModHelpersForge();

    private IMinecraftHelpers minecraftHelpers;
    private IMinecraftClientHelpers minecraftClientHelpers;
    private IRenderHelpersForge renderHelpers;
    private IRegistrationHelpers registrationHelpers;
    private IItemStackHelpers itemStackHelpers;
    private ICapabilityHelpersForge capabilityHelpers;
    private IFluidHelpersForge fluidHelpers;
    private IGuiHelpersForge guiHelpers;

    private ModHelpersForge() {}

    @Override
    protected void initializeHelpers() {
        this.minecraftHelpers = new MinecraftHelpersForge();
        super.initializeHelpers();
        if (this.getMinecraftHelpers().isClientSide()) {
            this.minecraftClientHelpers = new MinecraftClientHelpersForge();
            this.renderHelpers = new RenderHelpersForge(this);
            this.guiHelpers = new GuiHelpersForge(this);
        } else {
            this.minecraftClientHelpers = null;
            this.renderHelpers = null;
            this.guiHelpers = null;
        }
        this.registrationHelpers = new RegistrationHelpersForge();
        this.itemStackHelpers = new ItemStackHelpersForge();
        this.capabilityHelpers = new CapabilityHelpersForge(this);
        this.fluidHelpers = new FluidHelpersForge();
    }

    @Override
    public IMinecraftHelpers getMinecraftHelpers() {
        return this.minecraftHelpers;
    }

    @Override
    public IMinecraftClientHelpers getMinecraftClientHelpers() {
        return this.minecraftClientHelpers;
    }

    @Override
    public IRenderHelpersForge getRenderHelpers() {
        return this.renderHelpers;
    }

    @Override
    public IRegistrationHelpers getRegistrationHelpers() {
        return this.registrationHelpers;
    }

    @Override
    public IItemStackHelpers getItemStackHelpers() {
        return this.itemStackHelpers;
    }

    @Override
    public ICapabilityHelpersForge getCapabilityHelpers() {
        return this.capabilityHelpers;
    }

    @Override
    public IFluidHelpersForge getFluidHelpers() {
        return this.fluidHelpers;
    }

    @Override
    public IGuiHelpersForge getGuiHelpers() {
        return this.guiHelpers;
    }
}
