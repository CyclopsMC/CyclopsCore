package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.ComponentSerialization;
import net.minecraft.network.chat.MutableComponent;
import org.cyclops.cyclopscore.inventory.IValueNotifier;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Helper methods for {@link org.cyclops.cyclopscore.inventory.IValueNotifiable} and
 * {@link org.cyclops.cyclopscore.inventory.IValueNotifier}.
 * @author rubensworks
 */
public class ValueNotifierHelpers {

    public static String KEY = "v";

    /**
     * Set the NBT value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @param value The value
     */
    public static void setValue(IValueNotifier notifier, int valueId, Tag value) {
        CompoundTag tag = new CompoundTag();
        tag.put(KEY, value);
        notifier.setValue(valueId, tag);
    }

    /**
     * Set the int value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @param value The value
     */
    public static void setValue(IValueNotifier notifier, int valueId, int value) {
        CompoundTag tag = new CompoundTag();
        tag.putInt(KEY, value);
        notifier.setValue(valueId, tag);
    }

    /**
     * Set the boolean value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @param value The value
     */
    public static void setValue(IValueNotifier notifier, int valueId, boolean value) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean(KEY, value);
        notifier.setValue(valueId, tag);
    }

    /**
     * Set the string value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @param value The value
     */
    public static void setValue(IValueNotifier notifier, int valueId, String value) {
        CompoundTag tag = new CompoundTag();
        tag.putString(KEY, value);
        notifier.setValue(valueId, tag);
    }

    /**
     * Set the {@link Component} value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @param value The value
     */
    public static void setValue(IValueNotifier notifier, int valueId, Component value) {
        if (value != null) {
            CompoundTag tag = new CompoundTag();
            tag.store(KEY, ComponentSerialization.CODEC, value);
            notifier.setValue(valueId, tag);
        }
    }

    /**
     * Set the {@link MutableComponent} list value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @param values The values
     */
    public static void setValue(IValueNotifier notifier, int valueId, List<Component> values) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (Component value : values) {
            if (value != null) {
                CompoundTag subTag = new CompoundTag();
                subTag.store(KEY, ComponentSerialization.CODEC, value);
                list.add(subTag);
            }
        }
        tag.put(KEY, list);
        notifier.setValue(valueId, tag);
    }

    /**
     * get the NBT value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @return The value
     */
    @Nullable
    public static Tag getValueNbt(IValueNotifier notifier, int valueId) {
        CompoundTag tag = notifier.getValue(valueId);
        if(tag != null) {
            return tag.get(KEY);
        }
        return null;
    }

    /**
     * get the int value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @return The value
     */
    public static int getValueInt(IValueNotifier notifier, int valueId) {
        CompoundTag tag = notifier.getValue(valueId);
        if(tag != null) {
            return tag.getIntOr(KEY, 0);
        }
        return 0;
    }

    /**
     * get the boolean value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @return The value
     */
    public static boolean getValueBoolean(IValueNotifier notifier, int valueId) {
        CompoundTag tag = notifier.getValue(valueId);
        if(tag != null) {
            return tag.getBooleanOr(KEY, false);
        }
        return false;
    }

    /**
     * Get the string value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @return The value
     */
    @Nullable
    public static String getValueString(IValueNotifier notifier, int valueId) {
        CompoundTag tag = notifier.getValue(valueId);
        if(tag != null) {
            return tag.getStringOr(KEY, null);
        }
        return null;
    }

    /**
     * Get the {@link Component} value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @return The value
     */
    @Nullable
    public static Component getValueTextComponent(IValueNotifier notifier, int valueId) {
        CompoundTag tag = notifier.getValue(valueId);
        if(tag != null) {
            return tag.read(KEY, ComponentSerialization.CODEC).orElse(null);
        }
        return null;
    }

    /**
     * Get the {@link Component} list value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @return The value
     */
    @Nullable
    public static List<Component> getValueTextComponentList(IValueNotifier notifier, int valueId) {
        CompoundTag tag = notifier.getValue(valueId);
        if(tag != null) {
            ListTag listTag = tag.getList(KEY).orElseThrow();
            List<Component> list = Lists.newArrayList();
            for (int i = 0; i < listTag.size(); i++) {
                CompoundTag subTag = listTag.getCompound(i).orElseThrow();
                list.add(subTag.read(KEY, ComponentSerialization.CODEC).orElseThrow());
            }
            return list;
        }
        return null;
    }

}
