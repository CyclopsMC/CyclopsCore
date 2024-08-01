package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import org.cyclops.cyclopscore.inventory.ItemLocation;

import javax.annotation.Nullable;

/**
 * A {@link MenuProvider} for held items.
 * @author rubensworks
 */
public class NamedContainerProviderItem implements MenuProvider {

    private final ItemLocation itemLocation;
    private final Component title;
    private final IContainerSupplier containerSupplier;

    public NamedContainerProviderItem(ItemLocation itemLocation, Component title, IContainerSupplier containerSupplier) {
        this.itemLocation = itemLocation;
        this.title = title;
        this.containerSupplier = containerSupplier;
    }

    @Override
    public Component getDisplayName() {
        return this.title;
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int id, Inventory playerInventory, Player player) {
        return this.containerSupplier.create(id, playerInventory, itemLocation);
    }

    public static interface IContainerSupplier {
        public AbstractContainerMenu create(int id, Inventory playerInventory, ItemLocation itemLocation);
    }

}
