package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Maps;
import net.minecraft.client.gui.FontRenderer;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.infobook.pageelement.SectionAppendix;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * Section that shows a sorted tag index with links.
 * @author rubensworks
 */
public class InfoSectionTagIndex extends InfoSection {

    public InfoSectionTagIndex(IInfoBook infoBook, InfoSection parent) {
        super(infoBook, parent, parent.getSubSections(), "info_book." + infoBook.getMod().getModId() + ".tag_index", new ArrayList<String>(),
                new ArrayList<SectionAppendix>(), new ArrayList<String>());

        // treemap to ensure order by localized tag
        InfoBookParser.configLinks = Maps.newTreeMap(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                return L10NHelpers.localize(o1).compareTo(L10NHelpers.localize(o2));
            }
        });
        addSoftLinks(InfoBookParser.configLinks, getParent());
    }

    public void bakeSection(FontRenderer fontRenderer, int width, int maxLines, int lineHeight, int yOffset) {
        if(paragraphs.size() == 0) {
            addLinks(maxLines, lineHeight, yOffset, InfoBookParser.configLinks);
        }
        super.bakeSection(fontRenderer, width, maxLines, lineHeight, yOffset);
    }

    protected boolean shouldAddIndex() {
        return false;
    }

    protected void addSoftLinks(Map<String, Pair<InfoSection, Integer>> softLinks, InfoSection section) {
        for(String tag : section.getTags()) {
            if(softLinks.containsKey(tag)) {
                // TODO: add support for multiple tag occurences?
                throw new IllegalArgumentException("The tag " + tag + " occurs multiple times.");
            }
            ExtendedConfig<?> config = getInfoBook().getMod().getConfigHandler().getDictionary().get(tag);
            if(config != null) {
                softLinks.put(config.getFullUnlocalizedName(), Pair.of(section, 0));
            }
        }
        for(int i = 0; i < section.getSubSections(); i++) {
            addSoftLinks(softLinks, section.getSubSection(i));
        }
    }
}
