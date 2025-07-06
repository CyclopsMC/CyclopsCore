package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.StringSplitter;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Style;
import org.apache.commons.lang3.StringUtils;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoBookParser;
import org.cyclops.cyclopscore.infobook.InfoSection;

import java.util.List;

/**
 * Text fields that can be added to sections.
 * @author rubensworks
 */
public class TextFieldAppendix extends SectionAppendix<TextFieldAppendixClient> {

    private static final int OFFSET_Y = 0;

    private final String text;
    private final double scale;
    private int height;
    private int maxWidth;
    private List<String> lines = null;

    public TextFieldAppendix(IInfoBook infoBook, String text, double scale) throws InfoBookParser.InvalidAppendixException {
        super(infoBook);
        this.text = text;
        this.scale = scale;
        this.height = this.text.split("\n").length * 9;

        if (infoBook.getMod().getModHelpers().getMinecraftHelpers().isClientSide()) {
            calculateLines();
        }
    }

    @Override
    protected int getOffsetY() {
        return OFFSET_Y;
    }

    @Override
    protected int getWidth() {
        return this.maxWidth == 0 ? 120 : this.maxWidth;
    }

    @Override
    protected int getHeight() {
        return height;
    }

    @Override
    public TextFieldAppendixClient constructSectionAppendixClient() {
        return new TextFieldAppendixClient(this);
    }

    protected void calculateLines() {
        Font font = Minecraft.getInstance().font;
        StringSplitter stringSplitter = font.getSplitter();
        this.lines = Lists.newArrayList();
        stringSplitter.splitLines(this.text, this.getWidth(), Style.EMPTY, true, (style, startPos, endPos) -> {
            String stringPart = this.text.substring(startPos, endPos);
            String line = StringUtils.stripEnd(stringPart, " \n");
            lines.add(line);
            this.maxWidth = (int) Math.max(this.maxWidth, font.width(line) * this.scale);
        });
        this.height = (int) (this.scale * lines.size() * font.lineHeight);
    }

    @Override
    public void preBakeElement(InfoSection infoSection) {

    }

    @Override
    public void bakeElement(InfoSection infoSection) {

    }

    public List<String> getLines() {
        return lines;
    }

    public double getScale() {
        return scale;
    }
}
