package org.cyclops.cyclopscore.client.gui.config;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.Screen;
import net.minecraftforge.fml.client.IModGuiFactory;

import java.lang.reflect.InvocationTargetException;
import java.util.Set;

/**
 * Config gui factory class.
 * @author rubensworks
 *
 */
// TODO: Update when Forge updates FMLConfigGuiFactory
public abstract class ExtendedConfigGuiFactoryBase implements IModGuiFactory {

	@Override
    public void initialize(Minecraft minecraftInstance) {
 
    }

    @Override
    public boolean hasConfigGui() {
        return true;
    }

//    protected abstract Class<? extends GuiConfigOverviewBase> mainConfigGuiClass();

    @Override
    public Screen createConfigGui(Screen parentScreen) {
//        try {
//            return this.mainConfigGuiClass().getDeclaredConstructor(Screen.class).newInstance(parentScreen);
//        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
//            e.printStackTrace();
//        }
        return null;
    }

    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }
	
}
