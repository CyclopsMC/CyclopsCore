package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for recipe serializers.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class TileEntityConfig<T extends TileEntity> extends ExtendedConfigForge<TileEntityConfig<T>, TileEntityType<T>> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public TileEntityConfig(ModBase mod, String namedId, Function<TileEntityConfig<T>, TileEntityType<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "tileentity." + getMod().getModId() + "." + getNamedId();
	}

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }
    
    @Override
	public ConfigurableType getConfigurableType() {
		return ConfigurableType.TILE_ENTITY;
	}

    @Override
    public IForgeRegistry<? super TileEntityType<T>> getRegistry() {
        return ForgeRegistries.TILE_ENTITIES;
    }

}
