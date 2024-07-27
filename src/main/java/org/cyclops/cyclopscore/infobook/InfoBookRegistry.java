package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Maps;
import net.neoforged.bus.api.EventPriority;
import net.neoforged.neoforge.client.event.RecipesUpdatedEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.TagsUpdatedEvent;
import net.neoforged.neoforge.event.server.ServerStartedEvent;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.infobook.pageelement.AdvancementRewards;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.Map;
import java.util.Objects;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Registry for info books for a mod.
 * @author rubensworks
 */
public class InfoBookRegistry implements IInfoBookRegistry {

    static {
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, InfoBookRegistry::onClientTagsLoadedStatic);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, InfoBookRegistry::onClientRecipesLoadedStatic);
        NeoForge.EVENT_BUS.addListener(EventPriority.HIGHEST, InfoBookRegistry::onServerStartedStatic);
    }

    private final Map<IInfoBook, String> bookPaths = Maps.newIdentityHashMap();
    private final Map<IInfoBook, InfoSection> bookRoots = Maps.newIdentityHashMap();
    private final Queue<SectionInjection> sectionInjections = new LinkedBlockingQueue<>(); // Thread-safe queue

    public InfoBookRegistry() {
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onClientTagsLoaded);
        NeoForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onClientRecipesLoaded);
        NeoForge.EVENT_BUS.addListener(this::onServerStarted);
    }

    @Override
    public void registerInfoBook(IInfoBook infoBook, String path) {
        bookPaths.put(infoBook, path);
    }

    @Override
    public void registerSection(ModBase<?> mod, IInfoBook infoBook, String parentSection, String sectionPath) {
        synchronized (sectionInjections) {
            sectionInjections.add(new SectionInjection(Objects.requireNonNull(mod), infoBook, parentSection, sectionPath));
        }
    }

    @Override
    public InfoSection getRoot(IInfoBook infoBook) {
        return bookRoots.get(infoBook);
    }

    // Reset achievement rewards to avoid remembering stuff across different servers and client worlds.
    // We have this in static methods to make sure that this is called only once per server start,
    // to avoid it being called for every infobook registry.
    private static volatile boolean infobookStageTagsStatic = false;
    private static volatile boolean infobookStageRecipesStatic = false;
    public static void onClientTagsLoadedStatic(TagsUpdatedEvent event) {
        infobookStageTagsStatic = true;
        if (infobookStageTagsStatic && infobookStageRecipesStatic) {
            infobookStageTagsStatic = false;
            infobookStageRecipesStatic = false;
            AdvancementRewards.reset();
        }
    }
    public static void onClientRecipesLoadedStatic(RecipesUpdatedEvent event) {
        infobookStageRecipesStatic = true;
        if (infobookStageTagsStatic && infobookStageRecipesStatic) {
            infobookStageTagsStatic = false;
            infobookStageRecipesStatic = false;
            AdvancementRewards.reset();
        }
    }
    public static void onServerStartedStatic(ServerStartedEvent event) {
        if (event.getServer().isDedicatedServer()) {
            infobookStageTagsStatic = false;
            infobookStageRecipesStatic = false;
            // Only call this on dedicated servers, as the RecipesUpdatedEvent won't be emitted there
            AdvancementRewards.reset();
        }
    }

    // Reload infobooks if BOTH tags and recipes are initialized (can occur out-of-order in SMP)
    private volatile boolean infobookStageTags = false;
    private volatile boolean infobookStageRecipes = false;
    public void onClientTagsLoaded(TagsUpdatedEvent event) {
        infobookStageTags = true;
        if (infobookStageTags && infobookStageRecipes) {
            afterRecipesAndTagsLoaded();
        }
    }
    public void onClientRecipesLoaded(RecipesUpdatedEvent event) {
        infobookStageRecipes = true;
        if (infobookStageTags && infobookStageRecipes) {
            afterRecipesAndTagsLoaded();
        }
    }
    public void onServerStarted(ServerStartedEvent event) {
        if (event.getServer().isDedicatedServer()) {
            // Only call this on dedicated servers, as the RecipesUpdatedEvent won't be emitted there
            afterRecipesAndTagsLoaded();
        }
    }

    public void afterRecipesAndTagsLoaded() {
        this.infobookStageTags = false;
        this.infobookStageRecipes = false;

        // Load after recipes are loaded client-side
        for (Map.Entry<IInfoBook, String> entry : bookPaths.entrySet()) {
            entry.getKey().getMod().log(Level.INFO, "Loading infobook " + entry.getValue());
            bookRoots.put(entry.getKey(), InfoBookParser.initializeInfoBook(entry.getKey().getMod(), entry.getKey(), entry.getValue(), null));
            // Reset the infobook history
            entry.getKey().setCurrentSection(null);
        }

        // Load section injections
        for (SectionInjection sectionInjection : sectionInjections) {
            InfoSection section = sectionInjection.getInfoBook().getSection(sectionInjection.getParentSection());
            if (section == null) {
                throw new IllegalArgumentException(String.format("Could not find section '%s' in infobook '%s'.", sectionInjection.getParentSection(), sectionInjection.getInfoBook()));
            }
            section.registerSection(InfoBookParser.initializeInfoBook(sectionInjection.getMod(), sectionInjection.getInfoBook(), sectionInjection.getSectionPath(), section));
        }
    }

    private static final class SectionInjection {
        private final ModBase<?> mod;
        private final IInfoBook infoBook;
        private final String parentSection;
        private final String sectionPath;

        private SectionInjection(ModBase<?> mod, IInfoBook infoBook, String parentSection, String sectionPath) {
            this.mod = mod;
            this.infoBook = Objects.requireNonNull(infoBook);
            this.parentSection = parentSection;
            this.sectionPath = sectionPath;
        }

        public ModBase<?> getMod() {
            return mod;
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
