package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Maps;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.eventhandler.EventPriority;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.ForgeRegistries;

import java.util.Map;

/**
 * Registry for info books for a mod.
 * @author rubensworks
 */
public class InfoBookRegistry implements IInfoBookRegistry {

    private final Map<IInfoBook, String> bookPaths = Maps.newIdentityHashMap();
    private final Map<IInfoBook, InfoSection> bookRoots = Maps.newIdentityHashMap();

    public InfoBookRegistry() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void registerInfoBook(IInfoBook infoBook, String path) {
        bookPaths.put(infoBook, path);
    }

    @Override
    public void registerSection(IInfoBook infoBook, String parentSection, String sectionPath) {
        InfoSection section = infoBook.getSection(parentSection);
        if (section == null) {
            throw new IllegalArgumentException(String.format("Could not find section '%s' in infobook '%s'.", parentSection, infoBook));
        }
        section.registerSection(InfoBookParser.initializeInfoBook(infoBook, sectionPath, section));
    }

    @Override
    public InfoSection getRoot(IInfoBook infoBook) {
        return bookRoots.get(infoBook);
    }

    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRegistryEvent(RegistryEvent.Register event) {
        // Load _after_ recipes are loaded
        if (event.getRegistry() == ForgeRegistries.RECIPES) {
            for (Map.Entry<IInfoBook, String> entry : bookPaths.entrySet()) {
                bookRoots.put(entry.getKey(), InfoBookParser.initializeInfoBook(entry.getKey(), entry.getValue(), null));
                // Reset the infobook history
                entry.getKey().setCurrentSection(null);
            }

        }
    }
}
