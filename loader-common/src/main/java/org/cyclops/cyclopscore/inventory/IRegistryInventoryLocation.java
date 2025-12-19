package org.cyclops.cyclopscore.inventory;

import net.minecraft.resources.Identifier;
import org.cyclops.cyclopscore.init.IRegistry;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author rubensworks
 */
public interface IRegistryInventoryLocation extends IRegistry {

    public void register(IInventoryLocation inventoryLocation);

    @Nullable
    public IInventoryLocation get(Identifier uniqueName);

    public Collection<IInventoryLocation> values();

}
