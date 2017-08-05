package org.cyclops.cyclopscore.modcompat.crafttweaker.handlers;

import crafttweaker.CraftTweakerAPI;

import java.util.List;

/**
 * Taken from jared's MTlib
 */
public abstract class BaseListRemoval<T> extends BaseListModification<T> {

    protected BaseListRemoval(String name, List<T> list) {
        super(name, list);
    }

    protected BaseListRemoval(String name, List<T> list, List<T> recipies) {
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
        for(T recipe : this.recipes) {
            if(recipe != null) {
                if(this.list.remove(recipe)) {

                    successful.add(recipe);
                } else {
                    CraftTweakerAPI.logError(String.format("Error removing %s Recipe for %s", name, getRecipeInfo(recipe)));
                }
            } else {
                CraftTweakerAPI.logError(String.format("Error removing %s Recipe: null object", name));
            }
        }
    }

    @Override
    public String describe() {
        return String.format("Removing %d %s Recipe(s) for %s", this.recipes.size(), this.name, this.getRecipeInfo());
    }
}
