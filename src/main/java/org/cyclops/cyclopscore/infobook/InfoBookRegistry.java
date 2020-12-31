package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraftforge.client.event.RecipesUpdatedEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.TagsUpdatedEvent;
import net.minecraftforge.eventbus.api.EventPriority;
import net.minecraftforge.fml.event.server.FMLServerStartedEvent;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.infobook.pageelement.AdvancementRewards;

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
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onClientTagsLoaded);
        MinecraftForge.EVENT_BUS.addListener(EventPriority.LOWEST, this::onClientRecipesLoaded);
        MinecraftForge.EVENT_BUS.addListener(this::onServerStarted);
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
    public void onServerStarted(FMLServerStartedEvent event) {
        if (!MinecraftHelpers.isClientSide()) {
            // Only call this on dedicated servers, as the RecipesUpdatedEvent won't be emitted there
            afterRecipesAndTagsLoaded();
        }
    }

    public void afterRecipesAndTagsLoaded() {
        AdvancementRewards.reset();
        this.infobookStageTags = false;
        this.infobookStageRecipes = false;

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
