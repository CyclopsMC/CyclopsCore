package org.cyclops.cyclopscore.modcompat.capabilities;

import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
import org.checkerframework.checker.units.qual.C;

import javax.annotation.Nullable;

/**
 * Constructor for capabilities.
 * @param <O> The host type that will contain the capability.
 * @param <C> The capability context type
 * @param <T> The capability type
 * @param <CK> The capability key type, such as BlockEntityType.
 * @author rubensworks
 */
public interface IBlockCapabilityConstructor<O, C, T, CK> extends ICapabilityTypeGetter<T, C> {

    @Nullable
    public IBlockCapabilityProvider<T, C> createProvider(CK capabilityKey);

}
