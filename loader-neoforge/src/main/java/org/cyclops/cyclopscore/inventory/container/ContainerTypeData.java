package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.world.flag.FeatureFlagSet;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.neoforge.network.IContainerFactory;

/**
 * A {@link MenuType} for a {@link MenuType.MenuSupplier},
 * which enables additional information to be passed to containers using packet buffers.
 * This enables more convenient syntax via lambdas than the default {@link MenuType}.
 *
 * For example: `new ContainerTypeData(ContainerAbilityContainer::new))`.
 *
 * @author rubensworks
 */
public class ContainerTypeData<T extends AbstractContainerMenu> extends MenuType<T> {
    public ContainerTypeData(IContainerFactory<T> factory, FeatureFlagSet featureFlagSet) {
        super(factory, featureFlagSet);
    }
}
