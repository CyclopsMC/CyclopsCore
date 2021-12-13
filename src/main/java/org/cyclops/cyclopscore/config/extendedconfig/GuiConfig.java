package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.client.gui.IHasContainer;
import net.minecraft.client.gui.ScreenManager;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.inventory.container.Container;
import net.minecraft.inventory.container.ContainerType;
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
public abstract class GuiConfig<T extends Container> extends ExtendedConfigForge<GuiConfig<T>, ContainerType<T>> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public GuiConfig(ModBase mod, String namedId, Function<GuiConfig<T>, ? extends ContainerType<T>> elementConstructor) {
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
    public IForgeRegistry<? super ContainerType<T>> getRegistry() {
        return ForgeRegistries.CONTAINERS;
    }

    @OnlyIn(Dist.CLIENT)
	public abstract <U extends Screen & IHasContainer<T>> ScreenManager.IScreenFactory<T, U> getScreenFactory();

	@OnlyIn(Dist.CLIENT)
    @Override
    public void onRegistered() {
        super.onRegistered();
        ScreenManager.register(getInstance(), getScreenFactory());
    }
}
