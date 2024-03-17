package org.cyclops.cyclopscore.modcompat.capabilities;

import net.minecraft.core.Direction;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.cyclops.cyclopscore.datastructure.EnumFacingMap;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * A default sided implementation of the capability provider.
 * @author rubensworks
 */
public class DefaultSidedCapabilityProvider<O, T> implements ICapabilityProvider<O, Direction, T> {

    private final EnumFacingMap<T> capabilities;

    public DefaultSidedCapabilityProvider(EnumFacingMap<T> capabilities) {
        this.capabilities = Objects.requireNonNull(capabilities);
    }

    @Nullable
    @Override
    public T getCapability(O object, Direction context) {
        return capabilities.get(context);
    }

    public static <T, H extends ISidedCapabilityConstructor<T>> EnumFacingMap<T> forAllSides(H constructor) {
        return EnumFacingMap.forAllValues(
                constructor.createForSide(Direction.DOWN),
                constructor.createForSide(Direction.UP),
                constructor.createForSide(Direction.NORTH),
                constructor.createForSide(Direction.SOUTH),
                constructor.createForSide(Direction.WEST),
                constructor.createForSide(Direction.EAST)
        );
    }

    public static interface ISidedCapabilityConstructor<T> {
        public T createForSide(Direction side);
    }
}
