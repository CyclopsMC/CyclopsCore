package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public class ModHelpersFabric extends ModHelpersCommon implements IModHelpersFabric {

    public static final ModHelpersFabric INSTANCE = new ModHelpersFabric();

    private IMinecraftHelpers minecraftHelpers;
    private IItemStackHelpers itemStackHelpers;
    private IFluidHelpersFabric fluidHelpers;
    private IRenderHelpersFabric renderHelpers;
    private IRegistrationHelpers registrationHelpers;
    private IGuiHelpersFabric guiHelpers;

    private ModHelpersFabric() {}

    @Override
    protected void initializeHelpers() {
        this.minecraftHelpers = new MinecraftHelpersFabric();
        super.initializeHelpers();
        if (this.getMinecraftHelpers().isClientSide()) {
            this.renderHelpers = new RenderHelpersFabric(this);
            this.guiHelpers = new GuiHelpersFabric(this);
        } else {
            this.renderHelpers = null;
            this.guiHelpers = null;
        }
        this.itemStackHelpers = new ItemStackHelpersFabric();
        this.fluidHelpers = new FluidHelpersFabric();
        this.registrationHelpers = new RegistrationHelpersFabric();
    }

    @Override
    public IMinecraftHelpers getMinecraftHelpers() {
        return this.minecraftHelpers;
    }

    @Override
    public IItemStackHelpers getItemStackHelpers() {
        return this.itemStackHelpers;
    }

    @Override
    public IFluidHelpersFabric getFluidHelpers() {
        return this.fluidHelpers;
    }

    @Override
    public IRenderHelpersFabric getRenderHelpers() {
        return this.renderHelpers;
    }

    @Override
    public IRegistrationHelpers getRegistrationHelpers() {
        return this.registrationHelpers;
    }

    @Override
    public IGuiHelpersFabric getGuiHelpers() {
        return this.guiHelpers;
    }
}
