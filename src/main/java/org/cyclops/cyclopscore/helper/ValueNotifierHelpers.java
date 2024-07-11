package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
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
     * Set the {@link MutableComponent} value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @param value The value
     */
    public static void setValue(IValueNotifier notifier, int valueId, MutableComponent value) {
        if (value != null) {
            CompoundTag tag = new CompoundTag();
            tag.putString(KEY, Component.Serializer.toJson(value, notifier.getHolderLookupProvider()));
            notifier.setValue(valueId, tag);
        }
    }

    /**
     * Set the {@link MutableComponent} list value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @param values The values
     */
    public static void setValue(IValueNotifier notifier, int valueId, List<MutableComponent> values) {
        CompoundTag tag = new CompoundTag();
        ListTag list = new ListTag();
        for (Component value : values) {
            if (value != null) {
                list.add(StringTag.valueOf(Component.Serializer.toJson(value, notifier.getHolderLookupProvider())));
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
            return tag.getInt(KEY);
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
            return tag.getBoolean(KEY);
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
            return tag.getString(KEY);
        }
        return null;
    }

    /**
     * Get the {@link MutableComponent} value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @return The value
     */
    @Nullable
    public static MutableComponent getValueTextComponent(IValueNotifier notifier, int valueId) {
        CompoundTag tag = notifier.getValue(valueId);
        if(tag != null) {
            return Component.Serializer.fromJson(tag.getString(KEY), notifier.getHolderLookupProvider());
        }
        return null;
    }

    /**
     * Get the {@link MutableComponent} list value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @return The value
     */
    @Nullable
    public static List<MutableComponent> getValueTextComponentList(IValueNotifier notifier, int valueId) {
        CompoundTag tag = notifier.getValue(valueId);
        if(tag != null) {
            ListTag listTag = tag.getList(KEY, Tag.TAG_STRING);
            List<MutableComponent> list = Lists.newArrayList();
            for (int i = 0; i < listTag.size(); i++) {
                list.add(Component.Serializer.fromJson(listTag.getString(i), notifier.getHolderLookupProvider()));
            }
            return list;
        }
        return null;
    }

}
