package org.cyclops.cyclopscore.helper.obfuscation;

import net.minecraft.entity.Entity;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.fml.common.ObfuscationReflectionHelper;

import java.lang.reflect.Field;

/**
 * Helper for getting private fields or methods.
 * @author rubensworks
 *
 */
public class ObfuscationHelpers {

    /**
     * Get the capabilities of the given entity.
     * @param entity The entity.
     * @return The capability dispatcher.
     */
    public static CapabilityDispatcher getEntityCapabilities(Entity entity) {
        Field field = ObfuscationReflectionHelper.findField(Entity.class, ObfuscationData.ENTITY_CAPABILITIES);
        try {
            return (CapabilityDispatcher) field.get(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }
	
}
