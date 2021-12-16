package org.cyclops.cyclopscore.inventory.container;

import net.minecraft.nbt.CompoundTag;
import org.cyclops.cyclopscore.persist.nbt.NBTClassType;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A convenience datastructure that offers direct synchronization of values between server and client inside GUIs.
 * @param <T> The type of value.
 */
public class SyncedGuiVariable<T> implements Supplier<T> {

    private final ContainerExtended gui;
    private final int guiValueId;
    private final NBTClassType<T> nbtClassType;
    private final Supplier<T> serverValueSupplier;

    private CompoundTag lastTag;

    SyncedGuiVariable(ContainerExtended gui, Class<T> clazz, Supplier<T> serverValueSupplier) {
        this.gui = gui;
        this.guiValueId = gui.getNextValueId();
        this.nbtClassType = NBTClassType.getClassType(clazz);
        this.serverValueSupplier = serverValueSupplier;

        this.lastTag = null;
    }

    public void detectAndSendChanges() {
        T value = this.serverValueSupplier.get();
        CompoundTag tag = new CompoundTag();
        this.nbtClassType.writePersistedField("v", value, tag);
        if (!Objects.equals(this.lastTag, tag)) {
            this.gui.setValue(this.guiValueId, tag);
            this.lastTag = tag;
        }
    }

    @Override
    public T get() {
        CompoundTag tag = this.gui.getValue(this.guiValueId);
        if (tag == null) {
            return this.nbtClassType.getDefaultValue();
        }
        return this.nbtClassType.readPersistedField("v", tag);
    }

}
