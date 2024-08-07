package org.cyclops.cyclopscore.persist.nbt;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.cyclops.cyclopscore.blockentity.CyclopsBlockEntityCommon;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;


/**
 * If this field should be persisted in Tile Entities.
 * Fields that are a subtype of {@link INBTSerializable} must not be null, they should
 * have a dummy value that will then be populated with the actual values.
 * It will automatically be added to
 * {@link CyclopsBlockEntityCommon#saveAdditional(CompoundTag, HolderLookup.Provider)}
 * and {@link CyclopsBlockEntityCommon#loadAdditional(CompoundTag, HolderLookup.Provider)}.
 * @author rubensworks
 *
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.FIELD)
public @interface NBTPersist {
    /**
     * If set to true, NBT tags that don't contain the key for this field will return a default value as set by Cyclops.
     * Otherwise, NBT tags that don't contain a key for this field will not be overwritten.
     * @return true if non-existing NBT keys are overwritten with a default value, or false otherwise.
     */
    boolean useDefaultValue() default true;
}
