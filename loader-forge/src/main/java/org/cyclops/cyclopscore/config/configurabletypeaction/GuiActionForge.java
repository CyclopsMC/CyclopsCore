package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.inventory.container.ContainerFactoryWrapperForge;
import org.cyclops.cyclopscore.inventory.container.IContainerFactoryCommon;

/**
 * @author rubensworks
 */
public class GuiActionForge<T extends AbstractContainerMenu, M extends IModBase> extends GuiActionCommonBase<T, M> {
    @Override
    protected MenuType.MenuSupplier<T> transformMenuSupplier(MenuType.MenuSupplier<T> menuSupplier) {
        if (menuSupplier instanceof IContainerFactoryCommon<T> containerFactoryCommon) {
            return new ContainerFactoryWrapperForge<>(containerFactoryCommon);
        }
        return menuSupplier;
    }
}
