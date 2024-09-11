package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;

/**
 * The action used for {@link EntityConfig}.
 * @param <T> The entity type.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
// TODO: append NeoForge to name in next major
@Deprecated // TODO: rm in next major
public class EntityAction<T extends Entity> extends ConfigurableTypeActionForge<EntityConfig<T>, EntityType<T>> {

}
