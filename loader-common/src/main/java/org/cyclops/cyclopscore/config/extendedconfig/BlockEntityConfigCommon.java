package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for block entities.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfig
 */
public abstract class BlockEntityConfigCommon<T extends BlockEntity, M extends IModBase> extends ExtendedConfigRegistry<BlockEntityConfigCommon<T, M>, BlockEntityType<T>, M> {

    /**
     * Create a new config
     *
     * @param mod                The mod instance.
     * @param namedId            A unique name id
     * @param elementConstructor The element constructor.
     */
    public BlockEntityConfigCommon(M mod, String namedId, Function<BlockEntityConfigCommon<T, M>, BlockEntityType<T>> elementConstructor) {
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
    public Registry<? super BlockEntityType<T>> getRegistry() {
        return BuiltInRegistries.BLOCK_ENTITY_TYPE;
    }
}
