package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

/**
 * @author rubensworks
 */
public interface IContainerFactoryCommon<T extends AbstractContainerMenu> extends MenuType.MenuSupplier<T> {
    T create(int windowId, Inventory inv, RegistryFriendlyByteBuf data);

    @Override
    default T create(int p_create_1_, Inventory p_create_2_) {
        return create(p_create_1_, p_create_2_, null);
    }
}
