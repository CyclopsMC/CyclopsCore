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
public class EntityAction<T extends Entity> extends ConfigurableTypeAction<EntityConfig<T>, EntityType<T>> {

    @Override
    public void onRegisterForge(EntityConfig<T> eConfig) {
        register(eConfig.getInstance(), (EntityConfig) eConfig);

        // Register
        // TODO: is this still needed?
//        EntityRegistry.registerModEntity(
//                new ResourceLocation(eConfig.getMod().getModId(), eConfig.getSubUniqueName()),
//                clazz,
//                eConfig.getSubUniqueName(),
//                Helpers.getNewId(eConfig.getMod(), Helpers.IDType.ENTITY),
//                eConfig.getMod(),
//                eConfig.getTrackingRange(),
//                eConfig.getUpdateFrequency(),
//                eConfig.sendVelocityUpdates()
//        );
    }

}
