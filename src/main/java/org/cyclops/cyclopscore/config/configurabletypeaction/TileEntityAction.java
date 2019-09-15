package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.tileentity.TileEntity;
import net.minecraft.tileentity.TileEntityType;
import org.cyclops.cyclopscore.config.extendedconfig.TileEntityConfig;

/**
 * The action used for {@link TileEntityConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class TileEntityAction<T extends TileEntity> extends ConfigurableTypeActionForge<TileEntityConfig<T>, TileEntityType<T>> {

}
