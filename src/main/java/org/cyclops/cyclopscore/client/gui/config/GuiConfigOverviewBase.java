package org.cyclops.cyclopscore.client.gui.config;

/**
 * Overview config screen.
 * Extend this class and make sure that this new class has a constructor with single parameter {@link Screen}
 * @author rubensworks
 *
 */
// TODO: Update when Forge updates FMLConfigGuiFactory
//public abstract class GuiConfigOverviewBase extends GuiConfig {
//
//	/**
//	 * Make a new instance.
//	 * @param mod The mod.
//	 * @param parentScreen the parent GuiScreen object
//	 */
//	public GuiConfigOverviewBase(ModBase mod, Screen parentScreen) {
//		super(parentScreen, getConfigElements(mod), mod.getModId(), false, false,
//		         L10NHelpers.localize("config." + mod.getModId(), mod.getModName()));
//	}
//
//	public abstract ModBase getMod();
//
//	private static List<IConfigElement> getConfigElements(ModBase mod) {
//		List<IConfigElement> list = new ArrayList<IConfigElement>();
//		for(String category : mod.getConfigHandler().getCategories()) {
//		    list.add(new DummyConfigElement.DummyCategoryElement(category, "config." + mod.getModId() + "." + category.replaceAll("\\s", ""), ExtendedCategoryEntry.class));
//		}
//		return list;
//	}
//
//	/**
//	 * A category entry for {@link ConfigurableTypeCategory}.
//	 * @author rubensworks
//	 *
//	 */
//	public static class ExtendedCategoryEntry extends CategoryEntry {
//
//		private String category;
//
//		/**
//		 * Make a new instance.
//		 * @param config The config gui.
//		 * @param entries The gui entries.
//		 * @param element The config element for this category.
//		 */
//		public ExtendedCategoryEntry(GuiConfig config, GuiConfigEntries entries,
//				IConfigElement element) {
//			super(config, entries, element);
//        }
//
//		@Override
//        protected Screen buildChildScreen() {
//            ModBase mod = ((GuiConfigOverviewBase) this.owningScreen).getMod();
//
//			// Cheaty way of getting the current ConfigurableTypeCategory.
//			this.category = configElement.getName();
//            // Get all the elements inside this category
//			List<IConfigElement> elements = (new ConfigElement(mod.getConfigHandler().getConfig()
//            		.getCategory(category))).getChildElements();
//			return new GuiConfig(this.owningScreen, elements,
//                    this.owningScreen.modID, category,
//                    this.configElement.requiresWorldRestart()
//                    	|| this.owningScreen.allRequireWorldRestart,
//                    this.configElement.requiresMcRestart()
//                    	|| this.owningScreen.allRequireMcRestart,
//                    L10NHelpers.localize("config.cyclopscore", mod.getModName()) + " > " + L10NHelpers.localize("config." + mod.getModId() + "." + this.category.replaceAll("\\s", "")));
//        }
//
//	}
//
//}
