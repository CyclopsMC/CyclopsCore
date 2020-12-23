package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Lists;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.nbt.StringNBT;
import net.minecraft.util.text.IFormattableTextComponent;
import net.minecraft.util.text.ITextComponent;
import net.minecraftforge.common.util.Constants;
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
    public static void setValue(IValueNotifier notifier, int valueId, INBT value) {
        CompoundNBT tag = new CompoundNBT();
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
        CompoundNBT tag = new CompoundNBT();
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
        CompoundNBT tag = new CompoundNBT();
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
        CompoundNBT tag = new CompoundNBT();
        tag.putString(KEY, value);
        notifier.setValue(valueId, tag);
    }

    /**
     * Set the {@link ITextComponent} value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @param value The value
     */
    public static void setValue(IValueNotifier notifier, int valueId, IFormattableTextComponent value) {
        CompoundNBT tag = new CompoundNBT();
        tag.putString(KEY, ITextComponent.Serializer.toJson(value));
        notifier.setValue(valueId, tag);
    }

    /**
     * Set the {@link ITextComponent} list value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @param values The values
     */
    public static void setValue(IValueNotifier notifier, int valueId, List<IFormattableTextComponent> values) {
        CompoundNBT tag = new CompoundNBT();
        ListNBT list = new ListNBT();
        for (ITextComponent value : values) {
            list.add(StringNBT.valueOf(ITextComponent.Serializer.toJson(value)));
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
    public static INBT getValueNbt(IValueNotifier notifier, int valueId) {
        CompoundNBT tag = notifier.getValue(valueId);
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
        CompoundNBT tag = notifier.getValue(valueId);
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
        CompoundNBT tag = notifier.getValue(valueId);
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
        CompoundNBT tag = notifier.getValue(valueId);
        if(tag != null) {
            return tag.getString(KEY);
        }
        return null;
    }

    /**
     * Get the {@link ITextComponent} value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @return The value
     */
    @Nullable
    public static IFormattableTextComponent getValueTextComponent(IValueNotifier notifier, int valueId) {
        CompoundNBT tag = notifier.getValue(valueId);
        if(tag != null) {
            return ITextComponent.Serializer.getComponentFromJson(tag.getString(KEY));
        }
        return null;
    }

    /**
     * Get the {@link ITextComponent} list value
     * @param notifier The notifier instance
     * @param valueId The value id
     * @return The value
     */
    @Nullable
    public static List<IFormattableTextComponent> getValueTextComponentList(IValueNotifier notifier, int valueId) {
        CompoundNBT tag = notifier.getValue(valueId);
        if(tag != null) {
            ListNBT listTag = tag.getList(KEY, Constants.NBT.TAG_STRING);
            List<IFormattableTextComponent> list = Lists.newArrayList();
            for (int i = 0; i < listTag.size(); i++) {
                list.add(ITextComponent.Serializer.getComponentFromJson(listTag.getString(i)));
            }
            return list;
        }
        return null;
    }

}
