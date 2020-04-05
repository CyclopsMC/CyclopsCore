package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Maps;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import net.minecraftforge.registries.ForgeRegistries;

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

    @SubscribeEvent
    public void onRecipesLoaded(RecipesUpdatedEvent event) {
        // Load after recipes are loaded client-side
        for (Map.Entry<IInfoBook, String> entry : bookPaths.entrySet()) {
            bookRoots.put(entry.getKey(), InfoBookParser.initializeInfoBook(entry.getKey(), entry.getValue(), null));
            // Reset the infobook history
            entry.getKey().setCurrentSection(null);
        }
    }
}
