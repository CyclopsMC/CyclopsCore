package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public abstract class ModHelpersCommon implements IModHelpers {

    private IL10NHelpers l10nHelpers;
    private IBlockHelpers blockHelpers;
    private ILocationHelpers locationHelpers;
    private IBlockEntityHelpers blockEntityHelpers;
    private IInventoryHelpers inventoryHelpers;
    private IRenderHelpers renderHelpers;
    private IBaseHelpers baseHelpers;
    private ICraftingHelpers craftingHelpers;
    private IWorldHelpers worldHelpers;
    private IGuiHelpers guiHelpers;

    protected ModHelpersCommon() {
        this.initializeHelpers();
    }

    protected void initializeHelpers() {
        if (this.getMinecraftHelpers().isClientSide()) {
            this.renderHelpers = new RenderHelpersCommon();
            this.guiHelpers = new GuiHelpersCommon(this);
        } else {
            this.renderHelpers = null;
            this.guiHelpers = null;
        }
        this.l10nHelpers = new L10NHelpersCommon(this);
        this.blockHelpers = new BlockHelpersCommon(this);
        this.locationHelpers = new LocationHelpersCommon();
        this.blockEntityHelpers = new BlockEntityHelpersCommon();
        this.inventoryHelpers = new InventoryHelpersCommon(this);
        this.baseHelpers = new BaseHelpersCommon();
        this.craftingHelpers = new CraftingHelpersCommon(this);
        this.worldHelpers = new WorldHelpersCommon(this);
    }

    @Override
    public IL10NHelpers getL10NHelpers() {
        return this.l10nHelpers;
    }

    @Override
    public IBlockHelpers getBlockHelpers() {
        return this.blockHelpers;
    }

    @Override
    public ILocationHelpers getLocationHelpers() {
        return this.locationHelpers;
    }

    @Override
    public IBlockEntityHelpers getBlockEntityHelpers() {
        return this.blockEntityHelpers;
    }

    @Override
    public IInventoryHelpers getInventoryHelpers() {
        return this.inventoryHelpers;
    }

    @Override
    public IRenderHelpers getRenderHelpers() {
        return this.renderHelpers;
    }

    @Override
    public IBaseHelpers getBaseHelpers() {
        return this.baseHelpers;
    }

    @Override
    public ICraftingHelpers getCraftingHelpers() {
        return this.craftingHelpers;
    }

    @Override
    public IWorldHelpers getWorldHelpers() {
        return this.worldHelpers;
    }

    @Override
    public IGuiHelpers getGuiHelpers() {
        return this.guiHelpers;
    }
}
