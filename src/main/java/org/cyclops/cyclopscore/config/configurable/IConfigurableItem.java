package org.cyclops.cyclopscore.config.configurable;

import net.minecraft.client.renderer.color.IItemColor;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

import javax.annotation.Nullable;

/**
 * Configurable blocks.
 * @author rubensworks
 */
public interface IConfigurableItem extends IConfigurable {

    /**
     * @return The color handler for the item instance.
     */
    @SideOnly(Side.CLIENT)
    public @Nullable IItemColor getItemColorHandler();

}
