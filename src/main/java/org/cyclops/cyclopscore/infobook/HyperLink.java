package org.cyclops.cyclopscore.infobook;

import lombok.Getter;

/**
 * A link wrapper targeted at other sections.
 * @author rubensworks
 */
public class HyperLink {

    @Getter private int x, y;
    @Getter private InfoSection target;
    @Getter private String translationKey;

    public HyperLink(int x, int y, InfoSection target, String translationKey) {
        this.x = x;
        this.y = y;
        this.target = target;
        this.translationKey = translationKey;
    }

}
