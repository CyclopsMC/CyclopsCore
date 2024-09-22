package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraftforge.network.IContainerFactory;

/**
 * @author rubensworks
 */
public class ContainerFactoryWrapperForge<T extends AbstractContainerMenu> implements IContainerFactory<T> {

    private final IContainerFactoryCommon<T> containerFactoryCommon;

    public ContainerFactoryWrapperForge(IContainerFactoryCommon<T> containerFactoryCommon) {
        this.containerFactoryCommon = containerFactoryCommon;
    }

    @Override
    public T create(int windowId, Inventory inv, FriendlyByteBuf data) {
        return this.containerFactoryCommon.create(windowId, inv, (RegistryFriendlyByteBuf) data);
    }

    @Override
    public T create(int windowId, Inventory inv) {
        return this.containerFactoryCommon.create(windowId, inv);
    }
}
