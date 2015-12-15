package org.cyclops.cyclopscore.block.property;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.StateMap;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.property.ExtendedBlockState;
import net.minecraftforge.common.property.IUnlistedProperty;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Implementation of {@link IBlockPropertyManager}.
 * Because of limitations, simply delegating to this using the {@link lombok.experimental.Delegate} annotation
 * will not work, you will also need to override the following method.
 * If {@link net.minecraftforge.common.property.IUnlistedProperty} are detected, an
 * {@link net.minecraftforge.common.property.ExtendedBlockState} will be automatically created instead of a normal
 * {@link net.minecraft.block.state.IBlockState}.
 * <code>
 *     {@literal @}Override
 *     protected BlockState createBlockState() {
 *         return (propertyManager = new BlockPropertyManagerComponent(this)).createDelegatedBlockState();
 *     }
 * </code>
 * @author rubensworks
 */
@Data
public class BlockPropertyManagerComponent implements IBlockPropertyManager {

    private static final Comparator<IProperty> DEFAULT_PROPERTY_COMPARATOR = new PropertyComparator();
    private static final Comparator<IUnlistedProperty> DEFAULT_UNLISTEDPROPERTY_COMPARATOR = new UnlistedPropertyComparator();

    private final Block block;
    private final IProperty[] properties;
    private final IUnlistedProperty[] unlistedProperties;
    private final IProperty[] propertiesReversed;
    private final Map<IProperty, ArrayList<Comparable>> propertyValues;
    private final Comparator<IProperty> propertyComparator;
    private final Comparator<IUnlistedProperty> unlistedPropertyComparator;

    public BlockPropertyManagerComponent(Block block, Comparator<IProperty> propertyComparator,
                                         Comparator<IUnlistedProperty> unlistedPropertyComparator) {
        this.block = block;
        this.propertyComparator = propertyComparator;
        this.unlistedPropertyComparator = unlistedPropertyComparator;
        try {
            Pair<IProperty[], IUnlistedProperty[]> allProperties = preprocessProperties();
            this.properties = allProperties.getLeft();
            this.unlistedProperties = allProperties.getRight();
            this.propertiesReversed = Arrays.copyOf(properties, properties.length);
            ArrayUtils.reverse(this.propertiesReversed);
            this.propertyValues = preprocessPropertyValues(this.properties);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public BlockPropertyManagerComponent(Block block) {
        this(block, DEFAULT_PROPERTY_COMPARATOR, DEFAULT_UNLISTEDPROPERTY_COMPARATOR);
    }

    private Comparator<IProperty> getPropertyComponent() {
        return this.propertyComparator;
    }

    private Comparator<IUnlistedProperty> getUnlistedPropertyComponent() {
        return this.unlistedPropertyComparator;
    }

    private Pair<IProperty[], IUnlistedProperty[]> preprocessProperties() throws IllegalAccessException {
        TreeSet<IProperty> sortedProperties = Sets.newTreeSet(getPropertyComparator());
        TreeSet<IUnlistedProperty> sortedUnlistedProperties = Sets.newTreeSet(getUnlistedPropertyComparator());
        TreeSet<IProperty> ignoredProperties = Sets.newTreeSet(getPropertyComparator());
        for(Class<?> clazz = block.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            for(Field field : clazz.getDeclaredFields()) {
                if(field.isAnnotationPresent(BlockProperty.class)) {
                    BlockProperty annotation = field.getAnnotation(BlockProperty.class);
                    boolean ignored = annotation.ignore();

                    Object fieldObject = field.get(block);
                    if(fieldObject instanceof IProperty) {
                        sortedProperties.add((IProperty) fieldObject);
                        if (ignored) ignoredProperties.add((IProperty) fieldObject);
                    } else if(fieldObject instanceof IUnlistedProperty) {
                        sortedUnlistedProperties.add((IUnlistedProperty) fieldObject);
                    } else if(fieldObject instanceof IProperty[]) {
                        for(IProperty property : ((IProperty[]) fieldObject)) {
                            sortedProperties.add(property);
                            if (ignored) ignoredProperties.add(property);
                        }
                    } else if(fieldObject instanceof IUnlistedProperty[]) {
                        Collections.addAll(sortedUnlistedProperties, ((IUnlistedProperty[]) fieldObject));
                    } else {
                        throw new IllegalArgumentException(String.format("The field %s in class %s can not be used " +
                                "as block property.", field.getName(), clazz.getCanonicalName()));
                    }
                }
            }
        }

        if (MinecraftHelpers.isClientSide() && ignoredProperties.size() != 0) {
            ignoreProperties(ignoredProperties);
        }

        IProperty[] properties = new IProperty[sortedProperties.size()];
        IUnlistedProperty[] unlistedProperties = new IUnlistedProperty[sortedUnlistedProperties.size()];
        return Pair.of(sortedProperties.toArray(properties), sortedUnlistedProperties.toArray(unlistedProperties));
    }

    @SideOnly(Side.CLIENT)
    protected void ignoreProperties(TreeSet<IProperty> ignoredProperties) {
        IProperty[] ignoredPropertiesArray = new IProperty[ignoredProperties.size()];
        ignoredProperties.toArray(ignoredPropertiesArray);
        ModelLoader.setCustomStateMapper(block,
                (new StateMap.Builder()).ignore(ignoredPropertiesArray).build());
    }

    @SuppressWarnings("unchecked")
    private Map<IProperty, ArrayList<Comparable>> preprocessPropertyValues(IProperty[] properties) {
        Map<IProperty, ArrayList<Comparable>> dict = Maps.newHashMap();
        for(IProperty property : properties) {
            ArrayList<Comparable> values = Lists.newArrayList((Collection<Comparable>) property.getAllowedValues());
            Collections.sort(values);
            dict.put(property, values);
        }
        return dict;
    }

    protected boolean ignoreMetaOverflow() {
        return false;
    }

    @Override
    public int getMetaFromState(IBlockState blockState) {
        int meta = 0;
        for(IProperty property : properties) {
            int propertySize = property.getAllowedValues().size();
            int propertyValueIndex = propertyValues.get(property).indexOf(blockState.getValue(property));
            if(propertyValueIndex < 0) {
                throw new RuntimeException(String.format("The value %s was not found in the calculated property " +
                        "values for %s.", propertyValueIndex, property));
            }
            meta = meta * propertySize + propertyValueIndex;
        }
        if(meta > 15 && !ignoreMetaOverflow()) {
            throw new RuntimeException(String.format("The metadata for %s was too large (%s) to store.", this, meta));
        }
        return meta;
    }

    @Override
    public IBlockState getStateFromMeta(int meta) {
        IBlockState blockState = block.getDefaultState();
        int metaLoop = meta;
        for(IProperty property : propertiesReversed) {
            int propertySize = property.getAllowedValues().size();
            int value = metaLoop % propertySize;
            Comparable propertyValue = propertyValues.get(property).get(value);
            if(propertyValue == null) {
                throw new RuntimeException(String.format("The value %s was not found in the calculated property " +
                        "values for %s.", value, property));
            }
            blockState = blockState.withProperty(property, propertyValue);
            metaLoop = (metaLoop - value) / propertySize;
        }
        return blockState;
    }

    @Override
    public BlockState createDelegatedBlockState() {
        if(unlistedProperties.length == 0) {
            return new BlockState(block, properties);
        } else {
            return new ExtendedBlockState(block, properties, unlistedProperties);
        }
    }

    public static class PropertyComparator implements Comparator<IProperty> {
        @Override
        public int compare(IProperty o1, IProperty o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }

    public static class UnlistedPropertyComparator implements Comparator<IUnlistedProperty> {
        @Override
        public int compare(IUnlistedProperty o1, IUnlistedProperty o2) {
            return o1.getName().compareTo(o2.getName());
        }
    }
}
