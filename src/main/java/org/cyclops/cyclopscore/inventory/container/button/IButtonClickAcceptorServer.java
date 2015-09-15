package org.cyclops.cyclopscore.inventory.container.button;

import net.minecraft.inventory.Container;

/**
 * A server side version of {@link IButtonClickAcceptor}.
 * @author rubensworks
 */
public interface IButtonClickAcceptorServer<C extends Container> extends IButtonClickAcceptor<IButtonActionServer<C>> {

}
