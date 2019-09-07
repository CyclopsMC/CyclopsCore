package org.cyclops.cyclopscore.modcompat.capabilities;

import net.minecraft.util.Direction;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.common.util.LazyOptional;
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
    public <T2> LazyOptional<T2> getCapability(Capability<T2> capability, Direction facing) {
        Pair<Capability<T>, T> value = capabilities.get(facing);
        if(value != null
                && Objects.requireNonNull(value.getKey(), "A registered capability is null")
                == Objects.requireNonNull(capability, "A given capability is null")) {
            return LazyOptional.of(() -> value.getValue()).cast();
        }
        return LazyOptional.empty();
    }

    public static <T, H extends ISidedCapabilityConstructor<T>> EnumFacingMap<Pair<Capability<T>, T>> forAllSides(Capability<T> capabilityType, H constructor) {
        return EnumFacingMap.forAllValues(
                Pair.of(capabilityType, constructor.createForSide(Direction.DOWN)),
                Pair.of(capabilityType, constructor.createForSide(Direction.UP)),
                Pair.of(capabilityType, constructor.createForSide(Direction.NORTH)),
                Pair.of(capabilityType, constructor.createForSide(Direction.SOUTH)),
                Pair.of(capabilityType, constructor.createForSide(Direction.WEST)),
                Pair.of(capabilityType, constructor.createForSide(Direction.EAST))
        );
    }

    public static interface ISidedCapabilityConstructor<T> {
        public T createForSide(Direction side);
    }
}
