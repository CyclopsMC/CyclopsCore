package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.gen.placement.Placement;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for world decorators.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class WorldDecoratorConfig extends ExtendedConfigForge<WorldDecoratorConfig, Placement<?>>{

    public WorldDecoratorConfig(ModBase mod, String namedId, Function<WorldDecoratorConfig, ? extends Placement<?>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }
    
    @Override
	public String getTranslationKey() {
		return "decorator." + getMod().getModId() + "." + getNamedId();
	}
    
    @Override
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.WORLD_DECORATOR;
	}

    @Override
    public IForgeRegistry<Placement<?>> getRegistry() {
        return ForgeRegistries.DECORATORS;
    }

}
