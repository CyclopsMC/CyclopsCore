package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for client-side guis and server-side containers.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
public abstract class GuiConfigCommon<T extends AbstractContainerMenu, M extends IModBase> extends ExtendedConfigRegistry<GuiConfigCommon<T, M>, MenuType<T>, M> {

    public GuiConfigCommon(M mod, String namedId, Function<GuiConfigCommon<T, M>, ? extends MenuType<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "gui." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.GUI;
    }

    @Override
    public Registry<? super MenuType<T>> getRegistry() {
        return BuiltInRegistries.MENU;
    }

    public abstract GuiConfigScreenFactoryProvider<T> getScreenFactoryProvider();

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();

        MenuScreens.register(getInstance(), getScreenFactoryProvider().getScreenFactory());
    }
}
