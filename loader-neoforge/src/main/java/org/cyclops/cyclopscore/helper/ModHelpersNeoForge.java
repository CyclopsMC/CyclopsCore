package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public class ModHelpersNeoForge extends ModHelpersCommon implements IModHelpersNeoForge {

    public static final ModHelpersNeoForge INSTANCE = new ModHelpersNeoForge();

    private IMinecraftHelpers minecraftHelpers;
    private IMinecraftClientHelpers minecraftClientHelpers;
    private IItemStackHelpers itemStackHelpers;
    private ICapabilityHelpersNeoForge capabilityHelpers;
    private IFluidHelpersNeoForge fluidHelpers;
    private IRenderHelpersNeoForge renderHelpers;
    private IRegistrationHelpers registrationHelpers;
    private IGuiHelpersNeoForge guiHelpers;

    private ModHelpersNeoForge() {}

    @Override
    protected void initializeHelpers() {
        this.minecraftHelpers = new MinecraftHelpersNeoForge();
        super.initializeHelpers();
        if (this.getMinecraftHelpers().isClientSide()) {
            this.minecraftClientHelpers = new MinecraftClientHelpersNeoForge();
            this.renderHelpers = new RenderHelpersNeoForge(this);
            this.guiHelpers = new GuiHelpersNeoForge(this);
        } else {
            this.minecraftClientHelpers = null;
            this.renderHelpers = null;
            this.guiHelpers = null;
        }
        this.itemStackHelpers = new ItemStackHelpersNeoForge();
        this.capabilityHelpers = new CapabilityHelpersNeoForge(this);
        this.fluidHelpers = new FluidHelpersNeoForge();
        this.registrationHelpers = new RegistrationHelpersNeoForge();
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
    public IItemStackHelpers getItemStackHelpers() {
        return this.itemStackHelpers;
    }

    @Override
    public ICapabilityHelpersNeoForge getCapabilityHelpers() {
        return this.capabilityHelpers;
    }

    @Override
    public IFluidHelpersNeoForge getFluidHelpers() {
        return this.fluidHelpers;
    }

    @Override
    public IRenderHelpersNeoForge getRenderHelpers() {
        return this.renderHelpers;
    }

    @Override
    public IRegistrationHelpers getRegistrationHelpers() {
        return this.registrationHelpers;
    }

    @Override
    public IGuiHelpersNeoForge getGuiHelpers() {
        return this.guiHelpers;
    }
}
