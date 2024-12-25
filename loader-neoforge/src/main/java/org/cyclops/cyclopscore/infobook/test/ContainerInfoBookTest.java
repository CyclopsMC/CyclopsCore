package org.cyclops.cyclopscore.infobook.test;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.player.Inventory;
import org.cyclops.cyclopscore.RegistryEntries;
import org.cyclops.cyclopscore.inventory.container.InventoryContainerCommon;

/**
 * Container for the test book.
 * @author rubensworks
 */
public class ContainerInfoBookTest extends InventoryContainerCommon {

    public ContainerInfoBookTest(int id, Inventory inventory, FriendlyByteBuf packetBuffer) {
        this(id, inventory);
    }

    public ContainerInfoBookTest(int id, Inventory playerInventory) {
        super(RegistryEntries.CONTAINER_INFOBOOK_TEST.get(), id, playerInventory, new SimpleContainer(0));
    }

    @Override
    protected int getSizeInventory() {
        return 0;
    }
}
