package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public abstract class ModHelpersCommon implements IModHelpers {
    @Override
    public IMinecraftClientHelpers getMinecraftClientHelpers() {
        return new MinecraftClientHelpersCommon();
    }

    @Override
    public IL10NHelpers getL10NHelpers() {
        return new L10NHelpersCommon(this);
    }

    @Override
    public IBlockHelpers getBlockHelpers() {
        return new BlockHelpersCommon(this);
    }

    @Override
    public ILocationHelpers getLocationHelpers() {
        return new LocationHelpersCommon();
    }

    @Override
    public IBlockEntityHelpers getBlockEntityHelpers() {
        return new BlockEntityHelpersCommon();
    }

    @Override
    public IInventoryHelpers getInventoryHelpers() {
        return new InventoryHelpersCommon(this);
    }

    @Override
    public IItemStackHelpers getItemStackHelpers() {
        return new ItemStackHelpersCommon();
    }
}
