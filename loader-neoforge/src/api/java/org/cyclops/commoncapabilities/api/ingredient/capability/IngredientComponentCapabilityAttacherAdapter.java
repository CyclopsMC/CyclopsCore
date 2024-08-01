package org.cyclops.commoncapabilities.api.ingredient.capability;

import net.minecraft.resources.ResourceLocation;

/**
 * A base implementation of {@link IIngredientComponentCapabilityAttacher}.
 * @author rubensworks
 */
public abstract class IngredientComponentCapabilityAttacherAdapter<T, M> implements IIngredientComponentCapabilityAttacher<T, M> {

    private final ResourceLocation targetName;
    private final IngredientComponentCapability<?, ?> capability;

    public IngredientComponentCapabilityAttacherAdapter(ResourceLocation targetName, IngredientComponentCapability<?, ?> capability) {
        this.targetName = targetName;
        this.capability = capability;
    }

    @Override
    public ResourceLocation getTargetName() {
        return this.targetName;
    }

    @Override
    public IngredientComponentCapability<?, ?> getCapability() {
        return this.capability;
    }
}
