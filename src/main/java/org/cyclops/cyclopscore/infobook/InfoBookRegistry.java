package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Registry for info books for a mod.
 * @author rubensworks
 */
public class InfoBookRegistry implements IInfoBookRegistry {

    private final Map<IInfoBook, InfoSection> bookRoots = Maps.newIdentityHashMap();

    @Override
    public void registerInfoBook(IInfoBook infoBook, String path) {
        bookRoots.put(infoBook, InfoBookParser.initializeInfoBook(infoBook, path, null));
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
}
