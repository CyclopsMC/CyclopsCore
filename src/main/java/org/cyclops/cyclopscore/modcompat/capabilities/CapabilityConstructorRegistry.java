package org.cyclops.cyclopscore.modcompat.capabilities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.ItemCapability;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.NeoForge;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * Registry for capabilities created by this mod.
 * @author rubensworks
 */
public class CapabilityConstructorRegistry {
    private final Map<BlockEntityType<? extends BlockEntity>, List<ICapabilityConstructor<?, ?, ? extends BlockCapability<?, ?>, ? extends BlockEntityType<? extends BlockEntity>>>>
            capabilityConstructorsBlockEntity = Maps.newIdentityHashMap();
    private final Map<EntityType<? extends Entity>, List<ICapabilityConstructor<?, ?, ? extends EntityCapability<?, ?>, ? extends EntityType<? extends Entity>>>>
            capabilityConstructorsEntity = Maps.newIdentityHashMap();
    private final Map<ItemLike, List<ICapabilityConstructor<?, ?, ? extends ItemCapability<?, ?>, ? extends ItemLike>>>
            capabilityConstructorsItem = Maps.newIdentityHashMap();
    private Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?, ?>>>
            capabilityConstructorsBlockEntitySuper = Sets.newHashSet();
    private Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?, ?>>>
            capabilityConstructorsEntitySuper = Sets.newHashSet();
    private Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?, ?>>>
            capabilityConstructorsItemSuper = Sets.newHashSet();

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
     * @param blockEntityType The tile class.
     * @param constructor The capability constructor.
     * @param <T> The tile type.
     */
    public <T extends BlockEntity> void registerBlockEntity(BlockEntityType<T> blockEntityType, ICapabilityConstructor<?, ?, ? extends BlockCapability<?, ?>, BlockEntityType<T>> constructor) {
        checkNotBaked();
        List<ICapabilityConstructor<?, ?, ? extends BlockCapability<?, ?>, ? extends BlockEntityType<?>>> constructors = capabilityConstructorsBlockEntity.get(blockEntityType);
        if (constructors == null) {
            constructors = Lists.newArrayList();
            capabilityConstructorsBlockEntity.put(blockEntityType, constructors);
        }
        constructors.add(constructor);

        if (!registeredTileEventListener) {
            registeredTileEventListener = true;
            NeoForge.EVENT_BUS.register(new TileEventListener());
        }
    }

    /**
     * Register an entity capability constructor.
     * @param entityType The entity class.
     * @param constructor The capability constructor.
     * @param <T> The entity type.
     */
    public <T extends Entity> void registerEntity(EntityType<T> entityType, ICapabilityConstructor<?, ?, ? extends EntityCapability<?, ?>, ? extends EntityType<T>> constructor) {
        checkNotBaked();
        List<ICapabilityConstructor<?, ?, ? extends EntityCapability<?, ?>, ? extends EntityType<?>>> constructors = capabilityConstructorsEntity.get(entityType);
        if (constructors == null) {
            constructors = Lists.newArrayList();
            capabilityConstructorsEntity.put(entityType, constructors);
        }
        constructors.add(constructor);

        if (!registeredEntityEventListener) {
            registeredEntityEventListener = true;
            NeoForge.EVENT_BUS.register(new EntityEventListener());
        }
    }

    /**
     * Register an item capability constructor.
     * @param item The item class.
     * @param constructor The capability constructor.
     */
    public void registerItem(ItemLike item, ICapabilityConstructor<?, ?, ? extends ItemCapability<?, ?>, ? extends ItemLike> constructor) {
        checkNotBaked();
        List<ICapabilityConstructor<?, ?, ? extends ItemCapability<?, ?>, ? extends ItemLike>> constructors = capabilityConstructorsItem.get(item);
        if (constructors == null) {
            constructors = Lists.newArrayList();
            capabilityConstructorsItem.put(item, constructors);
        }
        constructors.add(constructor);

        if (!registeredItemStackEventListener) {
            registeredItemStackEventListener = true;
            NeoForge.EVENT_BUS.register(new ItemStackEventListener());
        }
    }

    /**
     * Register a tile capability constructor with subtype checking.
     * Only call this when absolutely required, this will is less efficient than its non-inheritable counterpart.
     * @param clazz The tile class, all subtypes will be checked.
     * @param constructor The capability constructor.
     */
    public void registerInheritableTile(Class<?> clazz, ICapabilityConstructor<?, ?, ?, ?> constructor) {
        checkNotBaked();
        capabilityConstructorsBlockEntitySuper.add(
                Pair.<Class<?>, ICapabilityConstructor<?, ?, ?, ?>>of(clazz, constructor));

        if (!registeredTileEventListener) {
            registeredTileEventListener = true;
            NeoForge.EVENT_BUS.register(new TileEventListener());
        }
    }

    /**
     * Register an entity capability constructor with subtype checking.
     * Only call this when absolutely required, this will is less efficient than its non-inheritable counterpart.
     * @param clazz The tile class, all subtypes will be checked.
     * @param constructor The capability constructor.
     */
    public void registerInheritableEntity(Class<?> clazz, ICapabilityConstructor<?, ?, ?, ?> constructor) {
        checkNotBaked();
        capabilityConstructorsEntitySuper.add(
                Pair.<Class<?>, ICapabilityConstructor<?, ?, ?, ?>>of(clazz, constructor));

        if (!registeredEntityEventListener) {
            registeredEntityEventListener = true;
            NeoForge.EVENT_BUS.register(new EntityEventListener());
        }
    }

    /**
     * Register an item capability constructor with subtype checking.
     * Only call this when absolutely required, this will is less efficient than its non-inheritable counterpart.
     * @param clazz The tile class, all subtypes will be checked.
     * @param constructor The capability constructor.
     */
    public void registerInheritableItem(Class<?> clazz, ICapabilityConstructor<?, ?, ?, ?> constructor) {
        checkNotBaked();
        capabilityConstructorsItemSuper.add(
                Pair.<Class<?>, ICapabilityConstructor<?, ?, ?, ?>>of(clazz, constructor));

        if (!registeredItemStackEventListener) {
            registeredItemStackEventListener = true;
            NeoForge.EVENT_BUS.register(new ItemStackEventListener());
        }
    }

    @SuppressWarnings("unchecked")
    protected <CK> ICapabilityProvider<?, ?, ?> createProvider(CK capabilityKey, ICapabilityConstructor<?, ?, ?, CK> capabilityConstructor) {
        return capabilityConstructor.createProvider(capabilityKey);
    }

    protected <CK> void onLoad(Map<CK, List<ICapabilityConstructor<?, ?, ?, CK>>> allConstructors,
                                 //Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?, CK>>> allInheritableConstructors,
                                 RegisterCapabilitiesEvent event) {
        boolean initialized = baked || MinecraftHelpers.isMinecraftInitialized();
        synchronized (this) {
            if (!baked && MinecraftHelpers.isMinecraftInitialized()) {
                bake();
            }
        }

        // Normal constructors
        for (Map.Entry<CK, List<ICapabilityConstructor<?, ?, ?, CK>>> entry : allConstructors.entrySet()) {
            if (initialized) {
                for (ICapabilityConstructor<?, ?, ?, CK> constructor : entry.getValue()) {
                    addLoadedCapabilityProvider(event, entry.getKey(), constructor);
                }
            }
        }

        // Inheritable constructors
        // TODO: restore
//        for (Pair<Class<?>, ICapabilityConstructor<?, ?, ?>> constructorEntry : allInheritableConstructors) {
//            if ((initialized)
//                    && (keyObject == baseClass || constructorEntry.getLeft() == keyObject || constructorEntry.getLeft().isInstance(keyObject))) {
//                addLoadedCapabilityProvider(event, keyObject, valueObject, constructorEntry.getRight());
//            }
//        }
    }

    protected <CK> void addLoadedCapabilityProvider(RegisterCapabilitiesEvent event, CK capabilityKey, ICapabilityConstructor<?, ?, ?, CK> constructor) {
        ICapabilityProvider provider = createProvider(capabilityKey, constructor);
        if (provider != null) {
            BaseCapability capability = constructor.getCapability();
            if (capability instanceof BlockCapability blockCapability) {
                event.registerBlockEntity(blockCapability, (BlockEntityType<BlockEntity>) capabilityKey, provider);
            } else if (capability instanceof EntityCapability entityCapability) {
                event.registerEntity(entityCapability, (EntityType<? extends Entity>) capabilityKey, provider);
            } else if (capability instanceof ItemCapability itemCapability) {
                event.registerItem(itemCapability, provider, (ItemLike) capabilityKey);
            } else {
                throw new IllegalStateException("Capability of type " + capability.getClass() + " is not supported");
            }
        }
    }

    /**
     * Bakes the registry so that it becomes immutable.
     */
    public void bake() {
        baked = true;

        // Bake all collections
        capabilityConstructorsBlockEntitySuper = ImmutableList.copyOf(capabilityConstructorsBlockEntitySuper);
        capabilityConstructorsEntitySuper = ImmutableList.copyOf(capabilityConstructorsEntitySuper);
        capabilityConstructorsItemSuper = ImmutableList.copyOf(capabilityConstructorsItemSuper);

    }

    public class TileEventListener {
        @SubscribeEvent
        public void onTileLoad(RegisterCapabilitiesEvent event) {
            onLoad((Map) capabilityConstructorsBlockEntity, event);
        }
    }

    public class EntityEventListener {
        @SubscribeEvent
        public void onEntityLoad(RegisterCapabilitiesEvent event) {
            onLoad((Map) capabilityConstructorsEntity, event);
        }
    }

    public class ItemStackEventListener {
        @SubscribeEvent
        public void onItemStackLoad(RegisterCapabilitiesEvent event) {
            onLoad((Map) capabilityConstructorsItem, event);
        }
    }
}
