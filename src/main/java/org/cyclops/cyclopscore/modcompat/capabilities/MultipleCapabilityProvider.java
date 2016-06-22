package org.cyclops.cyclopscore.modcompat.capabilities;

import net.minecraft.util.EnumFacing;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

import java.util.Objects;

/**
 * A default implementation of a capability provider for multiple capabilities.
 * @author rubensworks
 */
public class MultipleCapabilityProvider implements ICapabilityProvider {

    private final Capability<?>[] capabilityTypes;
    private final Object[] capabilities;

    public MultipleCapabilityProvider(Capability<?>[] capabilityTypes, Object[] capabilities) {
        assert capabilityTypes.length == capabilities.length;
        this.capabilityTypes = Objects.requireNonNull(capabilityTypes);
        this.capabilities = Objects.requireNonNull(capabilities);
    }

    protected int getCapabilityId(Capability<?> capability) {
        for (int i = 0; i < capabilityTypes.length; i++) {
            Capability<?> capabilityType = capabilityTypes[i];
            if (capabilityType == capability) {
                return i;
            }
        }
        return -1;
    }

    @Override
    public boolean hasCapability(Capability<?> capability, EnumFacing facing) {
        return getCapabilityId(capability) >= 0;
    }

    @Override
    public <T> T getCapability(Capability<T> capability, EnumFacing facing) {
        int capabilityId = getCapabilityId(capability);
        if(capabilityId >= 0) {
            for (Object o : capabilities) {
                return (T) capabilities[capabilityId];
            }
        }
        return null;
    }

    public static <T1, T2> MultipleCapabilityProvider of(Capability<T1> capabilityType1, T1 capability1,
                                                         Capability<T2> capabilityType2, T2 capability2) {
        return new MultipleCapabilityProvider(
                new Capability[]{capabilityType1, capabilityType2},
                new Object[]{capability1, capability2}
        );
    }

    public static <T1, T2, T3> MultipleCapabilityProvider of(Capability<T1> capabilityType1, T1 capability1,
                                                             Capability<T2> capabilityType2, T2 capability2,
                                                             Capability<T3> capabilityType3, T3 capability3) {
        return new MultipleCapabilityProvider(
                new Capability[]{capabilityType1, capabilityType2, capabilityType3},
                new Object[]{capability1, capability2, capability3}
        );
    }
}
