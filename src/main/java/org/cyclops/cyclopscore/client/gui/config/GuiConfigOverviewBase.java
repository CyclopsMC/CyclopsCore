package org.cyclops.cyclopscore.client.gui.config;

import net.minecraft.client.gui.GuiScreen;
import net.minecraftforge.common.config.ConfigElement;
import net.minecraftforge.fml.client.config.DummyConfigElement;
import net.minecraftforge.fml.client.config.GuiConfig;
import net.minecraftforge.fml.client.config.GuiConfigEntries;
import net.minecraftforge.fml.client.config.GuiConfigEntries.CategoryEntry;
import net.minecraftforge.fml.client.config.IConfigElement;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.config.ConfigurableTypeCategory;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.ArrayList;
import java.util.List;

/**
 * Overview config screen.
 * Extend this class and make sure that this new class has a constructor with single parameter {@link GuiScreen}
 * @author rubensworks
 *
 */
public abstract class GuiConfigOverviewBase extends GuiConfig {

    private final ModBase mod;

	/**
	 * Make a new instance.
	 * @param parentScreen the parent GuiScreen object
	 */
	public GuiConfigOverviewBase(ModBase mod, GuiScreen parentScreen) {
		super(parentScreen, getConfigElements(mod), mod.getModId(), false, false,
			GuiConfig.getAbridgedConfigPath(mod.getConfigHandler().getConfig().toString()));
        this.mod = mod;
	}

	public ModBase getMod() {
        return this.mod;
    }

	private ConfigHandler getConfigHandler() {
		return getMod().getConfigHandler();
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	private static List<IConfigElement> getConfigElements(ModBase mod) {
		List<IConfigElement> list = new ArrayList<IConfigElement>();
		for(String category : mod.getConfigHandler().getCategories()) {
			list.add(new DummyConfigElement.DummyCategoryElement(category, category, ExtendedCategoryEntry.class));
		}
		return list;
	}
	
	/**
	 * A category entry for {@link ConfigurableTypeCategory}.
	 * @author rubensworks
	 *
	 */
	public class ExtendedCategoryEntry extends CategoryEntry {

		private String category;
		
		/**
		 * Make a new instance.
		 * @param config The config gui.
		 * @param entries The gui entries.
		 * @param element The config element for this category.
		 */
		@SuppressWarnings("rawtypes")
		public ExtendedCategoryEntry(GuiConfig config, GuiConfigEntries entries,
				IConfigElement element) {
			super(config, entries, element);			
		}
		
		@SuppressWarnings({ "unchecked", "rawtypes" })
		@Override
        protected GuiScreen buildChildScreen() {
			// Cheaty way of getting the current ConfigurableTypeCategory.
			this.category = configElement.getName();
			
			// Get all the elements inside this category
            List<IConfigElement> elements = (new ConfigElement(getConfigHandler().getConfig()
            		.getCategory(category))).getChildElements();
			return new GuiConfig(this.owningScreen, elements,
                    this.owningScreen.modID, category,
                    this.configElement.requiresWorldRestart()
                    	|| this.owningScreen.allRequireWorldRestart, 
                    this.configElement.requiresMcRestart()
                    	|| this.owningScreen.allRequireMcRestart,
                    GuiConfig.getAbridgedConfigPath(getConfigHandler().getConfig().toString()));
        }
		
	}
	
}
