package org.cyclops.cyclopscore.block;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import lombok.Data;
import net.minecraft.block.Block;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockState;
import net.minecraft.block.state.IBlockState;
import org.apache.commons.lang3.ArrayUtils;

import java.lang.reflect.Field;
import java.util.*;

/**
 * Implementation of {@link org.cyclops.cyclopscore.block.IBlockPropertyManager}.
 * Because of limitations, simply delegating to this using the {@link lombok.experimental.Delegate} annotation
 * will not work, you will also need to override the following method.
 * <code>
 *     {@literal @}Override
 *     protected BlockState createBlockState() {
 *         return (propertyManager = new BlockPropertyManagerComponent(this)).createDelegatedBlockState();
 *     }
 * </code>
 * @author rubensworks
 */
@Data
public class BlockPropertyManagerComponent implements IBlockPropertyManager, Comparator<IProperty> {

    private final Block block;
    private final IProperty[] properties;
    private final IProperty[] propertiesReversed;
    private final Map<IProperty, ArrayList<Comparable>> propertyValues;

    public BlockPropertyManagerComponent(Block block) {
        this.block = block;
        try {
            this.properties = preprocessProperties();
            this.propertiesReversed = Arrays.copyOf(properties, properties.length);
            ArrayUtils.reverse(this.propertiesReversed);
            this.propertyValues = preprocessPropertyValues(this.properties);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private IProperty[] preprocessProperties() throws IllegalAccessException {
        TreeSet<IProperty> sortedProperties = Sets.newTreeSet(this);
        for(Class<?> clazz = block.getClass(); clazz != null; clazz = clazz.getSuperclass()) {
            for(Field field : clazz.getDeclaredFields()) {
                if(field.isAnnotationPresent(BlockProperty.class)) {
                    sortedProperties.add((IProperty) field.get(block));
                }
            }
        }
        IProperty[] properties = new IProperty[sortedProperties.size()];
        return sortedProperties.toArray(properties);
    }

    private Map<IProperty, ArrayList<Comparable>> preprocessPropertyValues(IProperty[] properties) {
        Map<IProperty, ArrayList<Comparable>> dict = Maps.newHashMap();
        for(IProperty property : properties) {
            ArrayList<Comparable> values = Lists.newArrayList((Collection<Comparable>) property.getAllowedValues());
            Collections.sort(values);
            dict.put(property, values);
        }
        return dict;
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
        if(meta > Character.MAX_VALUE) {
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
        return new BlockState(block, properties);
    }

    @Override
    public int compare(IProperty o1, IProperty o2) {
        return o1.getName().compareTo(o2.getName());
    }
}
