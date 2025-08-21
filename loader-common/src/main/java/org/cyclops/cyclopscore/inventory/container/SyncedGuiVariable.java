package org.cyclops.cyclopscore.inventory.container;

import com.mojang.logging.LogUtils;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.level.storage.ValueInput;
import org.cyclops.cyclopscore.persist.nbt.NBTClassType;
import org.slf4j.Logger;

import java.util.Objects;
import java.util.function.Supplier;

/**
 * A convenience datastructure that offers direct synchronization of values between server and client inside GUIs.
 * @param <T> The type of value.
 */
public class SyncedGuiVariable<T> implements Supplier<T> {

    private static final Logger LOGGER = LogUtils.getLogger();

    private final ContainerExtended gui;
    private final int guiValueId;
    private final NBTClassType<T> nbtClassType;
    private final Supplier<T> serverValueSupplier;
    private final HolderLookup.Provider holderLookupProvider;

    private CompoundTag lastTag;

    SyncedGuiVariable(ContainerExtended gui, Class<T> clazz, Supplier<T> serverValueSupplier, HolderLookup.Provider holderLookupProvider) {
        this.gui = gui;
        this.guiValueId = gui.getNextValueId();
        this.nbtClassType = NBTClassType.getClassType(clazz);
        this.serverValueSupplier = serverValueSupplier;
        this.holderLookupProvider = holderLookupProvider;

        this.lastTag = null;
    }

    public void detectAndSendChanges() {
        T value = this.serverValueSupplier.get();
        try (ProblemReporter.ScopedCollector problemReporter = new ProblemReporter.ScopedCollector(gui.player.problemPath(), LOGGER)) {
            TagValueOutput tagValueOutput = TagValueOutput.createWithContext(problemReporter, this.holderLookupProvider);
            this.nbtClassType.writePersistedField("v", value, tagValueOutput);
            CompoundTag tag = tagValueOutput.buildResult();
            if (!Objects.equals(this.lastTag, tag)) {
                this.gui.setValue(this.guiValueId, tag);
                this.lastTag = tag;
            }
        }
    }

    @Override
    public T get() {
        CompoundTag tag = this.gui.getValue(this.guiValueId);
        if (tag == null || !tag.contains("v")) {
            return this.nbtClassType.getDefaultValue();
        }
        try (ProblemReporter.ScopedCollector problemreporter$scopedcollector = new ProblemReporter.ScopedCollector(LOGGER)) {
            ValueInput input = TagValueInput.create(
                    problemreporter$scopedcollector.forChild(gui.player.problemPath()),
                    this.holderLookupProvider,
                    tag
            );
            return this.nbtClassType.readPersistedField("v", input);
        }
    }

}
