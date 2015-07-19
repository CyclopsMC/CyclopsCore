package org.cyclops.cyclopscore.helper.obfuscation;

import net.minecraft.potion.Potion;
import net.minecraftforge.fml.relauncher.ReflectionHelper;
import net.minecraftforge.oredict.ShapedOreRecipe;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

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
	
}
