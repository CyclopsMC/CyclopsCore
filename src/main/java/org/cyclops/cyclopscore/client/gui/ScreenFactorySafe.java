package org.cyclops.cyclopscore.client.gui;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

/**
 * A type-safe implementation of {@link ScreenManager.IScreenFactory}.
 * This enables more convenient syntax via lambdas than the default {@link ScreenManager.IScreenFactory}.
 *
 * For example: `new ScreenFactorySafe(ContainerScreenAbilityContainer::new)`.
 *
 * @author rubensworks
 */
@OnlyIn(Dist.CLIENT)
public class ScreenFactorySafe<T extends Container, U1 extends Screen & IHasContainer<T>, U2 extends Screen & IHasContainer<T>> implements ScreenManager.IScreenFactory<T, U1> {

    private final ScreenManager.IScreenFactory<T, U2> screenFactoryInner;

    public ScreenFactorySafe(ScreenManager.IScreenFactory<T, U2> screenFactoryInner) {
        this.screenFactoryInner = screenFactoryInner;
    }

    @Override
    public U1 create(T container, PlayerInventory inventory, ITextComponent title) {
        return (U1) this.screenFactoryInner.create(container, inventory, title);
    }

}
