package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public class ModHelpersFabric extends ModHelpersCommon implements IModHelpersFabric {

    public static final ModHelpersFabric INSTANCE = new ModHelpersFabric();

    private final IMinecraftHelpers minecraftHelpers;

    private ModHelpersFabric() {
        this.minecraftHelpers = new MinecraftHelpersFabric();
    }

    @Override
    public IMinecraftHelpers getMinecraftHelpers() {
        return this.minecraftHelpers;
    }

    @Override
    public IItemStackHelpers getItemStackHelpers() {
        return new ItemStackHelpersFabric();
    }

    @Override
    public IFluidHelpersFabric getFluidHelpers() {
        return new FluidHelpersFabric();
    }

    @Override
    public IRenderHelpersFabric getRenderHelpers() {
        return new RenderHelpersFabric(this);
    }

    @Override
    public IRegistrationHelpers getRegistrationHelpers() {
        return new RegistrationHelpersFabric();
    }
}
