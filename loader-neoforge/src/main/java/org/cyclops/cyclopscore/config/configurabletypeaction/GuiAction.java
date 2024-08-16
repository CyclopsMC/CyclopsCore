package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfig;

/**
 * The action used for {@link org.cyclops.cyclopscore.config.extendedconfig.GuiConfig}.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
// TODO: append NeoForge to name in next major
public class GuiAction<T extends AbstractContainerMenu> extends ConfigurableTypeActionForge<GuiConfig<T>, MenuType<T>> {

}
