package org.cyclops.cyclopscore.infobook.test;

import net.minecraft.client.audio.SoundHandler;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

/**
 * Gui for the On the Dynamics of Integration book.
 * @author rubensworks
 */
public class ContainerScreenInfoBookTest extends ScreenInfoBook<ContainerInfoBookTest> {

    public ContainerScreenInfoBookTest(ContainerInfoBookTest container, PlayerInventory playerInventory, ITextComponent title) {
        super(container, playerInventory, title, InfoBookTest.getInstance());
    }

    @Override
    protected int getGuiWidth() {
        return 283;
    }

    @Override
    protected int getGuiHeight() {
        return 180;
    }

    @Override
    protected int getPageWidth() {
        return 142;
    }

    @Override
    protected int getPageYOffset() {
        return 9;
    }

    @Override
    protected int getFootnoteOffsetX() {
        return -2;
    }

    @Override
    protected int getFootnoteOffsetY() {
        return -8;
    }

    @Override
    protected int getPrevNextOffsetY() {
        return 7;
    }

    @Override
    protected int getPrevNextOffsetX() {
        return 16;
    }

    @Override
    protected int getOffsetXForPageBase(int page) {
        return page == 0 ? 20 : 10;
    }

    @Override
    public int getTitleColor() {
        return Helpers.RGBToInt(70, 70, 150);
    }

    @Override
    public void playPageFlipSound(SoundHandler soundHandler) {
        // Do nothing
    }

    @Override
    public void playPagesFlipSound(SoundHandler soundHandler) {
        // Do nothing
    }

    @Override
    protected ResourceLocation constructGuiTexture() {
        return new ResourceLocation("textures/gui/book.png");
    }
}
