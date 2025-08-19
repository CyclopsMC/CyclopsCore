package org.cyclops.cyclopscore.helper;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeMap;

/**
 * @author rubensworks
 */
public interface IMinecraftClientHelpers {

    /**
     * @return The player instance.
     */
    public Player getPlayer();

    /**
     * @return If the user is shifted.
     */
    public boolean isShifted();

    /**
     * Note that {@link IMinecraftHelpers#sendRecipesToClients(java.util.function.Supplier)} must first be called for the relevant
     * recipe types.
     * @return Get the recipe map that has been sent from the server to the client.
     */
    public RecipeMap getRecipes();

}
