package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for block entities.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public abstract class BlockEntityConfig<T extends BlockEntity> extends BlockEntityConfigCommon<T, ModBase<?>> {
    public BlockEntityConfig(ModBase<?> mod, String namedId, Function<BlockEntityConfigCommon<T, ModBase<?>>, BlockEntityType<T>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }
}
