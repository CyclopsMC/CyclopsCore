package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import net.minecraftforge.fml.network.IContainerFactory;

/**
 * A {@link ContainerType} for a {@link IContainerFactory},
 * which enables additional information to be passed to containers using packet buffers.
 * This enables more convenient syntax via lambdas than the default {@link ContainerType}.
 *
 * For example: `new ContainerTypeData(ContainerAbilityContainer::new))`.
 *
 * @author rubensworks
 */
public class ContainerTypeData<T extends Container> extends ContainerType<T> {
    public ContainerTypeData(IContainerFactory<T> factory) {
        super(factory);
    }
}
