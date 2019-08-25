package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.entity.merchant.villager.VillagerProfession;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for villagers.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class VillagerConfig extends ExtendedConfigForge<VillagerConfig, VillagerProfession> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabledDefault     If this should is enabled by default. If this is false, this can still
     *                           be enabled through the config file.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param elementConstructor The element constructor.
     */
    public VillagerConfig(ModBase mod, boolean enabledDefault, String namedId,
            String comment, Function<VillagerConfig, ? extends VillagerProfession> elementConstructor) {
        super(mod, enabledDefault, namedId, comment, elementConstructor);
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
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.VILLAGER;
	}

    @Override
    public IForgeRegistry<VillagerProfession> getRegistry() {
        return ForgeRegistries.PROFESSIONS;
    }

}
