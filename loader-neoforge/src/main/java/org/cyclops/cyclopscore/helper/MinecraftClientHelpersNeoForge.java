package org.cyclops.cyclopscore.helper;

import net.minecraft.world.item.crafting.RecipeMap;
import net.neoforged.neoforge.client.event.ClientPlayerNetworkEvent;
import net.neoforged.neoforge.client.event.RecipesReceivedEvent;
import net.neoforged.neoforge.common.NeoForge;

/**
 * @author rubensworks
 */
public class MinecraftClientHelpersNeoForge extends MinecraftClientHelpersCommon {

    public static RecipeMap RECIPE_MAP;

    public MinecraftClientHelpersNeoForge() {
        NeoForge.EVENT_BUS.addListener((RecipesReceivedEvent event) -> MinecraftClientHelpersNeoForge.RECIPE_MAP = event.getRecipeMap());
        NeoForge.EVENT_BUS.addListener((ClientPlayerNetworkEvent.LoggingOut event) -> MinecraftClientHelpersNeoForge.RECIPE_MAP = null);
    }

    @Override
    public RecipeMap getRecipes() {
        return RECIPE_MAP;
    }
}
