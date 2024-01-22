package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.StringSplitter;
import net.minecraft.network.chat.Style;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.commons.lang3.StringUtils;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

import java.util.List;

/**
 * Text fields that can be added to sections.
 * @author rubensworks
 */
public class TextFieldAppendix extends SectionAppendix {

    private static final int OFFSET_Y = 0;

    private final String text;
    private final double scale;
    private int height;
    private int maxWidth;
    private List<String> lines = null;

    public TextFieldAppendix(IInfoBook infoBook, String text, double scale) {
        super(infoBook);
        this.text = text;
        this.scale = scale;
        this.height = this.text.split("\n").length * 9; // Set temp value
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
    @OnlyIn(Dist.CLIENT)
    protected void drawElement(ScreenInfoBook gui, PoseStack matrixStack, int x, int y, int width, int height, int page, int mx, int my) {
        if (this.lines == null) {
            StringSplitter stringSplitter = gui.getFont().getSplitter();
            this.lines = Lists.newArrayList();
            stringSplitter.splitLines(this.text, this.getWidth(), Style.EMPTY, true, (style, startPos, endPos) -> {
                String stringPart = this.text.substring(startPos, endPos);
                String line = StringUtils.stripEnd(stringPart, " \n");
                lines.add(line);
                this.maxWidth = (int) Math.max(this.maxWidth, gui.getFont().width(line) * this.scale);
            });
            this.height = (int) (this.scale * lines.size() * gui.getFont().lineHeight);
        }

        int lineId = 0;
        for (String line : lines) {
            RenderHelpers.drawScaledString(
                    matrixStack,
                    gui.getFont(),
                    line,
                    x,
                    (int) (y + (((float) lineId) * gui.getFont().lineHeight * this.scale)),
                    (float) this.scale,
                    Helpers.RGBToInt(10, 10, 10)
            );
            lineId++;
        }

        gui.drawOuterBorder(matrixStack, x - 1, y - 1, getWidth() + 2, getHeight() + 2, 0.5F, 0.5F, 0.5F, 0.4f);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void postDrawElement(ScreenInfoBook gui, PoseStack matrixStack, int x, int y, int width, int height, int page, int mx, int my) {

    }

    @Override
    public void preBakeElement(InfoSection infoSection) {

    }

    @Override
    public void bakeElement(InfoSection infoSection) {

    }
}
