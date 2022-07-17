package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.entity.npc.VillagerProfession;
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
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public VillagerConfig(ModBase mod, String namedId, Function<VillagerConfig, ? extends VillagerProfession> elementConstructor) {
        super(mod, namedId, elementConstructor);
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
        return ForgeRegistries.VILLAGER_PROFESSIONS;
    }

}
