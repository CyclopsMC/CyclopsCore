package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.IForgeRegistry;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for block entities.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class BlockEntityConfig<T extends BlockEntity> extends ExtendedConfigForge<BlockEntityConfig<T>, BlockEntityType<T>> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public BlockEntityConfig(ModBase mod, String namedId, Function<BlockEntityConfig<T>, BlockEntityType<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "blockentity." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.BLOCK_ENTITY;
    }

    @Override
    public IForgeRegistry<? super BlockEntityType<T>> getRegistry() {
        return ForgeRegistries.BLOCK_ENTITY_TYPES;
    }

}
