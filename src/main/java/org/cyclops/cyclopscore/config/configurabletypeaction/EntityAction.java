package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;

/**
 * The action used for {@link EntityConfig}.
 * @param <T> The entity type.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class EntityAction<T extends Entity> extends ConfigurableTypeActionForge<EntityConfig<T>, EntityType<T>> {

}
