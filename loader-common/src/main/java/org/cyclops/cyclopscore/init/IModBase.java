package org.cyclops.cyclopscore.init;

import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import org.apache.commons.lang3.tuple.Pair;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.LoggerHelper;

import javax.annotation.Nullable;
import java.util.List;

/**
 * Base interface for mods which adds a few convenience methods.
 * @author rubensworks
 */
public interface IModBase {

    public String getModId();

    public IModHelpers getModHelpers();

    public ConfigHandler getConfigHandler();

    public LoggerHelper getLoggerHelper();

    @Nullable
    public CreativeModeTab getDefaultCreativeTab();

    public void registerDefaultCreativeTabEntry(ItemStack itemStack, CreativeModeTab.TabVisibility visibility);

    public List<Pair<ItemStack, CreativeModeTab.TabVisibility>> getDefaultCreativeTabEntries();

    /**
     * Log a new info message for this mod.
     * @param message The message to show.
     */
    public default void log(String message) {
        log(Level.INFO, message);
    }

    /**
     * Log a new message of the given level for this mod.
     * @param level The level in which the message must be shown.
     * @param message The message to show.
     */
    public default void log(Level level, String message) {
        getLoggerHelper().log(level, message);
    }

}
