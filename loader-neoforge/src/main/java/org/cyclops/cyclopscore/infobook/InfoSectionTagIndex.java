package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Maps;
import net.minecraft.client.gui.Font;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.infobook.pageelement.SectionAppendix;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Map;

/**
 * Section that shows a sorted tag index with links.
 * @author rubensworks
 */
public class InfoSectionTagIndex extends InfoSection {

    public InfoSectionTagIndex(IInfoBook infoBook, InfoSection parent, ModBase<?> mod) {
        super(infoBook, parent, parent.getSubSections(), "info_book." + infoBook.getMod().getModId() + ".tag_index", new ArrayList<String>(),
                new ArrayList<SectionAppendix>(), new ArrayList<String>(), mod);

        // treemap to ensure order by localized tag
        infoBook.setConfigLinks(Maps.<String, String, Pair<InfoSection, Integer>>newTreeMap(new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                if (!MinecraftHelpers.isClientSide()) {
                    return o1.compareTo(o2);
                }
                return L10NHelpers.localize(o1).compareTo(L10NHelpers.localize(o2));
            }
        }));
    }

    public void bakeSection(Font fontRenderer, int width, int maxLines, int lineHeight, int yOffset) {
        if(paragraphs.size() == 0) {
            addLinks(maxLines, lineHeight, yOffset, getInfoBook().getConfigLinks());
        }
        super.bakeSection(fontRenderer, width, maxLines, lineHeight, yOffset);
    }

    protected boolean shouldAddIndex() {
        return false;
    }

    public void addSoftLinks(InfoSection section) {
        Map<String, Pair<InfoSection, Integer>> softLinks = getInfoBook().getConfigLinks();
        for(String tag : section.getTags()) {
            // If the tag is of the format "mod:tag", then we scope it at that mod.
            ModBase mod = getInfoBook().getMod();
            if (tag.contains(":")) {
                String[] split = tag.split(":");
                mod = ModBase.get(split[0]);
                tag = split[1];
            }

            if(softLinks.containsKey(tag)) {
                throw new IllegalArgumentException("The tag " + tag + " occurs multiple times.");
            }

            ExtendedConfig<?, ?> config = mod.getConfigHandler().getDictionary().get(tag);
            if(config != null) {
                softLinks.put(config.getFullTranslationKey(), Pair.of(section, 0));
            }
        }
        for(int i = 0; i < section.getSubSections(); i++) {
            addSoftLinks(section.getSubSection(i));
        }
    }
}
