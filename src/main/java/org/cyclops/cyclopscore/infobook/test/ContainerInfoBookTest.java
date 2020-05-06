package org.cyclops.cyclopscore.infobook.test;

import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.inventory.Inventory;
import net.minecraft.network.PacketBuffer;
import org.cyclops.cyclopscore.RegistryEntries;
import org.cyclops.cyclopscore.inventory.container.InventoryContainer;

/**
 * Container for the test book.
 * @author rubensworks
 */
public class ContainerInfoBookTest extends InventoryContainer {

    public ContainerInfoBookTest(int id, PlayerInventory inventory, PacketBuffer packetBuffer) {
        this(id, inventory);
    }

    public ContainerInfoBookTest(int id, PlayerInventory playerInventory) {
        super(RegistryEntries.CONTAINER_INFOBOOK_TEST, id, playerInventory, new Inventory(0));
    }

    @Override
    protected int getSizeInventory() {
        return 0;
    }
}
