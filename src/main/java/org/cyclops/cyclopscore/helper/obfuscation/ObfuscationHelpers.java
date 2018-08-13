package org.cyclops.cyclopscore.helper.obfuscation;

import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.client.particle.ParticleManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.Entity;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.common.capabilities.CapabilityDispatcher;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.List;
import java.util.Map;

/**
 * Helper for getting private fields or methods.
 * @author rubensworks
 *
 */
public class ObfuscationHelpers {

    /**
     * Set the private static final 'potionTypes' field from @link{net.minecraft.potion.Potion}
     * @param potionTypes The panorama path.
     */
    public static void setPotionTypesArray(Potion[] potionTypes) {
        Field field = ReflectionHelper.findField(Potion.class, ObfuscationData.POTION_POTIONTYPES);

        Field modifiersField;
        try {
            modifiersField = Field.class.getDeclaredField("modifiers");
            modifiersField.setAccessible(true);
            modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);

            field.setAccessible(true);
            field.set(null, potionTypes);
        } catch (NoSuchFieldException | SecurityException | IllegalAccessException | IllegalArgumentException e) {
            e.printStackTrace();
        }
    }

    /**
     * Get the private 'width' field from {@link net.minecraftforge.oredict.ShapedOreRecipe}.
     * @param recipe The recipe instance.
     * @return width
     */
    public static int getShapedOreRecipeWidth(ShapedOreRecipe recipe) {
        return ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, recipe, ObfuscationData.SHAPEDORERECIPE_WIDTH);
    }

    /**
     * Get the private 'height' field from {@link net.minecraftforge.oredict.ShapedOreRecipe}.
     * @param recipe The recipe instance.
     * @return width
     */
    public static int getShapedOreRecipeHeight(ShapedOreRecipe recipe) {
        return ReflectionHelper.getPrivateValue(ShapedOreRecipe.class, recipe, ObfuscationData.SHAPEDORERECIPE_HEIGHT);
    }

    /**
     * Get the private 'pools' field from {@link LootTable}.
     * @param lootTable The loot table
     * @return classToIDMapping
     */
    public static List<LootPool> getLootPools(LootTable lootTable) {
        return ReflectionHelper.getPrivateValue(LootTable.class, lootTable, ObfuscationData.LOOTTABLE_POOLS);
    }

    /**
     * Get the private 'particleTextures' field from {@link net.minecraft.client.particle.ParticleManager}.
     * @return The private 'particleTextures' field.
     */
    public static ResourceLocation getParticleTexture() {
        return ReflectionHelper.getPrivateValue(ParticleManager.class, null, ObfuscationData.PARTICLE_TEXTURES);
    }

    /**
     * Get the capabilities of the given entity.
     * @param entity The entity.
     * @return The capability dispatcher.
     */
    public static CapabilityDispatcher getEntityCapabilities(Entity entity) {
        Field field = ReflectionHelper.findField(Entity.class, ObfuscationData.ENTITY_CAPABILITIES);
        try {
            return (CapabilityDispatcher) field.get(entity);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Call the private static `register` from @link{CriteriaTriggers}
     * @param criterion The criterion.
     * @param <T> The criterion type.
     * @return The registered instance.
     */
    public static <T extends ICriterionTrigger<?>> T registerCriteriaTrigger(T criterion) {
        Method method = ReflectionHelper.findMethod(CriteriaTriggers.class,
                ObfuscationData.CRITERIATRIGGERS_REGISTER[0], ObfuscationData.CRITERIATRIGGERS_REGISTER[1],
                ICriterionTrigger.class);
        try {
            return (T) method.invoke(null, criterion);
        } catch (SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return Get the private static {@link net.minecraft.client.settings.KeyBinding#KEYBIND_ARRAY field}.
     */
    @SideOnly(Side.CLIENT)
    public static Map<String, KeyBinding> getKeyBindingKeyBindArray() {
        return ReflectionHelper.getPrivateValue(KeyBinding.class, null, ObfuscationData.KEYBINDING_KEYBIND_ARRAY);
    }
	
}
