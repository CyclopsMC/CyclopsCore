package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Maps;
import lombok.Data;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.datastructure.EvictingStack;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.Collections;
import java.util.Map;

/**
 * Default implementation for an {@link IInfoBook}.
 * @author rubensworks
 */
@Data
public class InfoBook implements IInfoBook {

    private final ModBase mod;
    private final int pagesPerView;
    private final String baseUrl;
    private final EvictingStack<InfoSection.Location> history = new EvictingStack<InfoSection.Location>(128);
    private int currentPage = 0;
    private InfoSection currentSection = null;
    private InfoSectionTagIndex tagIndex = null;
    private Map<String, Pair<InfoSection, Integer>> configLinks = Collections.emptyMap();

    private final Map<String, InfoSection> sections = Maps.newHashMap();

    @Override
    public void addSection(String sectionName, InfoSection section) {
        sections.put(sectionName, section);
    }

    @Override
    public InfoSection getSection(String sectionName) {
        return sections.get(sectionName);
    }

    @Override
    public void putIndex(InfoSectionTagIndex tagIndex) {
        this.tagIndex = tagIndex;
    }
}
