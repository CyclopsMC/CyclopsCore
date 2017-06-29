package org.cyclops.cyclopscore.client.gui.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * Config gui factory class.
 * @author rubensworks
 *
 */
public abstract class ExtendedConfigGuiFactoryBase implements IModGuiFactory {

	@Override
    public void initialize(Minecraft minecraftInstance) {
 
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

    protected abstract Class<? extends GuiConfigOverviewBase> mainConfigGuiClass();

    @Override
    public GuiScreen createConfigGui(GuiScreen parentScreen) {
        try {
            return this.mainConfigGuiClass().getDeclaredConstructor(GuiScreen.class).newInstance(parentScreen);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }
	
}
