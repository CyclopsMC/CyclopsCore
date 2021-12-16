package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.gui.screens.MenuScreens;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.gui.screens.inventory.MenuAccess;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.MenuType;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for client-side guis and server-side containers.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class GuiConfig<T extends AbstractContainerMenu> extends ExtendedConfigForge<GuiConfig<T>, MenuType<T>> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public GuiConfig(ModBase mod, String namedId, Function<GuiConfig<T>, ? extends MenuType<T>> elementConstructor) {
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
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.GUI;
	}

    @Override
    public IForgeRegistry<? super MenuType<T>> getRegistry() {
        return ForgeRegistries.CONTAINERS;
    }

    @OnlyIn(Dist.CLIENT)
	public abstract <U extends Screen & MenuAccess<T>> MenuScreens.ScreenConstructor<T, U> getScreenFactory();

	@OnlyIn(Dist.CLIENT)
    @Override
    public void onRegistered() {
        super.onRegistered();
        MenuScreens.register(getInstance(), getScreenFactory());
    }
}
