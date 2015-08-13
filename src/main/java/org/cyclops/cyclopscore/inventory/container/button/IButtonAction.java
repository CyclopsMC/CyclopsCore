package org.cyclops.cyclopscore.inventory.container.button;

import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A button action signal interface.
 * @author rubensworks
 */
@SideOnly(Side.CLIENT)
public interface IButtonAction<C extends Container> {

}
