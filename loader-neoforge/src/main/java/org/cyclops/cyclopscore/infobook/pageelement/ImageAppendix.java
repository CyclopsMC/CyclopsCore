package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.resources.ResourceLocation;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoBookParser;
import org.cyclops.cyclopscore.infobook.InfoSection;

/**
 * Images that can be added to sections.
 * @author rubensworks
 */
public class ImageAppendix extends SectionAppendix<ImageAppendixClient> {

    private static final int OFFSET_Y = 0;

    private ResourceLocation resource;
    private int width;
    private int height;

    public ImageAppendix(IInfoBook infoBook, ResourceLocation resource, int width, int height) throws InfoBookParser.InvalidAppendixException {
        super(infoBook);
        this.resource = resource;
        this.width = width;
        this.height = height;
    }

    @Override
    protected int getOffsetY() {
        return OFFSET_Y;
    }

    @Override
    protected int getWidth() {
        return width;
    }

    @Override
    protected int getHeight() {
        return height;
    }

    @Override
    public ImageAppendixClient constructSectionAppendixClient() {
        return new ImageAppendixClient(this);
    }

    @Override
    public void preBakeElement(InfoSection infoSection) {

    }

    @Override
    public void bakeElement(InfoSection infoSection) {

    }

    public ResourceLocation getResource() {
        return resource;
    }
}
