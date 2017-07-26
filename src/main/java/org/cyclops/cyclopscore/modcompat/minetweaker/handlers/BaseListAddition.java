package org.cyclops.cyclopscore.modcompat.minetweaker.handlers;

import minetweaker.MineTweakerAPI;
import net.minecraftforge.fml.common.Loader;
import org.cyclops.cyclopscore.Reference;

import java.util.List;

/**
 * Taken from jared's MTlib
 */
public abstract class BaseListAddition<T> extends BaseListModification<T> {

    protected BaseListAddition(String name, List<T> list) {
        super(name, list);
    }

    protected BaseListAddition(String name, List<T> list, List<T> recipies) {
        this(name, list);
        if(recipes != null) {
            recipes.addAll(recipies);
        }
    }

    @Override
    public void apply() {
        if(recipes.isEmpty()) {
            return;
        }

        for(T recipe : recipes) {
            if(recipe != null) {
                if(list.add(recipe)) {
                    successful.add(recipe);
                    if (Loader.isModLoaded(Reference.MOD_JEI)) {
                        MineTweakerAPI.getIjeiRecipeRegistry().addRecipe(wrapRecipe(recipe) != null ? wrapRecipe(recipe) : recipe);
                    }
                } else {
                    MineTweakerAPI.logError(String.format("Error adding %s Recipe for %s", name, getRecipeInfo(recipe)));
                }
            } else {
                MineTweakerAPI.logError(String.format("Error adding %s Recipe: null object", name));
            }
        }
    }

    @Override
    public void undo() {
        if(this.successful.isEmpty()) {
            return;
        }

        for(T recipe : successful) {
            if(recipe != null) {
                if(!list.remove(recipe)) {
                    MineTweakerAPI.logError(String.format("Error removing %s Recipe for %s", name, this.getRecipeInfo(recipe)));
                }else{
                    if (Loader.isModLoaded(Reference.MOD_JEI)) {
                        MineTweakerAPI.getIjeiRecipeRegistry().removeRecipe(wrapRecipe(recipe) != null ? wrapRecipe(recipe) : recipe);
                    }
                }
            } else {
                MineTweakerAPI.logError(String.format("Error removing %s Recipe: null object", name));
            }
        }
    }

    @Override
    public String describe() {
        return String.format("Adding %d %s Recipe(s) for %s", recipes.size(), name, getRecipeInfo());
    }

    @Override
    public String describeUndo() {
        return String.format("Removing %d %s Recipe(s) for %s", recipes.size(), name, getRecipeInfo());
    }
}