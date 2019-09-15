package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfig;

/**
 * The action used for {@link org.cyclops.cyclopscore.config.extendedconfig.GuiConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class GuiAction<T extends Container> extends ConfigurableTypeActionForge<GuiConfig<T>, ContainerType<T>> {

}
