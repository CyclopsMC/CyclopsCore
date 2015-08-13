package org.cyclops.cyclopscore.inventory.container.button;

import net.minecraft.inventory.Container;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

/**
 * A server side version of {@link IButtonClickAcceptor}.
 * @author rubensworks
 */
@SideOnly(Side.CLIENT)
public interface IButtonClickAcceptorServer<C extends Container> extends IButtonClickAcceptor<IButtonActionServer<C>> {

}
