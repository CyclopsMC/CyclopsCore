package org.cyclops.cyclopscore.modcompat.capabilities;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ModBase;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Registry for capabilities created by this mod.
 * @author rubensworks
 */
public class CapabilityConstructorRegistry {
    private final Map<Class<? extends BlockEntity>, List<ICapabilityConstructor<?, ? extends BlockEntity, ? extends BlockEntity>>>
            capabilityConstructorsTile = Maps.newIdentityHashMap();
    private final Map<Class<? extends Entity>, List<ICapabilityConstructor<?, ? extends Entity, ? extends Entity>>>
            capabilityConstructorsEntity = Maps.newIdentityHashMap();
    private final Map<Class<? extends Item>, List<ICapabilityConstructor<?, ? extends Item, ? extends ItemStack>>>
            capabilityConstructorsItem = Maps.newIdentityHashMap();
    private Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?>>>
            capabilityConstructorsTileSuper = Sets.newHashSet();
    private Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?>>>
            capabilityConstructorsEntitySuper = Sets.newHashSet();
    private Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?>>>
            capabilityConstructorsItemSuper = Sets.newHashSet();
    private final Map<Item, List<ICapabilityConstructor<?, ? extends Item, ? extends ItemStack>>>
            capabilityConstructorsItemInstance = Maps.newIdentityHashMap();
    private final List<Pair<Supplier<Item>, ICapabilityConstructor<?, ? extends Item, ? extends ItemStack>>>
            capabilityConstructorsItemInstancePending = Lists.newArrayList();

    protected final ModBase mod;
    protected boolean baked = false;
    protected boolean registeredTileEventListener = false;
    protected boolean registeredEntityEventListener = false;
    protected boolean registeredItemStackEventListener = false;

    public CapabilityConstructorRegistry(ModBase mod) {
        this.mod = mod;
    }

    protected ModBase getMod() {
        return mod;
    }

    protected void checkNotBaked() {
        if (baked) {
            throw new IllegalStateException("Please register capabilities before pre-init.");
        }
    }

    /**
     * Register a tile capability constructor.
     * @param clazz The tile class.
     * @param constructor The capability constructor.
     * @param <T> The tile type.
     */
    public <T extends BlockEntity> void registerTile(Class<T> clazz, ICapabilityConstructor<?, T, T> constructor) {
        checkNotBaked();
        List<ICapabilityConstructor<?, ? extends BlockEntity, ? extends BlockEntity>> constructors = capabilityConstructorsTile.get(clazz);
        if (constructors == null) {
            constructors = Lists.newArrayList();
            capabilityConstructorsTile.put(clazz, constructors);
        }
        constructors.add(constructor);

        if (!registeredTileEventListener) {
            registeredTileEventListener = true;
            MinecraftForge.EVENT_BUS.register(new TileEventListener());
        }
    }

    /**
     * Register an entity capability constructor.
     * @param clazz The entity class.
     * @param constructor The capability constructor.
     * @param <T> The entity type.
     */
    public <T extends Entity> void registerEntity(Class<T> clazz, ICapabilityConstructor<?, T, T> constructor) {
        checkNotBaked();
        List<ICapabilityConstructor<?, ? extends Entity, ? extends Entity>> constructors = capabilityConstructorsEntity.get(clazz);
        if (constructors == null) {
            constructors = Lists.newArrayList();
            capabilityConstructorsEntity.put(clazz, constructors);
        }
        constructors.add(constructor);

        if (!registeredEntityEventListener) {
            registeredEntityEventListener = true;
            MinecraftForge.EVENT_BUS.register(new EntityEventListener());
        }
    }

    /**
     * Register an item capability constructor.
     * @param clazz The item class.
     * @param constructor The capability constructor.
     * @param <T> The item type.
     */
    public <T extends Item> void registerItem(Class<T> clazz, ICapabilityConstructor<?, T, ItemStack> constructor) {
        checkNotBaked();
        List<ICapabilityConstructor<?, ? extends Item, ? extends ItemStack>> constructors = capabilityConstructorsItem.get(clazz);
        if (constructors == null) {
            constructors = Lists.newArrayList();
            capabilityConstructorsItem.put(clazz, constructors);
        }
        constructors.add(constructor);

        if (!registeredItemStackEventListener) {
            registeredItemStackEventListener = true;
            MinecraftForge.EVENT_BUS.register(new ItemStackEventListener());
        }
    }

    /**
     * Register a tile capability constructor with subtype checking.
     * Only call this when absolutely required, this will is less efficient than its non-inheritable counterpart.
     * @param clazz The tile class, all subtypes will be checked.
     * @param constructor The capability constructor.
     * @param <K> The capability type.
     * @param <V> The tile type.
     */
    public <K, V> void registerInheritableTile(Class<K> clazz, ICapabilityConstructor<?, V, V> constructor) {
        checkNotBaked();
        capabilityConstructorsTileSuper.add(
                Pair.<Class<?>, ICapabilityConstructor<?, ?, ?>>of(clazz, constructor));

        if (!registeredTileEventListener) {
            registeredTileEventListener = true;
            MinecraftForge.EVENT_BUS.register(new TileEventListener());
        }
    }

    /**
     * Register an entity capability constructor with subtype checking.
     * Only call this when absolutely required, this will is less efficient than its non-inheritable counterpart.
     * @param clazz The tile class, all subtypes will be checked.
     * @param constructor The capability constructor.
     * @param <K> The capability type.
     * @param <V> The entity type.
     */
    public <K, V> void registerInheritableEntity(Class<K> clazz, ICapabilityConstructor<?, V, V> constructor) {
        checkNotBaked();
        capabilityConstructorsEntitySuper.add(
                Pair.<Class<?>, ICapabilityConstructor<?, ?, ?>>of(clazz, constructor));

        if (!registeredEntityEventListener) {
            registeredEntityEventListener = true;
            MinecraftForge.EVENT_BUS.register(new EntityEventListener());
        }
    }

    /**
     * Register an item capability constructor with subtype checking.
     * Only call this when absolutely required, this will is less efficient than its non-inheritable counterpart.
     * @param clazz The tile class, all subtypes will be checked.
     * @param constructor The capability constructor.
     * @param <T> The tile type.
     */
    public <T> void registerInheritableItem(Class<T> clazz, ICapabilityConstructor<?, ?, ? extends ItemStack> constructor) {
        checkNotBaked();
        capabilityConstructorsItemSuper.add(
                Pair.<Class<?>, ICapabilityConstructor<?, ?, ?>>of(clazz, constructor));

        if (!registeredItemStackEventListener) {
            registeredItemStackEventListener = true;
            MinecraftForge.EVENT_BUS.register(new ItemStackEventListener());
        }
    }

    /**
     * Register an item capability constructor for the given item instance.
     * @param itemSupplier An item supplier.
     * @param constructor The capability constructor.
     * @param <T> The tile type.
     */
    public <T extends Item> void registerItem(Supplier<T> itemSupplier, ICapabilityConstructor<?, T, ItemStack> constructor) {
        checkNotBaked();
        capabilityConstructorsItemInstancePending.add((Pair) Pair.of(itemSupplier, constructor));

        if (!registeredItemStackEventListener) {
            registeredItemStackEventListener = true;
            MinecraftForge.EVENT_BUS.register(new ItemStackEventListener());
        }
    }

    protected void registerItemsEffective() {
        for (Pair<Supplier<Item>, ICapabilityConstructor<?, ? extends Item, ? extends ItemStack>> entry : capabilityConstructorsItemInstancePending) {
            registerItemEffective(entry.getLeft(), (ICapabilityConstructor) entry.getRight());
        }
        capabilityConstructorsItemInstancePending.clear();
    }

    protected <T extends Item> void registerItemEffective(Supplier<T> itemSupplier, ICapabilityConstructor<?, T, ItemStack> constructor) {
        T item = Objects.requireNonNull(itemSupplier.get(), "Tried to register an item capability for a null item with constructor for " + constructor.getCapability().getName());
        List<ICapabilityConstructor<?, ? extends Item, ? extends ItemStack>> constructors = capabilityConstructorsItemInstance.get(item);
        if (constructors == null) {
            constructors = Lists.newArrayList();
            capabilityConstructorsItemInstance.put(item, constructors);
        }
        constructors.add(constructor);
    }

    @SuppressWarnings("unchecked")
    protected <K, KE, H, HE> ICapabilityProvider createProvider(KE hostType, HE host, ICapabilityConstructor<?, K, H> capabilityConstructor) {
        return capabilityConstructor.createProvider((K) hostType, (H) host);
    }

    protected <T> void onLoad(Map<Class<? extends T>, List<ICapabilityConstructor<?, ? extends T, ? extends T>>> allConstructors,
                              Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?>>> allInheritableConstructors,
                              @Nullable Map<? extends T, List<ICapabilityConstructor<?, ? extends T, ? extends T>>> allInstanceConstructors,
                              T object, AttachCapabilitiesEvent<?> event, Class<? extends T> baseClass) {
        onLoad(allConstructors, allInheritableConstructors, allInstanceConstructors, object, object, event, baseClass);
    }

    protected <K, V> void onLoad(Map<Class<? extends K>, List<ICapabilityConstructor<?, ? extends K, ? extends V>>> allConstructors,
                                 Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?>>> allInheritableConstructors,
                                 @Nullable Map<? extends K, List<ICapabilityConstructor<?, ? extends K, ? extends V>>> allInstanceConstructors,
                                 K keyObject, V valueObject, AttachCapabilitiesEvent<?> event, Class<? extends K> baseClass) {
        boolean initialized = baked || MinecraftHelpers.isMinecraftInitialized();
        if (!baked && MinecraftHelpers.isMinecraftInitialized()) {
            bake();
        }

        // Normal constructors
        Collection<ICapabilityConstructor<?, ? extends K, ? extends V>> constructors = allConstructors.get(keyObject.getClass());
        if (constructors != null) {
            for (ICapabilityConstructor<?, ? extends K, ? extends V> constructor : constructors) {
                if (initialized || constructor.getCapability() != null) {
                    addLoadedCapabilityProvider(event, keyObject, valueObject, constructor);
                }
            }
        }

        // Inheritable constructors
        for (Pair<Class<?>, ICapabilityConstructor<?, ?, ?>> constructorEntry : allInheritableConstructors) {
            if ((initialized || constructorEntry.getRight().getCapability() != null)
                    && (keyObject == baseClass || constructorEntry.getLeft() == keyObject || constructorEntry.getLeft().isInstance(keyObject))) {
                addLoadedCapabilityProvider(event, keyObject, valueObject, constructorEntry.getRight());
            }
        }

        // Instance constructors
        if (allInstanceConstructors != null) {
            Collection<ICapabilityConstructor<?, ? extends K, ? extends V>> instanceConstructors = allInstanceConstructors.get(keyObject);
            if (instanceConstructors != null) {
                for (ICapabilityConstructor<?, ? extends K, ? extends V> constructor : instanceConstructors) {
                    if (initialized || constructor.getCapability() != null) {
                        addLoadedCapabilityProvider(event, keyObject, valueObject, constructor);
                    }
                }
            }
        }
    }

    protected <K, V> void addLoadedCapabilityProvider(AttachCapabilitiesEvent<?> event, K keyObject, V valueObject, ICapabilityConstructor<?, ?, ?> constructor) {
        ICapabilityProvider provider = createProvider(keyObject, valueObject, constructor);
        if (provider != null) {
            ResourceLocation id = new ResourceLocation(getMod().getModId(), constructor.getCapability().getName().toLowerCase(Locale.ENGLISH));
            if (!event.getCapabilities().containsKey(id)) {
                event.addCapability(id, provider);
            } else {
                getMod().getLoggerHelper().log(Level.DEBUG, "Duplicate capability registration of " + id + " in " + keyObject);
            }
        }
    }

    protected <K, V> void removeNullCapabilities(Map<Class<? extends K>, List<ICapabilityConstructor<?, ? extends K, ? extends V>>> allConstructors,
                                                 Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?>>> allInheritableConstructors,
                                                 @Nullable Map<? extends K, List<ICapabilityConstructor<?, ? extends K, ? extends V>>> allInstanceConstructors) {
        // Normal constructors
        Multimap<Class<? extends K>, ICapabilityConstructor<?, ? extends K, ? extends V>> toRemoveMap = HashMultimap.create();
        for (Class<? extends K> key : allConstructors.keySet()) {
            Collection<ICapabilityConstructor<?, ? extends K, ? extends V>> constructors = allConstructors.get(key);
            for (ICapabilityConstructor<?, ? extends K, ? extends V> constructor : constructors) {
                if (constructor.getCapability() == null) {
                    toRemoveMap.put(key, constructor);
                }
            }
        }
        for (Map.Entry<Class<? extends K>, ICapabilityConstructor<?, ? extends K, ? extends V>> entry : toRemoveMap.entries()) {
            List<ICapabilityConstructor<?, ? extends K, ? extends V>> constructors = allConstructors.get(entry.getKey());
            constructors.remove(entry.getValue());
        }

        // Inheritable constructors
        List<Pair<Class<?>, ICapabilityConstructor<?, ?, ?>>> toRemoveInheritableList = Lists.newArrayList();
        for (Pair<Class<?>, ICapabilityConstructor<?, ?, ?>> constructorEntry : allInheritableConstructors) {
            if (constructorEntry.getRight().getCapability() == null) {
                toRemoveInheritableList.add(constructorEntry);
            }
        }
        for (Pair<Class<?>, ICapabilityConstructor<?, ?, ?>> toRemove : toRemoveInheritableList) {
            allInheritableConstructors.remove(toRemove);
        }

        // Instance constructors
        if (allInstanceConstructors != null) {
            Multimap<K, ICapabilityConstructor<?, ? extends K, ? extends V>> toRemoveMapInstance = HashMultimap.create();
            for (K key : allInstanceConstructors.keySet()) {
                Collection<ICapabilityConstructor<?, ? extends K, ? extends V>> constructors = allInstanceConstructors.get(key);
                for (ICapabilityConstructor<?, ? extends K, ? extends V> constructor : constructors) {
                    if (constructor.getCapability() == null) {
                        toRemoveMapInstance.put(key, constructor);
                    }
                }
            }
            for (Map.Entry<K, ICapabilityConstructor<?, ? extends K, ? extends V>> entry : toRemoveMapInstance.entries()) {
                List<ICapabilityConstructor<?, ? extends K, ? extends V>> constructors = allInstanceConstructors.get(entry.getKey());
                constructors.remove(entry.getValue());
            }
        }
    }

    /**
     * Bakes the registry so that it becomes immutable.
     */
    public void bake() {
        baked = true;

        this.registerItemsEffective();

        // Remove capability constructors for capabilities that are not initialized.
        removeNullCapabilities(capabilityConstructorsTile, capabilityConstructorsTileSuper, null);
        removeNullCapabilities(capabilityConstructorsEntity, capabilityConstructorsEntitySuper, null);
        removeNullCapabilities(capabilityConstructorsItem, capabilityConstructorsItemSuper, capabilityConstructorsItemInstance);

        // Bake all collections
        capabilityConstructorsTileSuper = ImmutableList.copyOf(capabilityConstructorsTileSuper);
        capabilityConstructorsEntitySuper = ImmutableList.copyOf(capabilityConstructorsEntitySuper);
        capabilityConstructorsItemSuper = ImmutableList.copyOf(capabilityConstructorsItemSuper);

    }

    public class TileEventListener {
        @SubscribeEvent
        public void onTileLoad(AttachCapabilitiesEvent<BlockEntity> event) {
            onLoad(capabilityConstructorsTile, capabilityConstructorsTileSuper, null, event.getObject(), event, BlockEntity.class);
        }
    }

    public class EntityEventListener {
        @SubscribeEvent
        public void onEntityLoad(AttachCapabilitiesEvent<Entity> event) {
            onLoad(capabilityConstructorsEntity, capabilityConstructorsEntitySuper, null, event.getObject(), event, Entity.class);
        }
    }

    public class ItemStackEventListener {
        @SubscribeEvent
        public void onItemStackLoad(AttachCapabilitiesEvent<ItemStack> event) {
            if (!event.getObject().isEmpty()) {
                onLoad(capabilityConstructorsItem, capabilityConstructorsItemSuper, capabilityConstructorsItemInstance, event.getObject().getItem(), event.getObject(), event, Item.class);
            }
        }
    }
}
