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
    protected MenuType<T> transformMenuType(MenuType<T> menuType) {
        if (menuType.constructor instanceof IContainerFactoryCommon<T> containerFactoryCommon) {
            return new MenuType<>(new ContainerFactoryWrapperForge<>(containerFactoryCommon), menuType.requiredFeatures());
        }
        return menuType;
    }
}
