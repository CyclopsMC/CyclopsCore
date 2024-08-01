package org.cyclops.commoncapabilities.api.ingredient.capability;

import net.neoforged.bus.api.Event;
import net.neoforged.fml.event.IModBusEvent;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Event for when an {@link IngredientComponent} is being constructed that is emitted on the mod event bus.
 * @param <T> The instance type.
 * @param <M> The matching condition parameter.
 * @author rubensworks
 */
public class AttachCapabilitiesEventIngredientComponent<T, M> extends Event implements IModBusEvent {

    private final IngredientComponent<T, M> ingredientComponent;

    public AttachCapabilitiesEventIngredientComponent(IngredientComponent<T, M> ingredientComponent) {
        this.ingredientComponent = ingredientComponent;
    }

    public IngredientComponent<T, M> getIngredientComponent() {
        return this.ingredientComponent;
    }

    public <T, C> void register(
            IngredientComponentCapability<T, C> capability,
            IngredientComponent<?, ?> ingredientComponent,
            ICapabilityProvider<IngredientComponent<?, ?>, C, T> provider
    ) {
        Objects.requireNonNull(provider);
        capability.providers.computeIfAbsent(Objects.requireNonNull(ingredientComponent), i -> new ArrayList<>()).add(provider);
    }

    public boolean isRegistered(IngredientComponentCapability<?, ?> capability, IngredientComponent<?, ?> ingredientComponent) {
        Objects.requireNonNull(ingredientComponent);
        return capability.providers.containsKey(ingredientComponent);
    }
}
