package org.cyclops.cyclopscore.modcompat.capabilities;

import com.google.common.collect.*;
import net.minecraft.entity.Entity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.capabilities.ICapabilityProvider;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Registry for capabilities created by this mod.
 * @author rubensworks
 */
public class CapabilityConstructorRegistry {
    private final Map<Class<? extends TileEntity>, List<ICapabilityConstructor<?, ? extends TileEntity, ? extends TileEntity>>>
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

    protected final ModBase mod;
    protected boolean baked = false;

    public CapabilityConstructorRegistry(ModBase mod) {
        this.mod = mod;
        MinecraftForge.EVENT_BUS.register(this);
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
    public <T extends TileEntity> void registerTile(Class<T> clazz, ICapabilityConstructor<?, T, T> constructor) {
        checkNotBaked();
        List<ICapabilityConstructor<?, ? extends TileEntity, ? extends TileEntity>> constructors = capabilityConstructorsTile.get(clazz);
        if (constructors == null) {
            constructors = Lists.newArrayList();
            capabilityConstructorsTile.put(clazz, constructors);
        }
        constructors.add(constructor);
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
    }

    @SuppressWarnings("unchecked")
    protected <K, KE, H, HE> ICapabilityProvider createProvider(KE hostType, HE host, ICapabilityConstructor<?, K, H> capabilityConstructor) {
        return capabilityConstructor.createProvider((K) hostType, (H) host);
    }

    protected <T> void onLoad(Map<Class<? extends T>, List<ICapabilityConstructor<?, ? extends T, ? extends T>>> allConstructors,
                              Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?>>> allInheritableConstructors,
                              T object, AttachCapabilitiesEvent<?> event, Class<? extends T> baseClass) {
        onLoad(allConstructors, allInheritableConstructors, object, object, event, baseClass);
    }

    protected <K, V> void onLoad(Map<Class<? extends K>, List<ICapabilityConstructor<?, ? extends K, ? extends V>>> allConstructors,
                                 Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?>>> allInheritableConstructors,
                                 K keyObject, V valueObject, AttachCapabilitiesEvent<?> event, Class<? extends K> baseClass) {
        boolean initialized = baked || Helpers.isMinecraftInitialized();
        if (!baked && Helpers.isMinecraftInitialized()) {
            bake();
        }

        // Normal constructors
        Collection<ICapabilityConstructor<?, ? extends K, ? extends V>> constructors = allConstructors.get((Class<? extends K>) keyObject.getClass());
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
    }

    protected <K, V> void addLoadedCapabilityProvider(AttachCapabilitiesEvent<?> event, K keyObject, V valueObject, ICapabilityConstructor<?, ?, ?> constructor) {
        ICapabilityProvider provider = createProvider(keyObject, valueObject, constructor);
        if (provider != null) {
            ResourceLocation id = new ResourceLocation(getMod().getModId(), constructor.getCapability().getName());
            if (!event.getCapabilities().containsKey(id)) {
                event.addCapability(id, provider);
            } else {
                getMod().getLoggerHelper().log(Level.DEBUG, "Duplicate capability registration of " + id + " in " + keyObject);
            }
        }
    }

    @SubscribeEvent
    public void onTileLoad(AttachCapabilitiesEvent<TileEntity> event) {
        onLoad(capabilityConstructorsTile, capabilityConstructorsTileSuper, event.getObject(), event, TileEntity.class);
    }

    @SubscribeEvent
    public void onEntityLoad(AttachCapabilitiesEvent<Entity> event) {
        onLoad(capabilityConstructorsEntity, capabilityConstructorsEntitySuper, event.getObject(), event, Entity.class);
    }

    @SubscribeEvent
    public void onItemStackLoad(AttachCapabilitiesEvent<ItemStack> event) {
        onLoad(capabilityConstructorsItem, capabilityConstructorsItemSuper, event.getObject().getItem(), event.getObject(), event, Item.class);
    }

    protected <K, V> void removeNullCapabilities(Map<Class<? extends K>, List<ICapabilityConstructor<?, ? extends K, ? extends V>>> allConstructors,
                                                 Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?>>> allInheritableConstructors) {
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
    }

    /**
     * Bakes the registry so that it becomes immutable.
     */
    public void bake() {
        baked = true;

        // Remove capability constructors for capabilities that are not initialized.
        removeNullCapabilities(capabilityConstructorsTile, capabilityConstructorsTileSuper);
        removeNullCapabilities(capabilityConstructorsEntity, capabilityConstructorsEntitySuper);
        removeNullCapabilities(capabilityConstructorsItem, capabilityConstructorsItemSuper);

        // Bake all collections
        capabilityConstructorsTileSuper = ImmutableList.copyOf(capabilityConstructorsTileSuper);
        capabilityConstructorsEntitySuper = ImmutableList.copyOf(capabilityConstructorsEntitySuper);
        capabilityConstructorsItemSuper = ImmutableList.copyOf(capabilityConstructorsItemSuper);

    }
}
