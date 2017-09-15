package org.cyclops.cyclopscore.config.configurable;

import net.minecraft.client.renderer.color.IBlockColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

import org.cyclops.cyclopscore.config.extendedconfig.BlockConfig;

/**
 * Configurable blocks.
 * @author rubensworks
 */
public interface IConfigurableBlock extends IConfigurable<BlockConfig> {

    /**
     * If this block has a corresponding GUI.
     * This required the block to implement {@link org.cyclops.cyclopscore.inventory.IGuiContainerProvider}.
     * @return If it has a GUI.
     */
    public boolean hasGui();

    /**
     * @return The color handler for the block instance.
     */
    @SideOnly(Side.CLIENT)
    public @Nullable IBlockColor getBlockColorHandler();

}
