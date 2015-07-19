package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Maps;

import java.util.Map;

/**
 * Registry for info books for a mod.
 * @author rubensworks
 */
public class InfoBookRegistry implements IInfoBookRegistry {

    private final Map<IInfoBook, InfoSection> bookRoots = Maps.newHashMap();

    @Override
    public void registerInfoBook(IInfoBook infoBook, String path) {
        bookRoots.put(infoBook, InfoBookParser.initializeInfoBook(infoBook, path));
    }

    @Override
    public InfoSection getRoot(IInfoBook infoBook) {
        return bookRoots.get(infoBook);
    }
}
