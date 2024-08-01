package org.cyclops.cyclopscore.capability.registrar;

import com.google.common.collect.Maps;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;

import java.util.Map;
import java.util.function.Supplier;

/**
 * Helper class for registering block entity capabilities.
 * This is mainly useful for hierarchies of block entity classes with overriding capabilities.
 * @author rubensworks
 */
public abstract class BlockEntityCapabilityRegistrar<BE extends BlockEntity> {

    private final Supplier<BlockEntityType<? extends BE>> blockEntityType;
    private final Map<BlockCapability<?, ?>, ICapabilityProvider<? super BE, ?, ?>> registrations = Maps.newIdentityHashMap();

    public BlockEntityCapabilityRegistrar(Supplier<BlockEntityType<? extends BE>> blockEntityType) {
        this.blockEntityType = blockEntityType;
    }

    public abstract void populate();

    public final <T, C> void add(BlockCapability<T, C> capability, ICapabilityProvider<? super BE, C, T> provider) {
        registrations.put(capability, provider);
    }

    public final void register(RegisterCapabilitiesEvent event) {
        this.populate();
        for (Map.Entry<BlockCapability<?, ?>, ICapabilityProvider<? super BE, ?, ?>> entry : registrations.entrySet()) {
            event.registerBlockEntity((BlockCapability) entry.getKey(), this.blockEntityType.get(), entry.getValue());
        }
    }

}
