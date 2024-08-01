package org.cyclops.commoncapabilities.api.ingredient.capability;

import net.minecraft.resources.ResourceLocation;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.CapabilityRegistry;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import org.cyclops.commoncapabilities.api.ingredient.IngredientComponent;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Nullable;

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

/**
 * @author rubensworks
 */
public class IngredientComponentCapability<T, C> extends BaseCapability<T, C> {
    public static <T, C> IngredientComponentCapability<T, C> create(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        return (IngredientComponentCapability<T, C>) registry.create(name, typeClass, contextClass);
    }

    public static <T> IngredientComponentCapability<T, Void> createVoid(ResourceLocation name, Class<T> typeClass) {
        return create(name, typeClass, void.class);
    }

    public static synchronized List<IngredientComponentCapability<?, ?>> getAll() {
        return registry.getAll();
    }

    // INTERNAL

    // Requires explicitly-typed constructor due to ECJ inference failure.
    private static final CapabilityRegistry<IngredientComponentCapability<?, ?>> registry = new CapabilityRegistry<IngredientComponentCapability<?, ?>>(IngredientComponentCapability::new);

    private IngredientComponentCapability(ResourceLocation name, Class<T> typeClass, Class<C> contextClass) {
        super(name, typeClass, contextClass);
    }

    final Map<IngredientComponent<?, ?>, List<ICapabilityProvider<IngredientComponent<?, ?>, C, T>>> providers = new IdentityHashMap<>();

    @ApiStatus.Internal
    @Nullable
    public T getCapability(IngredientComponent<?, ?> ingredientComponent, C context) {
        for (var provider : providers.getOrDefault(ingredientComponent, List.of())) {
            var ret = provider.getCapability(ingredientComponent, context);
            if (ret != null)
                return ret;
        }
        return null;
    }
}
