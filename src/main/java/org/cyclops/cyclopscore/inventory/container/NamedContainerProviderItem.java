package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.INamedContainerProvider;
import net.minecraft.util.Hand;
import net.minecraft.util.text.ITextComponent;

import javax.annotation.Nullable;

/**
 * A {@link INamedContainerProvider} for held items.
 * @author rubensworks
 */
public class NamedContainerProviderItem implements INamedContainerProvider {

    private final int itemIndex;
    private final Hand hand;
    private final ITextComponent title;
    private final IContainerSupplier containerSupplier;

    public NamedContainerProviderItem(int itemIndex, Hand hand, ITextComponent title, IContainerSupplier containerSupplier) {
        this.itemIndex = itemIndex;
        this.hand = hand;
        this.title = title;
        this.containerSupplier = containerSupplier;
    }

    @Override
    public ITextComponent getDisplayName() {
        return this.title;
    }

    @Nullable
    @Override
    public Container createMenu(int id, PlayerInventory playerInventory, PlayerEntity player) {
        return this.containerSupplier.create(id, playerInventory, itemIndex, hand);
    }

    public static interface IContainerSupplier {
        public Container create(int id, PlayerInventory playerInventory, int itemIndex, Hand hand);
    }

}
