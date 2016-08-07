package org.cyclops.cyclopscore.helper.obfuscation;

import net.minecraft.client.particle.ParticleManager;
import net.minecraft.potion.Potion;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.storage.loot.LootPool;
import net.minecraft.world.storage.loot.LootTable;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.List;

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
	
}
