package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.eventbus.api.SubscribeEvent;

import java.util.List;
import java.util.Map;

/**
 * Registry for info books for a mod.
 * @author rubensworks
 */
public class InfoBookRegistry implements IInfoBookRegistry {

    private final Map<IInfoBook, String> bookPaths = Maps.newIdentityHashMap();
    private final Map<IInfoBook, InfoSection> bookRoots = Maps.newIdentityHashMap();
    private final List<SectionInjection> sectionInjections = Lists.newArrayList();

    public InfoBookRegistry() {
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Override
    public void registerInfoBook(IInfoBook infoBook, String path) {
        bookPaths.put(infoBook, path);
    }

    @Override
    public void registerSection(IInfoBook infoBook, String parentSection, String sectionPath) {
        sectionInjections.add(new SectionInjection(infoBook, parentSection, sectionPath));
    }

    @Override
    public InfoSection getRoot(IInfoBook infoBook) {
        return bookRoots.get(infoBook);
    }

    // Reload infobooks if BOTH tags and recipes are initialized (can occur out-of-order in SMP)
    private volatile int infobookStageEvents = 0;
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onTagsLoaded(TagsUpdatedEvent event) {
        if (++infobookStageEvents >= 2) {
            afterRecipesAndTagsLoaded();
        }
    }
    @SubscribeEvent(priority = EventPriority.LOWEST)
    public void onRecipesLoaded(RecipesUpdatedEvent event) {
        if (++infobookStageEvents >= 2) {
            afterRecipesAndTagsLoaded();
        }
    }

    public void afterRecipesAndTagsLoaded() {
        this.infobookStageEvents = 0;

        // Load after recipes are loaded client-side
        for (Map.Entry<IInfoBook, String> entry : bookPaths.entrySet()) {
            bookRoots.put(entry.getKey(), InfoBookParser.initializeInfoBook(entry.getKey(), entry.getValue(), null));
            // Reset the infobook history
            entry.getKey().setCurrentSection(null);
        }

        // Load section injections
        for (SectionInjection sectionInjection : sectionInjections) {
            InfoSection section = sectionInjection.getInfoBook().getSection(sectionInjection.getParentSection());
            if (section == null) {
                throw new IllegalArgumentException(String.format("Could not find section '%s' in infobook '%s'.", sectionInjection.getParentSection(), sectionInjection.getInfoBook()));
            }
            section.registerSection(InfoBookParser.initializeInfoBook(sectionInjection.getInfoBook(), sectionInjection.getSectionPath(), section));
        }
    }

    private static final class SectionInjection {
        private final IInfoBook infoBook;
        private final String parentSection;
        private final String sectionPath;

        private SectionInjection(IInfoBook infoBook, String parentSection, String sectionPath) {
            this.infoBook = infoBook;
            this.parentSection = parentSection;
            this.sectionPath = sectionPath;
        }

        public IInfoBook getInfoBook() {
            return infoBook;
        }

        public String getParentSection() {
            return parentSection;
        }

        public String getSectionPath() {
            return sectionPath;
        }
    }

}
