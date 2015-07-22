package org.cyclops.cyclopscore.infobook;

import org.cyclops.cyclopscore.datastructure.EvictingStack;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Instances of this can be used with the {@link InfoBookRegistry} to create in-game manuals.
 * @author rubensworks
 */
public interface IInfoBook {

    public ModBase getMod();
    public int getPagesPerView();

    public void setCurrentPage(int page);
    public void setCurrentSection(InfoSection section);
    public int getCurrentPage();
    public InfoSection getCurrentSection();

    public EvictingStack<InfoSection.Location> getHistory();

}
