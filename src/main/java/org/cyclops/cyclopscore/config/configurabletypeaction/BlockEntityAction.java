package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.BlockEntityConfig;

/**
 * The action used for {@link BlockEntityConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class BlockEntityAction<T extends BlockEntity> extends ConfigurableTypeActionForge<BlockEntityConfig<T>, BlockEntityType<T>> {

}
