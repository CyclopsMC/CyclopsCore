package org.cyclops.commoncapabilities.api.ingredient;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import cpw.mods.modlauncher.TransformingClassLoader;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModLoader;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.registries.NewRegistryEvent;
import net.neoforged.neoforge.registries.RegisterEvent;
import net.neoforged.neoforge.registries.RegistryBuilder;
import org.cyclops.commoncapabilities.api.ingredient.capability.AttachCapabilitiesEventIngredientComponent;
import org.cyclops.commoncapabilities.api.ingredient.capability.ICapabilityGetter;
import org.cyclops.commoncapabilities.api.ingredient.capability.IngredientComponentCapability;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorage;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageHandler;
import org.cyclops.commoncapabilities.api.ingredient.storage.IIngredientComponentStorageWrapperHandler;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

/**
 * A IngredientComponent is a type of component that can be used as ingredients inside recipes.
 *
 * @param <T> The instance type.
 * @param <M> The matching condition parameter, may be Void. Instances MUST properly implement the equals method.
 * @author rubensworks
 */
@EventBusSubscriber(bus = EventBusSubscriber.Bus.MOD)
public final class IngredientComponent<T, M> implements Comparable<IngredientComponent<?, ?>> {

    public static Registry<IngredientComponent<?, ?>> REGISTRY;

    @SubscribeEvent
    public static void onRegistriesCreate(NewRegistryEvent event) {
        REGISTRY = event.create(new RegistryBuilder<>(
                ResourceKey.createRegistryKey(ResourceLocation.fromNamespaceAndPath("commoncapabilities", "ingredientcomponents"))
        ));
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public static void onRegistriesFilled(RegisterEvent event) {
        if (event.getRegistry() == REGISTRY) {
            ITEMSTACK = (IngredientComponent<ItemStack, Integer>) REGISTRY.get(ResourceLocation.parse("minecraft:itemstack"));
            FLUIDSTACK = (IngredientComponent<FluidStack, Integer>) REGISTRY.get(ResourceLocation.parse("minecraft:fluidstack"));
            ENERGY = (IngredientComponent<Long, Boolean>) REGISTRY.get(ResourceLocation.parse("minecraft:energy"));
        }
    }

    public static IngredientComponent<ItemStack, Integer> ITEMSTACK = null;
    public static IngredientComponent<FluidStack, Integer> FLUIDSTACK = null;
    public static IngredientComponent<Long, Boolean> ENERGY = null;

    // This check is needed to make this code run in unit tests
    private static BlockCapability<IIngredientComponentStorageHandler, Direction> CAPABILITY_BLOCK_INGREDIENT_COMPONENT_STORAGE_HANDLER = IngredientComponent.class.getClassLoader() instanceof TransformingClassLoader ? BlockCapability.createSided(ResourceLocation.fromNamespaceAndPath("commoncapabilities", "ingredient_component_storage_handler"), IIngredientComponentStorageHandler.class) : null;
    private static EntityCapability<IIngredientComponentStorageHandler, Direction> CAPABILITY_ENTITY_INGREDIENT_COMPONENT_STORAGE_HANDLER = IngredientComponent.class.getClassLoader() instanceof TransformingClassLoader ? EntityCapability.createSided(ResourceLocation.fromNamespaceAndPath("commoncapabilities", "ingredient_component_storage_handler"), IIngredientComponentStorageHandler.class) : null;
    private static ItemCapability<IIngredientComponentStorageHandler, Void> CAPABILITY_ITEM_INGREDIENT_COMPONENT_STORAGE_HANDLER = IngredientComponent.class.getClassLoader() instanceof TransformingClassLoader ? ItemCapability.createVoid(ResourceLocation.fromNamespaceAndPath("commoncapabilities", "ingredient_component_storage_handler"), IIngredientComponentStorageHandler.class) : null;
    private static Map<Class<?>, BaseCapability<IIngredientComponentStorageHandler, ?>> CAPABILITY_INGREDIENT_COMPONENT_STORAGE_HANDLERS = Maps.newIdentityHashMap();
    static {
        CAPABILITY_INGREDIENT_COMPONENT_STORAGE_HANDLERS.put(Block.class, CAPABILITY_BLOCK_INGREDIENT_COMPONENT_STORAGE_HANDLER);
        CAPABILITY_INGREDIENT_COMPONENT_STORAGE_HANDLERS.put(Entity.class, CAPABILITY_ENTITY_INGREDIENT_COMPONENT_STORAGE_HANDLER);
        CAPABILITY_INGREDIENT_COMPONENT_STORAGE_HANDLERS.put(Item.class, CAPABILITY_ITEM_INGREDIENT_COMPONENT_STORAGE_HANDLER);
    }

    private static Map<BaseCapability<?, ?>, IngredientComponent<?, ?>> STORAGE_WRAPPER_CAPABILITIES_COMPONENTS = Maps.newIdentityHashMap();

    private final IIngredientMatcher<T, M> matcher;
    private final IIngredientSerializer<T, M> serializer;
    private final List<IngredientComponentCategoryType<T, M, ?>> categoryTypes;
    private final List<BaseCapability<?, ?>> storageWrapperCapabilities;
    private final Map<BaseCapability<?, ?>, IIngredientComponentStorageWrapperHandler<T, M, ?, ?>> storageWrapperHandler;
    private final IngredientComponentCategoryType<T, M, ?> primaryQuantifier;
    private final ResourceLocation name;
    private String translationKey;

    public IngredientComponent(ResourceLocation name, IIngredientMatcher<T, M> matcher,
                               IIngredientSerializer<T, M> serializer,
                               List<IngredientComponentCategoryType<T, M, ?>> categoryTypes) {
        this.name = name;
        this.matcher = matcher;
        this.serializer = serializer;
        this.categoryTypes = Lists.newArrayList(categoryTypes);
        this.storageWrapperCapabilities = Lists.newArrayList();
        this.storageWrapperHandler = Maps.newIdentityHashMap();

        // Validate if the combination of all match conditions equals the exact match condition.
        M matchCondition = this.matcher.getAnyMatchCondition();
        IngredientComponentCategoryType<T, M, ?> primaryQuantifier = null;
        for (IngredientComponentCategoryType<T, M, ?> categoryType : this.categoryTypes) {
            matchCondition = this.matcher.withCondition(matchCondition, categoryType.getMatchCondition());
            if (categoryType.isPrimaryQuantifier()) {
                if (primaryQuantifier != null) {
                    throw new IllegalArgumentException("Found more than one primary quantifier in category types.");
                }
                primaryQuantifier = categoryType;
            }
        }
        this.primaryQuantifier = primaryQuantifier;
        if (!Objects.equals(matchCondition, this.getMatcher().getExactMatchCondition())) {
            throw new IllegalArgumentException("The given category types when combined do not conform to the " +
                    "exact match conditions of the matcher.");
        }
    }

    public IngredientComponent(String name, IIngredientMatcher<T, M> matcher, IIngredientSerializer<T, M> serializer,
                               List<IngredientComponentCategoryType<T, M, ?>> categoryTypes) {
        this(ResourceLocation.parse(name), matcher, serializer, categoryTypes);
    }

    public ResourceLocation getName() {
        return name;
    }

    @Override
    public String toString() {
        return "[IngredientComponent " + this.name + " " + hashCode() + "]";
    }

    public void gatherCapabilities(RegisterCapabilitiesEvent registerCapabilitiesEvent) {
        AttachCapabilitiesEventIngredientComponent<T, M> event = new AttachCapabilitiesEventIngredientComponent<>(this);
        ModLoader.postEvent(event);
    }

    /**
     * Get the given capability.
     * @param capability The capability to get.
     * @param <TC> The capability type.
     * @return The lazy optional capability instance.
     */
    public <TC> Optional<TC> getCapability(IngredientComponentCapability<TC, Void> capability) {
        return Optional.ofNullable(capability.getCapability(this, null));
    }

    public IngredientComponent<T, M> setTranslationKey(String translationKey) {
        this.translationKey = translationKey;
        return this;
    }

    /**
     * @return The unlocalized name.
     */
    public String getTranslationKey() {
        return translationKey;
    }

    /**
     * @return A matcher instance for comparing instances of this component type.
     */
    public IIngredientMatcher<T, M> getMatcher() {
        return matcher;
    }

    /**
     * @return A (de)serializer instance for this component type.
     */
    public IIngredientSerializer<T, M> getSerializer() {
        return serializer;
    }

    /**
     * @return The category types of this component.
     */
    public List<IngredientComponentCategoryType<T, M, ?>> getCategoryTypes() {
        return categoryTypes;
    }

    /**
     * Wrap the given instance inside an equals, hashCode and compareTo-safe holder.
     * @param instance An instance.
     * @return The wrapped instance.
     */
    public IngredientInstanceWrapper<T, M> wrap(T instance) {
        return new IngredientInstanceWrapper<>(this, instance);
    }

    /**
     * @return The primary quantifier category type, can be null.
     */
    @Nullable
    public IngredientComponentCategoryType<T, M, ?> getPrimaryQuantifier() {
        return primaryQuantifier;
    }

    /**
     * Set the storage wrapper handler for this component.
     * @param capability The capability for the storage wrapper.
     * @param storageWrapperHandler The storage wrapper handler.
     * @param <S> The capability type.
     */
    public <S> void setStorageWrapperHandler(BaseCapability<?, ?> capability,
                                             IIngredientComponentStorageWrapperHandler<T, M, ? super S, ?> storageWrapperHandler) {
        Objects.requireNonNull(capability, "Registered a storage wrapper handler before capabilities are registered.");
        if (this.storageWrapperHandler.put(capability, storageWrapperHandler) == null) {
            this.storageWrapperCapabilities.add(capability);
            IngredientComponent<?, ?> previousValue = IngredientComponent.STORAGE_WRAPPER_CAPABILITIES_COMPONENTS.put(capability, this);
            if (previousValue != null) {
                throw new IllegalStateException(String.format(
                        "Tried registering a storage capability (%s) for %s that was already registered to %s",
                        capability.name(), this, previousValue));
            }
        }
    }

    /**
     * Get the storage wrapper handler for this component.
     * @param capability The capability to get the storage wrapper for.
     * @param <S> The external storage type.
     * @return The storage wrapper handler, can be null if none has been assigned.
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <S, C> IIngredientComponentStorageWrapperHandler<T, M, S, C> getStorageWrapperHandler(BaseCapability<S, ?> capability) {
        return (IIngredientComponentStorageWrapperHandler<T, M, S, C>) storageWrapperHandler.get(capability);
    }

    /**
     * @return All supported capabilities that have registered wrapper handlers
     */
    public Collection<BaseCapability<?, ?>> getStorageWrapperHandlerCapabilities() {
        return this.storageWrapperCapabilities;
    }

    /**
     * Get the ingredient component that was attached to the given storage capability.
     * @param capability A storage capability.
     * @return The attached ingredient component, or null.
     */
    @Nullable
    public static IngredientComponent<?, ?> getIngredientComponentForStorageCapability(BaseCapability<?, ?> capability) {
        return IngredientComponent.STORAGE_WRAPPER_CAPABILITIES_COMPONENTS.get(capability);
    }

    /**
     * Get the ingredient storage within the given capability provider.
     *
     * This will first check if the capability provider has a {@link IIngredientComponentStorageHandler} capability
     * and it will try to fetch the storage from there.
     * If this fails, then the storage wrapper handlers that are registered in this ingredient component
     * will be iterated and the first successful one will be returned.
     *
     * @param capabilityType capabilityType The type of capability, usually Block.class, Item.class, or Entity.class.
     * @param capabilityGetter A capability provider.
     * @param context The context to get the storage from.
     * @return An ingredient storage, or null if it does not exist.
     */
    @SuppressWarnings("unchecked")
    @Nullable
    public <O, C> IIngredientComponentStorage<T, M> getStorage(
            Class<O> capabilityType,
            ICapabilityGetter<C> capabilityGetter,
            C context
    ) {
        BaseCapability<IIngredientComponentStorageHandler, ?> capabilityIngredientComponentStorageHandler = CAPABILITY_INGREDIENT_COMPONENT_STORAGE_HANDLERS.get(capabilityType);
        if (capabilityIngredientComponentStorageHandler == null) {
            throw new IllegalStateException("No capability is know for type " + capabilityType.getName());
        }

        // Check IIngredientComponentStorageHandler capability
        IIngredientComponentStorageHandler storageHandler = capabilityGetter.getCapability((BaseCapability<IIngredientComponentStorageHandler, C>) capabilityIngredientComponentStorageHandler, context);
        if (storageHandler != null) {
            IIngredientComponentStorage<T, M> storage = storageHandler.getStorage(this);
            if (storage != null) {
                return storage;
            }
        }

        // Check registered wrapper handlers
        for (BaseCapability<?, ?> capability : getStorageWrapperHandlerCapabilities()) {
            if (capabilityGetter.canHandleCapabilityType(capability)) {
                IIngredientComponentStorageWrapperHandler<T, M, O, C> wrapperHandler = getStorageWrapperHandler((BaseCapability<O, ?>) capability);
                IIngredientComponentStorage<T, M> storage = wrapperHandler.getComponentStorage(capabilityGetter, context);
                if (storage != null) {
                    return storage;
                }
            }
        }

        // Otherwise, fail
        return null;
    }

    @Nullable
    public <O, C> IIngredientComponentStorage<T, M> getBlockStorage(
            Level level, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, C context
    ) {
        return getStorage(Block.class, ICapabilityGetter.forBlock(level, pos, state, blockEntity), context);
    }

    @Nullable
    public <O, C> IIngredientComponentStorage<T, M> getEntityStorage(
            Entity entity, C context
    ) {
        return getStorage(Entity.class, ICapabilityGetter.forEntity(entity), context);
    }

    @Nullable
    public <O, C> IIngredientComponentStorage<T, M> getItemStorage(
            ItemStack itemStack, C context
    ) {
        return getStorage(Item.class, ICapabilityGetter.forItem(itemStack), context);
    }

    @Override
    public int compareTo(IngredientComponent<?, ?> that) {
        return this.getName().compareTo(that.getName());
    }
}
