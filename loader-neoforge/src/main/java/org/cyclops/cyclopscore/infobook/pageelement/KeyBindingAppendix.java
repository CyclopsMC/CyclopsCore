package org.cyclops.cyclopscore.infobook.pageelement;

import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoBookParser;
import org.cyclops.cyclopscore.infobook.InfoSection;

/**
 * An appendix for key bindings.
 * @author rubensworks
 */
public class KeyBindingAppendix extends SectionAppendix<KeyBindingAppendixClient> {

    public static final int WIDTH = 100;
    public static final int HEIGHT = 30;

    private final String keybindingName;

    public KeyBindingAppendix(IInfoBook infoBook, String keybindingName) throws InfoBookParser.InvalidAppendixException {
        super(infoBook);
        this.keybindingName = keybindingName;
        if (IModHelpers.get().getMinecraftHelpers().isClientSide()) {
            getSectionAppendixClient().loadKeybinding();
        }
    }

    @Override
    protected int getOffsetY() {
        return 0;
    }

    @Override
    protected int getWidth() {
        return WIDTH;
    }

    @Override
    protected int getHeight() {
        return HEIGHT;
    }

    @Override
    public KeyBindingAppendixClient constructSectionAppendixClient() throws InfoBookParser.InvalidAppendixException {
        return new KeyBindingAppendixClient(this);
    }

    @Override
    public void preBakeElement(InfoSection infoSection) {

    }

    @Override
    public void bakeElement(InfoSection infoSection) {

    }

    public String getKeybindingName() {
        return keybindingName;
    }
}
