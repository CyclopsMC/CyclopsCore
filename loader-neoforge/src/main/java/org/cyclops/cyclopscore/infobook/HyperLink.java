package org.cyclops.cyclopscore.infobook;

/**
 * A link wrapper targeted at other sections.
 *
 * @author rubensworks
 */
public class HyperLink {

    private int x, y;
    private InfoSection target;
    private String translationKey;

    public HyperLink(int x, int y, InfoSection target, String translationKey) {
        this.x = x;
        this.y = y;
        this.target = target;
        this.translationKey = translationKey;
    }

    public int getX() {
        return this.x;
    }

    public int getY() {
        return this.y;
    }

    public InfoSection getTarget() {
        return this.target;
    }

    public String getTranslationKey() {
        return this.translationKey;
    }
}
