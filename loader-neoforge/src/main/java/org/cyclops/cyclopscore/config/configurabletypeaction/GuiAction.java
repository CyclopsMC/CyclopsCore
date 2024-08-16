package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfig;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * The action used for {@link org.cyclops.cyclopscore.config.extendedconfig.GuiConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
// TODO: append NeoForge to name in next major
public class GuiAction<T extends AbstractContainerMenu> extends ConfigurableTypeActionRegistry<GuiConfig<T>, MenuType<T>, ModBase<?>> {

}
