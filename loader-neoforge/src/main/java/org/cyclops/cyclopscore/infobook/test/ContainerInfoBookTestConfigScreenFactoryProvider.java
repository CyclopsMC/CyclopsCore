package org.cyclops.cyclopscore.infobook.test;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import org.cyclops.cyclopscore.client.gui.ScreenFactorySafe;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigScreenFactoryProvider;

/**
 * @author rubensworks
 */
public class ContainerInfoBookTestConfigScreenFactoryProvider extends GuiConfigScreenFactoryProvider<ContainerInfoBookTest> {
    @Override
    public <U extends Screen & MenuAccess<ContainerInfoBookTest>> MenuScreens.ScreenConstructor<ContainerInfoBookTest, U> getScreenFactory() {
        return new ScreenFactorySafe<>(ContainerScreenInfoBookTest::new);
    }
}
