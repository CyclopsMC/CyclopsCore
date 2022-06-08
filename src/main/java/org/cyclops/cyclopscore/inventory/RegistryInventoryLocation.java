package org.cyclops.cyclopscore.inventory;

import com.google.common.collect.Maps;
import net.minecraft.resources.ResourceLocation;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Map;

/**
 * @author rubensworks
 */
public class RegistryInventoryLocation implements IRegistryInventoryLocation {

    private static RegistryInventoryLocation INSTANCE = new RegistryInventoryLocation();

    /**
     * @return The unique instance.
     */
    public static RegistryInventoryLocation getInstance() {
        return INSTANCE;
    }

    public Map<String, IInventoryLocation> registry = Maps.newHashMap();

    private RegistryInventoryLocation() {

    }

    @Override
    public void register(IInventoryLocation inventoryLocation) {
        registry.put(inventoryLocation.getUniqueName().toString(), inventoryLocation);
    }

    @Nullable
    @Override
    public IInventoryLocation get(ResourceLocation uniqueName) {
        return registry.get(uniqueName.toString());
    }

    @Override
    public Collection<IInventoryLocation> values() {
        return registry.values();
    }
}
