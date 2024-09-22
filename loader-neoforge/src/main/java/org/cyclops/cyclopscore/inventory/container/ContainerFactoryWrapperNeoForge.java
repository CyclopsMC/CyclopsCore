package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.neoforged.neoforge.network.IContainerFactory;

/**
 * @author rubensworks
 */
public class ContainerFactoryWrapperNeoForge<T extends AbstractContainerMenu> implements IContainerFactory<T> {

    private final IContainerFactoryCommon<T> containerFactoryCommon;

    public ContainerFactoryWrapperNeoForge(IContainerFactoryCommon<T> containerFactoryCommon) {
        this.containerFactoryCommon = containerFactoryCommon;
    }

    @Override
    public T create(int windowId, Inventory inv, RegistryFriendlyByteBuf data) {
        return this.containerFactoryCommon.create(windowId, inv, data);
    }

    @Override
    public T create(int windowId, Inventory inv) {
        return this.containerFactoryCommon.create(windowId, inv);
    }
}
