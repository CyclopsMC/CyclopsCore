package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;

/**
 * @author rubensworks
 */
public abstract class GuiConfigScreenFactoryProvider<T extends AbstractContainerMenu> {

    public abstract <U extends Screen & MenuAccess<T>> MenuScreens.ScreenConstructor<T, U> getScreenFactory();

}
