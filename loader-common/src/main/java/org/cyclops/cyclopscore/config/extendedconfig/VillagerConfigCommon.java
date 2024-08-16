package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.npc.VillagerProfession;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for villagers.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class VillagerConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<VillagerConfigCommon<M>, VillagerProfession, M> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public VillagerConfigCommon(M mod, String namedId, Function<VillagerConfigCommon<M>, ? extends VillagerProfession> elementConstructor) {
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
    public Registry<? super VillagerProfession> getRegistry() {
        return BuiltInRegistries.VILLAGER_PROFESSION;
    }
}
