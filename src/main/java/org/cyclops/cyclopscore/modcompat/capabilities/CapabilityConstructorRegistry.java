package org.cyclops.cyclopscore.modcompat.capabilities;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
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
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

/**
 * Registry for capabilities created by this mod.
 * @author rubensworks
 */
public class CapabilityConstructorRegistry {
    private final List<Pair<Supplier<BlockEntityType<? extends BlockEntity>>, ICapabilityConstructor<?, ?, ?, ? extends BlockEntityType<? extends BlockEntity>>>>
            capabilityConstructorsBlockEntity = Lists.newArrayList();
    private final List<Pair<Supplier<EntityType<? extends Entity>>, ICapabilityConstructor<?, ?, ?, ? extends EntityType<? extends Entity>>>>
            capabilityConstructorsEntity = Lists.newArrayList();
    private final List<Pair<Supplier<ItemLike>, ICapabilityConstructor<?, ?, ?, ? extends ItemLike>>>
            capabilityConstructorsItem = Lists.newArrayList();
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
    public <T extends BlockEntity> void registerBlockEntity(Supplier<BlockEntityType<T>> blockEntityType, ICapabilityConstructor<?, ?, ?, BlockEntityType<T>> constructor) {
        checkNotBaked();
        capabilityConstructorsBlockEntity.add(Pair.of((Supplier) blockEntityType, constructor));

        if (!registeredTileEventListener) {
            registeredTileEventListener = true;
            CyclopsCore._instance.getModEventBus().register(new BlockEntityEventListener());
        }
    }

    /**
     * Register an entity capability constructor.
     * @param entityType The entity class.
     * @param constructor The capability constructor.
     * @param <T> The entity type.
     */
    public <T extends Entity> void registerEntity(Supplier<EntityType<T>> entityType, ICapabilityConstructor<?, ?, ?, ? extends EntityType<T>> constructor) {
        checkNotBaked();
        capabilityConstructorsEntity.add(Pair.of((Supplier) entityType, constructor));

        if (!registeredEntityEventListener) {
            registeredEntityEventListener = true;
            CyclopsCore._instance.getModEventBus().register(new EntityEventListener());
        }
    }

    /**
     * Register an item capability constructor.
     * @param item The item class.
     * @param constructor The capability constructor.
     */
    public void registerItem(Supplier<ItemLike> item, ICapabilityConstructor<?, ?, ?, ? extends ItemLike> constructor) {
        checkNotBaked();
        capabilityConstructorsItem.add(Pair.of((Supplier) item, constructor));

        if (!registeredItemStackEventListener) {
            registeredItemStackEventListener = true;
            CyclopsCore._instance.getModEventBus().register(new ItemStackEventListener());
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
            CyclopsCore._instance.getModEventBus().register(new BlockEntityEventListener());
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
            CyclopsCore._instance.getModEventBus().register(new EntityEventListener());
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
            CyclopsCore._instance.getModEventBus().register(new ItemStackEventListener());
        }
    }

    @SuppressWarnings("unchecked")
    protected <CK> ICapabilityProvider<?, ?, ?> createProvider(CK capabilityKey, ICapabilityConstructor<?, ?, ?, CK> capabilityConstructor) {
        return capabilityConstructor.createProvider(capabilityKey);
    }

    protected <CK> void onLoad(List<Pair<Supplier<CK>, ICapabilityConstructor<?, ?, ?, CK>>> allConstructors,
                                  //Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?, CK>>> allInheritableConstructors,
                                  RegisterCapabilitiesEvent event) {
        synchronized (this) {
            if (!baked) {
                bake();
            }
        }

        // Normal constructors
        for (Pair<Supplier<CK>, ICapabilityConstructor<?, ?, ?, CK>> entry : allConstructors) {
            addLoadedCapabilityProvider(event, entry.getKey().get(), entry.getValue());
        }
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

    public class BlockEntityEventListener {
        @SubscribeEvent
        public void onBlockEntityLoad(RegisterCapabilitiesEvent event) {
            onLoad((List) capabilityConstructorsBlockEntity, event);
        }
    }

    public class EntityEventListener {
        @SubscribeEvent
        public void onEntityLoad(RegisterCapabilitiesEvent event) {
            onLoad((List) capabilityConstructorsEntity, event);
        }
    }

    public class ItemStackEventListener {
        @SubscribeEvent
        public void onItemStackLoad(RegisterCapabilitiesEvent event) {
            onLoad((List) capabilityConstructorsItem, event);
        }
    }
}
