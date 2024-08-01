package org.cyclops.commoncapabilities.api.ingredient.capability;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

/**
 * Instances of this interfaces can be used to attach a {@link ICapabilityProvider}
 * to instances of {@link IngredientComponent} that are selected by name.
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @author rubensworks
 */
public interface IIngredientComponentCapabilityAttacher<T, M> {

    /**
     * @return The unique name of the capability to attach to.
     */
    public ResourceLocation getTargetName();

    /**
     * @return The capability to attach for.
     */
    public IngredientComponentCapability<?, ?> getCapability();

    /**
     * Create a capability provider for the given ingredient component instance.
     * @param ingredientComponent An ingredient component.
     * @return A capability provider instance.
     */
    public ICapabilityProvider<?, ?, ?> createCapabilityProvider(IngredientComponent<T, M> ingredientComponent);

}
