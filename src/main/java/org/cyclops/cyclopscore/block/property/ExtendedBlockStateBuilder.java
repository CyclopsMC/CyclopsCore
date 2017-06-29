package org.cyclops.cyclopscore.block.property;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableTable;
import com.google.common.collect.Maps;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraftforge.common.property.IExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;

import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.Optional;

/**
 * An efficient builder for extended blockstates when many properties need to be applied all at once.
 * Builder instances are mutable.
 * @author rubensworks
 */
public class ExtendedBlockStateBuilder {

    private final IExtendedBlockState defaultBlockState;
    private final Map<IProperty<?>, Comparable<?>> properties;
    private final Map<IUnlistedProperty<?>, Optional<?>> unlistedProperties;

    protected ExtendedBlockStateBuilder(IExtendedBlockState defaultBlockState) {
        this.properties = Maps.newHashMap(defaultBlockState.getProperties());
        this.unlistedProperties = Maps.newHashMap(defaultBlockState.getUnlistedProperties());
        this.defaultBlockState = defaultBlockState;
    }

    public <T extends Comparable<T>, V extends T> ExtendedBlockStateBuilder withProperty(IProperty<T> property, V value) {
        if (!properties.containsKey(property)) {
            throw new IllegalArgumentException("Cannot set property " + property + " as it does not exist in " + getBlock().getBlockState());
        }
        if (!property.getAllowedValues().contains(value)) {
            throw new IllegalArgumentException("Cannot set property " + property + " to " + value + " on block " + Block.REGISTRY.getNameForObject(getBlock()) + ", it is not an allowed value");
        }
        properties.put(property, value);
        return this;
    }

    public <V> ExtendedBlockStateBuilder withProperty(IUnlistedProperty<V> property, V value) {
        if(!this.unlistedProperties.containsKey(property)) {
            throw new IllegalArgumentException("Cannot set unlisted property " + property + " as it does not exist in " + getBlock().getBlockState());
        }
        if(!property.isValid(value)) {
            throw new IllegalArgumentException("Cannot set unlisted property " + property + " to " + value + " on block " + Block.REGISTRY.getNameForObject(getBlock()) + ", it is not an allowed value");
        }
        if (unlistedProperties.get(property) == value) {
            return this;
        }
        unlistedProperties.put(property, Optional.ofNullable(value));
        return this;
    }

    protected Block getBlock() {
        return defaultBlockState.getBlock();
    }

    public IExtendedBlockState build() {
        return new BuiltExtendedBlockState(
                getBlock(),
                ImmutableMap.copyOf(properties),
                ImmutableMap.copyOf(unlistedProperties),
                null);
    }

    public static ExtendedBlockStateBuilder builder(IExtendedBlockState defaultBlockState) {
        return new ExtendedBlockStateBuilder(defaultBlockState);
    }

    public class BuiltExtendedBlockState extends BlockStateContainer.StateImplementation implements IExtendedBlockState {

        private final ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties;
        private Map<Map<IProperty<?>, Comparable<?>>, BlockStateContainer.StateImplementation> normalMap;

        protected BuiltExtendedBlockState(Block block, ImmutableMap<IProperty<?>, Comparable<?>> properties,
                                          ImmutableMap<IUnlistedProperty<?>, Optional<?>> unlistedProperties,
                                          ImmutableTable<IProperty<?>, Comparable<?>, IBlockState> table) {
            super(block, properties);
            this.unlistedProperties = unlistedProperties;
            this.propertyValueTable = table;
        }

        @Override
        public <T extends Comparable<T>, V extends T> IBlockState withProperty(IProperty<T> property, V value) {
            throw new UnsupportedOperationException("Can't set property on built blockstate");
        }

        @Override
        public <V> IExtendedBlockState withProperty(IUnlistedProperty<V> property, V value) {
            throw new UnsupportedOperationException("Can't set unlisted property on built blockstate");
        }

        @Override
        public ImmutableMap<IUnlistedProperty<?>, Optional<?>> getUnlistedProperties() {
            return unlistedProperties;
        }

        @Override
        public void buildPropertyValueTable(Map<Map<IProperty<?>, Comparable<?>>, BlockStateContainer.StateImplementation> map) {
            this.normalMap = map;
            super.buildPropertyValueTable(map);
        }

        @Override
        public IBlockState getClean() {
            return this.normalMap.get(getProperties());
        }

        @Override
        public Collection<IUnlistedProperty<?>> getUnlistedNames() {
            return Collections.unmodifiableCollection(unlistedProperties.keySet());
        }

        @Override
        public <V>V getValue(IUnlistedProperty<V> property) {
            Optional<?> value = this.unlistedProperties.get(property);
            if(value == null) {
                throw new IllegalArgumentException("Cannot get unlisted property " + property + " as it does not exist in " + getBlock().getBlockState());
            }
            return property.getType().cast(value.orElse(null));
        }
    }

}
