package org.cyclops.cyclopscore.infobook;

import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.datastructure.EvictingStack;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.Map;

/**
 * Instances of this can be used with the {@link InfoBookRegistry} to create in-game manuals.
 * @author rubensworks
 */
public interface IInfoBook {

    public ModBase getMod();
    public int getPagesPerView();
    public String getBaseUrl();

    public void setCurrentPage(int page);
    public void setCurrentSection(InfoSection section);
    public int getCurrentPage();
    public InfoSection getCurrentSection();
    public void addSection(String sectionName, InfoSection section);
    public InfoSection getSection(String sectionName);

    public Map<String, Pair<InfoSection, Integer>> getConfigLinks();
    public void setConfigLinks(Map<String, Pair<InfoSection, Integer>> configLinks);
    public InfoSectionTagIndex getTagIndex();
    public void setTagIndex(InfoSectionTagIndex tagIndex);

    public EvictingStack<InfoSection.Location> getHistory();

}
