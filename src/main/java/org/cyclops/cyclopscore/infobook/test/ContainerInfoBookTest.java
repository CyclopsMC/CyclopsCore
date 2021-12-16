package org.cyclops.cyclopscore.infobook.test;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.SimpleContainer;
import net.minecraft.network.FriendlyByteBuf;
import org.cyclops.cyclopscore.RegistryEntries;
import org.cyclops.cyclopscore.inventory.container.InventoryContainer;

/**
 * Container for the test book.
 * @author rubensworks
 */
public class ContainerInfoBookTest extends InventoryContainer {

    public ContainerInfoBookTest(int id, Inventory inventory, FriendlyByteBuf packetBuffer) {
        this(id, inventory);
    }

    public ContainerInfoBookTest(int id, Inventory playerInventory) {
        super(RegistryEntries.CONTAINER_INFOBOOK_TEST, id, playerInventory, new SimpleContainer(0));
    }

    @Override
    protected int getSizeInventory() {
        return 0;
    }
}
