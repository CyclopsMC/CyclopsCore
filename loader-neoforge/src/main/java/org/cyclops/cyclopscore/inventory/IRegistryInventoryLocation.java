package org.cyclops.cyclopscore.inventory;

import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.init.IRegistry;

import javax.annotation.Nullable;
import java.util.Collection;

/**
 * @author rubensworks
 */
public interface IRegistryInventoryLocation extends IRegistry {

    public void register(IInventoryLocation inventoryLocation);

    @Nullable
    public IInventoryLocation get(ResourceLocation uniqueName);

    public Collection<IInventoryLocation> values();

}
