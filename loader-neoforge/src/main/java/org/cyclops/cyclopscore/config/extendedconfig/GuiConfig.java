package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.client.event.RegisterMenuScreensEvent;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for client-side guis and server-side containers.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class GuiConfig<T extends AbstractContainerMenu, M extends ModBase> extends ExtendedConfigForge<GuiConfig<T, M>, MenuType<T>, M> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public GuiConfig(M mod, String namedId, Function<GuiConfig<T, M>, ? extends MenuType<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
        ((ModBase<?>) mod).getModEventBus().addListener(this::onRegisterMenuScreens);
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
    public ConfigurableType getConfigurableType() {
        return ConfigurableTypesNeoForge.GUI;
    }

    @Override
    public Registry<? super MenuType<T>> getRegistry() {
        return BuiltInRegistries.MENU;
    }

    @OnlyIn(Dist.CLIENT)
    public abstract <U extends Screen & MenuAccess<T>> MenuScreens.ScreenConstructor<T, U> getScreenFactory();

    public void onRegisterMenuScreens(RegisterMenuScreensEvent event) {
        event.register(getInstance(), getScreenFactory());
    }
}
