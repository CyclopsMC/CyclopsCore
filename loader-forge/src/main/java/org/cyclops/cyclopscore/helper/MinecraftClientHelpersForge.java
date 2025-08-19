package org.cyclops.cyclopscore.helper;

import net.minecraft.world.item.crafting.RecipeMap;

/**
 * @author rubensworks
 */
public class MinecraftClientHelpersForge extends MinecraftClientHelpersCommon {

    public static RecipeMap RECIPE_MAP;

    @Override
    public RecipeMap getRecipes() {
        return RECIPE_MAP;
    }
}
