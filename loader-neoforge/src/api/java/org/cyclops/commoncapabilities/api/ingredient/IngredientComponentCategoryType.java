package org.cyclops.commoncapabilities.api.ingredient;

import net.minecraft.resources.ResourceLocation;

import java.util.function.Function;

/**
 * A categorizer for an instance type.
 * @param <T> An instance type.
 * @param <M> A match condition type.
 * @param <C> A classifier type.
 * @author rubensworks
 */
public class IngredientComponentCategoryType<T, M, C> {

    private final ResourceLocation name;
    private final Class<C> categoryType;
    private final boolean referenceEqual;
    private final Function<T, C> classifier;
    private final M matchCondition;
    private final boolean primaryQuantifier;

    public IngredientComponentCategoryType(ResourceLocation name, Class<C> categoryType, boolean referenceEqual,
                                           Function<T, C> classifier, M matchCondition, boolean primaryQuantifier) {
        this.name = name;
        this.categoryType = categoryType;
        this.referenceEqual = referenceEqual;
        this.classifier = classifier;
        this.matchCondition = matchCondition;
        this.primaryQuantifier = primaryQuantifier;
    }

    /**
     * @return The name of this category.
     */
    public ResourceLocation getName() {
        return name;
    }

    /**
     * @return The category class.
     */
    public Class<C> getCategoryType() {
        return categoryType;
    }

    /**
     * @return If elements of the given category type can be compared using reference-equality.
     *         This should be set to true where possible, as it will lead to better performance.
     */
    public boolean isReferenceEqual() {
        return referenceEqual;
    }

    /**
     * @return A classifier function that will map an instance to a category of the given type.
     */
    public Function<T, C> getClassifier() {
        return classifier;
    }

    /**
     * @return The match condition that identifies exactly this this category type.
     */
    public M getMatchCondition() {
        return matchCondition;
    }

    /**
     * @return If this categorizer is indicative of instance quantities.
     */
    public boolean isPrimaryQuantifier() {
        return primaryQuantifier;
    }

    @Override
    public String toString() {
        return "[IngredientComponentCategoryType " + this.name + "]";
    }
}
