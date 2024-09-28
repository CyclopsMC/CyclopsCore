package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfigCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * @author rubensworks
 */
public abstract class GuiActionCommonBase<T extends AbstractContainerMenu, M extends IModBase> extends ConfigurableTypeActionRegistry<GuiConfigCommon<T, M>, MenuType<T>, M> {
    @Override
    public void onRegisterModInit(GuiConfigCommon<T, M> eConfig) {
        super.onRegisterModInit(eConfig);
        eConfig.addInstanceTransformer(this::transformMenuType);
    }

    protected abstract MenuType<T> transformMenuType(MenuType<T> menuType);
}
