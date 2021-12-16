package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.InteractionHand;
import net.minecraft.network.chat.Component;

import javax.annotation.Nullable;

/**
 * A {@link MenuProvider} for held items.
 * @author rubensworks
 */
public class NamedContainerProviderItem implements MenuProvider {

    private final int itemIndex;
    private final InteractionHand hand;
    private final Component title;
    private final IContainerSupplier containerSupplier;

    public NamedContainerProviderItem(int itemIndex, InteractionHand hand, Component title, IContainerSupplier containerSupplier) {
        this.itemIndex = itemIndex;
        this.hand = hand;
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
        return this.containerSupplier.create(id, playerInventory, itemIndex, hand);
    }

    public static interface IContainerSupplier {
        public AbstractContainerMenu create(int id, Inventory playerInventory, int itemIndex, InteractionHand hand);
    }

}
