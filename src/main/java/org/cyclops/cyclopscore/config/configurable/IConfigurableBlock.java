package org.cyclops.cyclopscore.config.configurable;

/**
 * Configurable blocks.
 * @author rubensworks
 */
public interface IConfigurableBlock extends IConfigurable {

    /**
     * If this block has a corresponding GUI.
     * This required the block to implement {@link org.cyclops.cyclopscore.inventory.IGuiContainerProvider}.
     * @return If it has a GUI.
     */
    public boolean hasGui();

}
