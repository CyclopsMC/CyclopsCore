package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Maps;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.datastructure.EvictingStack;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;

/**
 * Default implementation for an {@link IInfoBook}.
 *
 * @author rubensworks
 */
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

    public InfoBook(ModBase mod, int pagesPerView, String baseUrl) {
        this.mod = Objects.requireNonNull(mod);
        this.pagesPerView = pagesPerView;
        this.baseUrl = baseUrl;
    }

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

    public ModBase getMod() {
        return this.mod;
    }

    public int getPagesPerView() {
        return this.pagesPerView;
    }

    public String getBaseUrl() {
        return this.baseUrl;
    }

    public EvictingStack<InfoSection.Location> getHistory() {
        return this.history;
    }

    public int getCurrentPage() {
        return this.currentPage;
    }

    public InfoSection getCurrentSection() {
        return this.currentSection;
    }

    public InfoSectionTagIndex getTagIndex() {
        return this.tagIndex;
    }

    public Map<String, Pair<InfoSection, Integer>> getConfigLinks() {
        return this.configLinks;
    }

    public Map<String, InfoSection> getSections() {
        return this.sections;
    }

    public void setCurrentPage(int currentPage) {
        this.currentPage = currentPage;
    }

    public void setCurrentSection(InfoSection currentSection) {
        this.currentSection = currentSection;
    }

    public void setTagIndex(InfoSectionTagIndex tagIndex) {
        this.tagIndex = tagIndex;
    }

    public void setConfigLinks(Map<String, Pair<InfoSection, Integer>> configLinks) {
        this.configLinks = configLinks;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof InfoBook)) return false;
        final InfoBook other = (InfoBook) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$mod = this.getMod();
        final Object other$mod = other.getMod();
        if (this$mod == null ? other$mod != null : !this$mod.equals(other$mod)) return false;
        if (this.getPagesPerView() != other.getPagesPerView()) return false;
        final Object this$baseUrl = this.getBaseUrl();
        final Object other$baseUrl = other.getBaseUrl();
        if (this$baseUrl == null ? other$baseUrl != null : !this$baseUrl.equals(other$baseUrl)) return false;
        final Object this$history = this.getHistory();
        final Object other$history = other.getHistory();
        if (this$history == null ? other$history != null : !this$history.equals(other$history)) return false;
        if (this.getCurrentPage() != other.getCurrentPage()) return false;
        final Object this$currentSection = this.getCurrentSection();
        final Object other$currentSection = other.getCurrentSection();
        if (this$currentSection == null ? other$currentSection != null : !this$currentSection.equals(other$currentSection))
            return false;
        final Object this$tagIndex = this.getTagIndex();
        final Object other$tagIndex = other.getTagIndex();
        if (this$tagIndex == null ? other$tagIndex != null : !this$tagIndex.equals(other$tagIndex)) return false;
        final Object this$configLinks = this.getConfigLinks();
        final Object other$configLinks = other.getConfigLinks();
        if (this$configLinks == null ? other$configLinks != null : !this$configLinks.equals(other$configLinks))
            return false;
        final Object this$sections = this.getSections();
        final Object other$sections = other.getSections();
        if (this$sections == null ? other$sections != null : !this$sections.equals(other$sections)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof InfoBook;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $mod = this.getMod();
        result = result * PRIME + ($mod == null ? 43 : $mod.hashCode());
        result = result * PRIME + this.getPagesPerView();
        final Object $baseUrl = this.getBaseUrl();
        result = result * PRIME + ($baseUrl == null ? 43 : $baseUrl.hashCode());
        final Object $history = this.getHistory();
        result = result * PRIME + ($history == null ? 43 : $history.hashCode());
        result = result * PRIME + this.getCurrentPage();
        final Object $currentSection = this.getCurrentSection();
        result = result * PRIME + ($currentSection == null ? 43 : $currentSection.hashCode());
        final Object $tagIndex = this.getTagIndex();
        result = result * PRIME + ($tagIndex == null ? 43 : $tagIndex.hashCode());
        final Object $configLinks = this.getConfigLinks();
        result = result * PRIME + ($configLinks == null ? 43 : $configLinks.hashCode());
        final Object $sections = this.getSections();
        result = result * PRIME + ($sections == null ? 43 : $sections.hashCode());
        return result;
    }

    public String toString() {
        return "InfoBook(mod=" + this.getMod() + ", pagesPerView=" + this.getPagesPerView() + ", baseUrl=" + this.getBaseUrl() + ", history=" + this.getHistory() + ", currentPage=" + this.getCurrentPage() + ", currentSection=" + this.getCurrentSection() + ", tagIndex=" + this.getTagIndex() + ", configLinks=" + this.getConfigLinks() + ", sections=" + this.getSections() + ")";
    }
}
