package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;
import org.cyclops.cyclopscore.inventory.container.IContainerFactoryCommon;

/**
 * @author rubensworks
 */
public abstract class GuiActionCommonBase<T extends AbstractContainerMenu, M extends IModBase> extends ConfigurableTypeActionRegistry<GuiConfigCommon<T, M>, MenuType<T>, M> {
    @Override
    public void onRegisterModInit(GuiConfigCommon<T, M> eConfig) {
        super.onRegisterModInit(eConfig);
        eConfig.addInstanceTransformer(instance -> {
            MenuType.MenuSupplier<T> constructor = instance.constructor;
            if (constructor instanceof IContainerFactoryCommon) {
                constructor = transformMenuSupplier(constructor);
            }
            return new MenuType(constructor, instance.requiredFeatures());
        });
    }

    protected abstract MenuType.MenuSupplier<T> transformMenuSupplier(MenuType.MenuSupplier<T> menuSupplier);
}
