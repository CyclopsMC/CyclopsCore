package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.level.levelgen.feature.StructureFeature;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for world structures.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class WorldStructureConfig extends ExtendedConfigForge<WorldStructureConfig, StructureFeature<?>>{

    public WorldStructureConfig(ModBase mod, String namedId, Function<WorldStructureConfig, ? extends StructureFeature<?>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }
    
    @Override
	public String getTranslationKey() {
		return "features." + getMod().getModId() + "." + getNamedId();
	}
    
    @Override
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.WORLD_STRUCTURE;
	}

    @Override
    public IForgeRegistry<StructureFeature<?>> getRegistry() {
        return ForgeRegistries.STRUCTURE_FEATURES;
    }

}
