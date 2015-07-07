package org.cyclops.cyclopscore.client.gui.config;

import net.minecraft.client.Minecraft;
import net.minecraftforge.fml.client.IModGuiFactory;

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
    public abstract Class<? extends GuiConfigOverviewBase> mainConfigGuiClass();
 
    @Override
    public Set<RuntimeOptionCategoryElement> runtimeGuiCategories() {
        return null;
    }
 
    @Override
    public RuntimeOptionGuiHandler getHandlerFor(RuntimeOptionCategoryElement element) {
        return null;
    }
	
}
