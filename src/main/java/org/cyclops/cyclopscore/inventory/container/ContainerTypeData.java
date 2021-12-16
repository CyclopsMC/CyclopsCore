package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;

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
    public ContainerTypeData(MenuType.MenuSupplier<T> factory) {
        super(factory);
    }
}
