package org.cyclops.cyclopscore.modcompat.capabilities;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.capabilities.BaseCapability;
import net.neoforged.neoforge.capabilities.BlockCapability;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.IBlockCapabilityProvider;
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
    private final List<Pair<Supplier<? extends Block>, IBlockCapabilityConstructor<?, ?, ?, ? extends Block>>>
            capabilityConstructorsBlock = Lists.newArrayList();
    private final List<Pair<Supplier<EntityType<? extends Entity>>, ICapabilityConstructor<?, ?, ?, ? extends EntityType<? extends Entity>>>>
            capabilityConstructorsEntity = Lists.newArrayList();
    private final List<Pair<Supplier<ItemLike>, ICapabilityConstructor<?, ?, ?, ? extends ItemLike>>>
            capabilityConstructorsItem = Lists.newArrayList();
    private Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?, ?>>>
            capabilityConstructorsBlockEntitySuper = Sets.newHashSet();
    private Collection<Pair<Class<?>, IBlockCapabilityConstructor<?, ?, ?, ?>>>
            capabilityConstructorsBlockSuper = Sets.newHashSet();
    private Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?, ?>>>
            capabilityConstructorsEntitySuper = Sets.newHashSet();
    private Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?, ?>>>
            capabilityConstructorsItemSuper = Sets.newHashSet();

    protected final ModBase mod;
    protected boolean baked = false;
    protected boolean registeredBlockEntityEventListener = false;
    protected boolean registeredBlockEventListener = false;
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
     * Register a block entity capability constructor.
     * @param blockEntityType The block entity class.
     * @param constructor The capability constructor.
     * @param <T> The tile type.
     */
    public <T extends BlockEntity> void registerBlockEntity(Supplier<BlockEntityType<T>> blockEntityType, ICapabilityConstructor<?, ?, ?, BlockEntityType<T>> constructor) {
        checkNotBaked();
        capabilityConstructorsBlockEntity.add(Pair.of((Supplier) blockEntityType, constructor));

        if (!registeredBlockEntityEventListener) {
            registeredBlockEntityEventListener = true;
            CyclopsCore._instance.getModEventBus().register(new BlockEntityEventListener());
        }
    }

    /**
     * Register a tile capability constructor.
     * @param block The block.
     * @param constructor The capability constructor.
     * @param <T> The tile type.
     */
    public <T extends Block> void registerBlock(Supplier<T> block, IBlockCapabilityConstructor<?, ?, ?, T> constructor) {
        checkNotBaked();
        capabilityConstructorsBlock.add(Pair.of((Supplier) block, constructor));

        if (!registeredBlockEventListener) {
            registeredBlockEventListener = true;
            CyclopsCore._instance.getModEventBus().register(new BlockEventListener());
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
     * Register a block entity capability constructor with subtype checking.
     * Only call this when absolutely required, this will is less efficient than its non-inheritable counterpart.
     * @param clazz The block entity class, all subtypes will be checked.
     * @param constructor The capability constructor.
     */
    public void registerInheritableBlockEntity(Class<?> clazz, ICapabilityConstructor<?, ?, ?, ?> constructor) {
        checkNotBaked();
        capabilityConstructorsBlockEntitySuper.add(Pair.of(clazz, constructor));

        if (!registeredBlockEntityEventListener) {
            registeredBlockEntityEventListener = true;
            CyclopsCore._instance.getModEventBus().register(new BlockEntityEventListener());
        }
    }

    /**
     * Register a block capability constructor with subtype checking.
     * Only call this when absolutely required, this will is less efficient than its non-inheritable counterpart.
     * @param clazz The block class, all subtypes will be checked.
     * @param constructor The capability constructor.
     */
    public void registerInheritableBlock(Class<?> clazz, IBlockCapabilityConstructor<?, ?, ?, ?> constructor) {
        checkNotBaked();
        capabilityConstructorsBlockSuper.add(Pair.of(clazz, constructor));

        if (!registeredBlockEventListener) {
            registeredBlockEventListener = true;
            CyclopsCore._instance.getModEventBus().register(new BlockEventListener());
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
        capabilityConstructorsEntitySuper.add(Pair.of(clazz, constructor));

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
        capabilityConstructorsItemSuper.add(Pair.of(clazz, constructor));

        if (!registeredItemStackEventListener) {
            registeredItemStackEventListener = true;
            CyclopsCore._instance.getModEventBus().register(new ItemStackEventListener());
        }
    }

    @SuppressWarnings("unchecked")
    protected <CK> ICapabilityProvider<?, ?, ?> createProvider(CK capabilityKey, ICapabilityConstructor<?, ?, ?, CK> capabilityConstructor) {
        return capabilityConstructor.createProvider(capabilityKey);
    }

    protected <CK> IBlockCapabilityProvider<?, ?> createProviderBlock(CK capabilityKey, IBlockCapabilityConstructor<?, ?, ?, CK> capabilityConstructor) {
        return capabilityConstructor.createProvider(capabilityKey);
    }

    protected <CK> void onLoad(List<Pair<Supplier<CK>, ICapabilityConstructor<?, ?, ?, CK>>> allConstructors, RegisterCapabilitiesEvent event) {
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

    protected <CK> void addLoadedCapabilityProvider(RegisterCapabilitiesEvent event, CK capabilityKey, ICapabilityTypeGetter<?, ?> constructor) {
        BaseCapability capability = constructor.getCapability();
        if (capabilityKey instanceof BlockEntityType blockEntityType) {
            ICapabilityProvider provider = createProvider(capabilityKey, (ICapabilityConstructor<?, ?, ?, ? super CK>) constructor);
            if (provider != null) {
                event.registerBlockEntity((BlockCapability) capability, blockEntityType, provider);
            }
        } else if (capabilityKey instanceof Block block) {
            IBlockCapabilityProvider provider = createProviderBlock(capabilityKey, (IBlockCapabilityConstructor<?, ?, ?, ? super CK>) constructor);
            if (provider != null) {
                event.registerBlock((BlockCapability) capability, (IBlockCapabilityProvider) provider, block);
            }
        } else if (capabilityKey instanceof EntityType entityType) {
            ICapabilityProvider provider = createProvider(capabilityKey, (ICapabilityConstructor<?, ?, ?, ? super CK>) constructor);
            if (provider != null) {
                event.registerEntity((EntityCapability) capability, entityType, provider);
            }
        } else if (capabilityKey instanceof ItemLike itemLike) {
            ICapabilityProvider provider = createProvider(capabilityKey, (ICapabilityConstructor<?, ?, ?, ? super CK>) constructor);
            if (provider != null) {
                event.registerItem((ItemCapability) capability, provider, itemLike);
            }
        } else {
            throw new IllegalStateException("Capability of type " + capability.getClass() + " is not supported");
        }
    }

    /**
     * Bakes the registry so that it becomes immutable.
     */
    public void bake() {
        baked = true;

        // Bake all collections
        bakeCapabilityConstructorsSuper((Collection) capabilityConstructorsBlockEntitySuper, (List) capabilityConstructorsBlockEntity);
        bakeCapabilityConstructorsSuper((Collection) capabilityConstructorsEntitySuper, (List) capabilityConstructorsEntity);
        bakeCapabilityConstructorsSuper((Collection) capabilityConstructorsItemSuper, (List) capabilityConstructorsItem);

        // Special case for blocks
        for (Pair<Class<?>, IBlockCapabilityConstructor<?, ?, ?, Block>> entry : (Collection<Pair<Class<?>, IBlockCapabilityConstructor<?, ?, ?, Block>>>) (Collection) capabilityConstructorsBlockSuper) {
            Class<?> type = entry.getLeft();
            BuiltInRegistries.BLOCK.forEach(block -> {
                if (type.isInstance(block)) {
                    capabilityConstructorsBlock.add(Pair.of((Supplier) () -> block, entry.getRight()));
                }
            });
        }

        capabilityConstructorsBlockEntitySuper = null;
        capabilityConstructorsBlockSuper = null;
        capabilityConstructorsEntitySuper = null;
        capabilityConstructorsItemSuper = null;
    }

    protected <CK> void bakeCapabilityConstructorsSuper(
            Collection<Pair<Class<?>, ICapabilityConstructor<?, ?, ?, CK>>> allInheritableConstructors,
            List<Pair<Supplier<CK>, ICapabilityConstructor<?, ?, ?, CK>>> allConstructors
    ) {
        // Inheritable constructors
        for (Pair<Class<?>, ICapabilityConstructor<?, ?, ?, CK>> entry : allInheritableConstructors) {
            Class<?> type = entry.getLeft();
            if (BlockEntity.class.isAssignableFrom(type)) {
                BuiltInRegistries.BLOCK_ENTITY_TYPE.forEach(blockEntityType -> {
                    if (type.isInstance(blockEntityType)) {
                        allConstructors.add(Pair.of((Supplier) () -> blockEntityType, entry.getRight()));
                    }
                });
            } else if (EntityType.class.isAssignableFrom(type)) {
                BuiltInRegistries.ENTITY_TYPE.forEach(entityType -> {
                    if (type.isInstance(entityType)) {
                        allConstructors.add(Pair.of((Supplier) () -> entityType, entry.getRight()));
                    }
                });
            } else if (Item.class.isAssignableFrom(type)) {
                BuiltInRegistries.ITEM.forEach(item -> {
                    if (type.isInstance(item)) {
                        allConstructors.add(Pair.of((Supplier) () -> item, entry.getRight()));
                    }
                });
            } else {
                throw new IllegalStateException("Capability of type " + type + " is not supported");
            }
        }
    }

    public class BlockEntityEventListener {
        @SubscribeEvent
        public void onBlockEntityLoad(RegisterCapabilitiesEvent event) {
            onLoad((List) capabilityConstructorsBlockEntity, event);
        }
    }

    public class BlockEventListener {
        @SubscribeEvent
        public void onBlockLoad(RegisterCapabilitiesEvent event) {
            onLoad((List) capabilityConstructorsBlock, event);
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
