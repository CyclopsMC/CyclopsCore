package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for villagers.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public abstract class VillagerConfig extends ExtendedConfigForge<VillagerConfig, VillagerProfession> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public VillagerConfig(ModBase<?> mod, String namedId, Function<VillagerConfig, ? extends VillagerProfession> elementConstructor) {
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
        return ConfigurableTypesNeoForge.D_VILLAGER;
    }

    @Override
    public Registry<? super VillagerProfession> getRegistry() {
        return BuiltInRegistries.VILLAGER_PROFESSION;
    }
}
