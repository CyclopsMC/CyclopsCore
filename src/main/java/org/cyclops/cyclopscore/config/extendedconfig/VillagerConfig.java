package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.configurable.ConfigurableVillager;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for villagers.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class VillagerConfig extends ExtendedConfig<VillagerConfig> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public VillagerConfig(ModBase mod, boolean enabled, String namedId,
            String comment, Class<? extends ConfigurableVillager> element) {
        super(mod, enabled, namedId, comment, element);
    }
    
    @Override
	public String getTranslationKey() {
		return "entity.villager." + getNamedId();
	}
    
    @Override
    public String getFullTranslationKey() {
        return "entity.Villager." + getNamedId();
    }

    @Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.VILLAGER;
	}

    @Override
    public IForgeRegistry<?> getRegistry() {
        return ForgeRegistries.VILLAGER_PROFESSIONS;
    }

}
