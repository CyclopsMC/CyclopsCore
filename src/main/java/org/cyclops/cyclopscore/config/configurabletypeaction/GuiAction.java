package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
import org.cyclops.cyclopscore.config.extendedconfig.GuiConfig;

/**
 * The action used for {@link org.cyclops.cyclopscore.config.extendedconfig.GuiConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class GuiAction<T extends Container> extends ConfigurableTypeAction<GuiConfig<T>, ContainerType<T>> {

    @Override
    public void onRegister(GuiConfig<T> eConfig) {
        register(eConfig.getInstance(), (GuiConfig) eConfig);
    }
}
