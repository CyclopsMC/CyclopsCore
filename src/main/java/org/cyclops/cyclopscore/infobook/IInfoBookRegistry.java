package org.cyclops.cyclopscore.infobook;

import org.cyclops.cyclopscore.init.IRegistry;

/**
 * Registry for in-game manuals.
 * @author rubensworks
 */
public interface IInfoBookRegistry extends IRegistry {

    /**
     * Register a new infobook.
     * @param infoBook The infobook to register.
     * @param path The path to the xml file of the book.
     */
    public void registerInfoBook(IInfoBook infoBook, String path);

    /**
     * Register a new section to the given section of the given infobook.
     * @param infoBook The infobook to register to.
     * @param parentSection The section to register this new section to.
     * @param sectionPath The path to the xml file of the section to register.
     */
    public void registerSection(IInfoBook infoBook, String parentSection, String sectionPath);

    /**
     * Get the root of the given book.
     * @param infoBook The info book to get the root from.
     * @return The root section.
     */
    public InfoSection getRoot(IInfoBook infoBook);

}
