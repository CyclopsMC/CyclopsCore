package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.inventory.container.ContainerFactoryWrapperNeoForge;
import org.cyclops.cyclopscore.inventory.container.IContainerFactoryCommon;

/**
 * @author rubensworks
 */
public class GuiActionNeoForge<T extends AbstractContainerMenu, M extends IModBase> extends GuiActionCommonBase<T, M> {
    @Override
    protected MenuType.MenuSupplier<T> transformMenuSupplier(MenuType.MenuSupplier<T> menuSupplier) {
        if (menuSupplier instanceof IContainerFactoryCommon<T> containerFactoryCommon) {
            return new ContainerFactoryWrapperNeoForge<>(containerFactoryCommon);
        }
        return menuSupplier;
    }
}
