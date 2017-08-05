package org.cyclops.cyclopscore.modcompat.crafttweaker.handlers;

import crafttweaker.CraftTweakerAPI;

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
                } else {
                    CraftTweakerAPI.logError(String.format("Error adding %s Recipe for %s", name, getRecipeInfo(recipe)));
                }
            } else {
                CraftTweakerAPI.logError(String.format("Error adding %s Recipe: null object", name));
            }
        }
    }

    @Override
    public String describe() {
        return String.format("Adding %d %s Recipe(s) for %s", recipes.size(), name, getRecipeInfo());
    }
}