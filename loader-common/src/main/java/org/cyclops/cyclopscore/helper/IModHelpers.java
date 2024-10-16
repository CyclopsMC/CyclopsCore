package org.cyclops.cyclopscore.helper;

/**
 * @author rubensworks
 */
public interface IModHelpers {

    /**
     * @return The helpers owned by the CyclopsCore mod.
     */
    public static IModHelpers get() {
        return CyclopsCoreInstance.MOD.getModHelpers();
    }

    public IMinecraftHelpers getMinecraftHelpers();

    public IMinecraftClientHelpers getMinecraftClientHelpers();

    public IL10NHelpers getL10NHelpers();

    public IBlockHelpers getBlockHelpers();

    public ILocationHelpers getLocationHelpers();

    public IBlockEntityHelpers getBlockEntityHelpers();

    public IInventoryHelpers getInventoryHelpers();

    public IItemStackHelpers getItemStackHelpers();

    public IRenderHelpers getRenderHelpers();

    public IBaseHelpers getBaseHelpers();

    public ICraftingHelpers getCraftingHelpers();

    public IRegistrationHelpers getRegistrationHelpers();

    public IWorldHelpers getWorldHelpers();

    public IGuiHelpers getGuiHelpers();

}
