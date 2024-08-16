package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import org.cyclops.cyclopscore.config.extendedconfig.EntityConfig;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * The action used for {@link EntityConfig}.
 * @param <T> The entity type.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
// TODO: append NeoForge to name in next major
public class EntityAction<T extends Entity, M extends ModBase> extends ConfigurableTypeActionRegistry<EntityConfig<T, M>, EntityType<T>, M> {

}
