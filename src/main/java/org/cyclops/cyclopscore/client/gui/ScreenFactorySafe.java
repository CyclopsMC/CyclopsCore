package org.cyclops.cyclopscore.client.gui;

import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A type-safe implementation of {@link MenuScreens.IScreenFactory}.
 * This enables more convenient syntax via lambdas than the default {@link MenuScreens.IScreenFactory}.
 *
 * For example: `new ScreenFactorySafe(ContainerScreenAbilityContainer::new)`.
 *
 * @author rubensworks
 */
@OnlyIn(Dist.CLIENT)
public class ScreenFactorySafe<T extends AbstractContainerMenu, U1 extends Screen & MenuAccess<T>, U2 extends Screen & MenuAccess<T>> implements MenuScreens.ScreenConstructor<T, U1> {

    private final MenuScreens.ScreenConstructor<T, U2> screenFactoryInner;

    public ScreenFactorySafe(MenuScreens.ScreenConstructor<T, U2> screenFactoryInner) {
        this.screenFactoryInner = screenFactoryInner;
    }

    @Override
    public U1 create(T container, Inventory inventory, Component title) {
        return (U1) this.screenFactoryInner.create(container, inventory, title);
    }

}
