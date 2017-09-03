package org.cyclops.cyclopscore.modcompat.capabilities;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.datastructure.EnumFacingMap;

import java.util.Objects;

/**
 * A default sided implementation of the capability provider.
 * @author rubensworks
 */
public class DefaultSidedCapabilityProvider<T> implements ICapabilityProvider {

    private final EnumFacingMap<Pair<Capability<T>, T>> capabilities;

    public DefaultSidedCapabilityProvider(EnumFacingMap<Pair<Capability<T>, T>> capabilities) {
        this.capabilities = Objects.requireNonNull(capabilities);
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return capabilities.containsKey(facing) && capabilities.get(facing).getKey() == capability;
    }

    @Override
    public <T2> T2 getCapability(Capability<T2> capability, EnumFacing facing) {
        if(hasCapability(capability, facing)) {
            return (T2) capabilities.get(facing).getValue();
        }
        return null;
    }

    public static <T, H extends ISidedCapabilityConstructor<T>> EnumFacingMap<Pair<Capability<T>, T>> forAllSides(Capability<T> capabilityType, H constructor) {
        return EnumFacingMap.forAllValues(
                Pair.of(capabilityType, constructor.createForSide(EnumFacing.DOWN)),
                Pair.of(capabilityType, constructor.createForSide(EnumFacing.UP)),
                Pair.of(capabilityType, constructor.createForSide(EnumFacing.NORTH)),
                Pair.of(capabilityType, constructor.createForSide(EnumFacing.SOUTH)),
                Pair.of(capabilityType, constructor.createForSide(EnumFacing.WEST)),
                Pair.of(capabilityType, constructor.createForSide(EnumFacing.EAST))
        );
    }

    public static interface ISidedCapabilityConstructor<T> {
        public T createForSide(EnumFacing side);
    }
}
