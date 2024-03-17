package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.locale.Language;
import net.minecraft.network.chat.FormattedText;
import net.minecraft.network.chat.Style;
import net.minecraft.util.FormattedCharSequence;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.infobook.pageelement.SectionAppendix;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Section of the info book.
 * Can have child sections.
 * @author rubensworks
 */
public class InfoSection {

    //public static final int Y_OFFSET = 16;
    private static final int TITLE_LINES = 4;
    private static final int APPENDIX_OFFSET_LINE = 1;
    private static final int LINK_INDENT = 8;

    @Getter private final IInfoBook infoBook;
    private InfoSection parent;
    private int childIndex;
    @Getter private String translationKey;
    private List<InfoSection> sections = Lists.newLinkedList();
    private List<List<HyperLink>> links = Lists.newLinkedList();
    private List<SectionAppendix> appendixes;
    protected List<String> paragraphs;
    private ArrayList<String> tagList;
    private int pages;
    private List<List<FormattedCharSequence>> localizedPages;
    private Map<Integer, List<AdvancedButton>> advancedButtons = Maps.newHashMap();

    public InfoSection(IInfoBook infoBook, InfoSection parent, int childIndex, String translationKey,
                       List<String> paragraphs, List<SectionAppendix> appendixes, ArrayList<String> tagList) {
        this.infoBook = infoBook;
        this.parent = parent;
        this.childIndex = childIndex;
        this.translationKey = translationKey;
        this.paragraphs = paragraphs;
        this.appendixes = appendixes;
        this.tagList = tagList;
    }

    public String getRelativeWebPath() {
        if (isRoot()) {
            return "";
        } else {
            String suffix = getSubSections() > 0 ? "/" : ".html";
            return getParent().getRelativeWebPath() + getTranslationKey().substring(getTranslationKey().lastIndexOf('.') + 1) + suffix;
        }
    }

    /**
     * Add all links from the given map to this section, starting from page 0.
     * @param maxLines The maximum amount of lines per page.
     * @param lineHeight The line height.
     * @param yOffset The y gui offset.
     * @param softLinks The map of links.
     */
    protected void addLinks(int maxLines, int lineHeight, int yOffset, Map<String, Pair<InfoSection, Integer>> softLinks) {
        int linesOnPage = 0;
        if(isTitlePage(0)) {
            linesOnPage += TITLE_LINES;
        }
        List<HyperLink> pageLinks = Lists.newArrayListWithCapacity(maxLines);
        StringBuilder lines = new StringBuilder();
        for(Map.Entry<String, Pair<InfoSection, Integer>> entry : softLinks.entrySet()) {
            lines.append(" \n");
            linesOnPage++;
            if(linesOnPage >= maxLines) {
                linesOnPage = 0;
                links.add(pageLinks);
                pageLinks = Lists.newArrayListWithCapacity(maxLines);
            }
            pageLinks.add(new HyperLink(entry.getValue().getRight(), yOffset + (linesOnPage - 1) * lineHeight, entry.getValue().getLeft(), entry.getKey()));
        }
        paragraphs.add(lines.toString());
        links.add(pageLinks);
    }

    protected boolean shouldAddIndex() {
        return true;
    }

    protected static void constructAllLinks(InfoSection root, Map<String, Pair<InfoSection, Integer>> softLinks, int indent, int maxDepth) {
        for(InfoSection section : root.sections) {
            softLinks.put(section.getTranslationKey(), Pair.of(section, indent));
            if(maxDepth - 1 > 0) {
                constructAllLinks(section, softLinks, indent + LINK_INDENT, maxDepth - 1);
            }
        }
    }

    /**
     * Will make a localized version of this section with a variable amount of paragraphs.
     * Must be called once before the section will be drawn.
     * @param fontRenderer The font renderer.
     * @param width Section width
     * @param maxLines The maximum amount of lines per page.
     * @param lineHeight The line height.
     * @param yOffset The y gui offset.
     */
    public void bakeSection(Font fontRenderer, int width, int maxLines, int lineHeight, int yOffset) {
        if(paragraphs.size() == 0 && shouldAddIndex()) {
            // linkedmap to make sure the contents are sorted by insertion order.
            Map<String, Pair<InfoSection, Integer>> softLinks = Maps.newLinkedHashMap();
            constructAllLinks(this, softLinks, 0, 2);
            addLinks(maxLines, lineHeight, yOffset, softLinks);
        }

        // Localize paragraphs and fit them into materialized paragraphs.
        String contents = "";
        for(Iterator<String> it = paragraphs.iterator(); it.hasNext();) {
            String paragraph = it.next();
            contents += formatString(L10NHelpers.localize(paragraph)) + (it.hasNext() ? "\n\n" : "");
        }

        // Wrap the text into pages.
        List<FormattedCharSequence> allLines = trimStringToWidth(fontRenderer, FormattedText.of(contents), width);
        localizedPages = Lists.newLinkedList();
        int linesOnPage = 0;
        List<FormattedCharSequence> currentPage = Lists.newArrayList();
        if(isTitlePage(0)) {
            for(int i = 1; i < TITLE_LINES; i++) currentPage.add(FormattedCharSequence.forward("", Style.EMPTY)); // Make a blank space for the section title.
            linesOnPage += TITLE_LINES;
        }
        pages = 1;
        for(FormattedCharSequence line : allLines) {
            if(linesOnPage >= maxLines) {
                linesOnPage = 0;
                pages++;
                localizedPages.add(currentPage);
                currentPage = Lists.newArrayList();
            }
            linesOnPage++;
            currentPage.add(line);
        }
        localizedPages.add(currentPage);

        linesOnPage += APPENDIX_OFFSET_LINE;

        // Distribute appendixes among pages.
        Map<Integer, List<SectionAppendix>> appendixesPerPage = Maps.newHashMap();
        List<SectionAppendix> appendixCurrentPage = Lists.newLinkedList();
        int appendixPageStart = pages - 1;
        int appendixLineStart = linesOnPage;
        for(SectionAppendix appendix : appendixes) {
            int lines = getAppendixLineHeight(appendix, fontRenderer);
            if(linesOnPage + lines > maxLines) {
                appendixesPerPage.put(pages - 1, appendixCurrentPage);
                pages++;
                linesOnPage = 0;
                appendixCurrentPage = Lists.newLinkedList();
            }
            // Start line will be set in a later iteration.
            //appendix.setLineStart(linesOnPage);
            appendixCurrentPage.add(appendix);
            appendix.setPage(pages - 1);
            linesOnPage += lines + APPENDIX_OFFSET_LINE;
        }
        appendixesPerPage.put(pages - 1, appendixCurrentPage);

        // Loop over each page to determine optimal vertical float positioning of appendixes.
        for(Map.Entry<Integer, List<SectionAppendix>> entry : appendixesPerPage.entrySet()) {
            int freeLines = maxLines;
            int lineStart = 0;

            // Special case if appendixes occurs on a page that still has text content, so this needs an offset.
            if(entry.getKey() == appendixPageStart) {
                lineStart = appendixLineStart;
                freeLines -= appendixLineStart;
            }

            // Count total lines that are free.
            for(SectionAppendix appendix : entry.getValue()) {
                freeLines -= getAppendixLineHeight(appendix, fontRenderer);
            }

            // Distribute the free lines among all appendixes on this page.
            int linesOffset = freeLines / (entry.getValue().size() + 1);
            int linesOffsetMod = freeLines % (entry.getValue().size() + 1);
            lineStart += linesOffset;
            for(SectionAppendix appendix : entry.getValue()) {
                appendix.setLineStart(lineStart);
                lineStart += linesOffset + getAppendixLineHeight(appendix, fontRenderer) + (linesOffsetMod > 0 ? linesOffsetMod-- : 0);
            }
        }

        // Bake appendix contents
        advancedButtons.clear();
        for(SectionAppendix appendix : appendixes) {
            appendix.preBakeElement(this);
            appendix.bakeElement(this);
        }
    }

    // Adapted from Font#trimStringToWidth
    // The problem with the vanilla implementation is that it doesn't handle formatting well across multiple lines, see https://github.com/CyclopsMC/IntegratedDynamics/issues/1078
    // What we do here, is clear the forced Style's on each line, and instead apply the raw string-based formatting codes directly.
    protected static List<FormattedCharSequence> trimStringToWidth(Font fontRenderer, FormattedText text, int width) {
        List<FormattedText> textLines = fontRenderer.getSplitter().splitLines(text, width, Style.EMPTY);
        List<FormattedCharSequence> formattedLines = Lists.newArrayList();

        // Keep a memory of active formatting flags, which persists across lines
        Map<Character, Boolean> activeFlags = Maps.newHashMap();
        Character activeColor = null;
        for (FormattedText textLine : textLines) {
            String textLineRaw = textLine.getString();

            // Clear the Style formatting, and instead prepend the currently active raw string-based formatting codes.
            Character finalActiveColor = activeColor;
            Optional<FormattedText> textLineUnformatted = textLine.visit((style, string) -> {
                if (!activeFlags.isEmpty()) {
                    string = activeFlags.keySet().stream().map(character -> "§" + character).collect(Collectors.joining()) + string;
                }
                if (finalActiveColor != null) {
                    string = "§" + finalActiveColor + string;
                }
                return Optional.of(FormattedText.of(string, Style.EMPTY));
            }, Style.EMPTY);
            if (textLineUnformatted.isPresent()) {
                textLine = textLineUnformatted.get();
            }
            formattedLines.add(Language.getInstance().getVisualOrder(textLine));

            // Loop over each character of the current line, and save the active formatting flags in memory, so that they can be re-applied on the next line.
            for (int charPos = 0; charPos < textLineRaw.length(); charPos++) {
                if (textLineRaw.charAt(charPos) == '§') {
                    charPos++;
                    char character = textLineRaw.charAt(charPos);
                    if (Character.isDigit(character) || character == 'a' || character == 'b' || character == 'c' || character == 'e' || character == 'f') {
                        if (character == '0') {
                            activeColor = null;
                        } else {
                            activeColor = character;
                        }
                    } else if (character == 'r') {
                        activeFlags.clear();
                    } else {
                        activeFlags.put(character, true);
                    }
                }
            }
        }

        return formattedLines;
    }

    protected static int getAppendixLineHeight(SectionAppendix appendix, Font fontRenderer) {
        return (int) Math.ceil((double) appendix.getFullHeight() / (double) getFontHeight(fontRenderer));
    }

    public static int getFontHeight(Font fontRenderer) {
        return fontRenderer.lineHeight;
    }

    public boolean isTitlePage(int page) {
        return this.getTranslationKey() != null && page == 0;
    }

    public void registerSection(InfoSection section) {
        sections.add(section);
        section.childIndex = sections.size() - 1;
    }

    public int getPages() {
        return pages;
    }

    /**
     * Give the correct format to a string.
     * Will allow the convenient "&amp;" format codes to be used instead of "§": http://minecraft.gamepedia.com/Formatting_codes
     * Will also refresh all formats at the end of the string.
     * This will replace "&amp;N"'s with a newlines.
     * @param string The string to format.
     * @return The formatted string.
     */
    public static String formatString(String string) {
        return (string + "&r").replaceAll("&N", "\n").replaceAll("&", "§");
    }

    protected List<FormattedCharSequence> getLocalizedPageLines(int page) {
        if(page >= localizedPages.size() || page < 0) return null;
        return localizedPages.get(page);
    }

    public String getLocalizedTitle() {
        return formatString(L10NHelpers.localize(translationKey));
    }

    public int getSubSections() {
        return sections.size();
    }

    public InfoSection getSubSection(int index) {
        return sections.get(index);
    }

    public InfoSection getParent() {
        return this.parent;
    }

    public boolean isRoot() {
        return getParent() == null;
    }

    public int getChildIndex() {
        return this.childIndex;
    }

    public List<HyperLink> getLinks(int page) {
        if(links.size() <= page || page < 0) return Collections.emptyList();
        return links.get(page);
    }

    /**
     * Draw the screen for a given page.
     * @param gui The gui.
     * @param guiGraphics The gui graphics object
     * @param mouseX X.
     * @param mouseY Y.
     * @param yOffset The y offset.
     * @param width The width of the page.
     * @param height The height of the page.
     * @param page The page to render.
     * @param mx Mouse X.
     * @param my Mouse Y.
     * @param footnoteOffsetX Footnote offset x
     * @param footnoteOffsetY Footnote offset y
     */
    @OnlyIn(Dist.CLIENT)
    public void drawScreen(ScreenInfoBook gui, GuiGraphics guiGraphics, int mouseX, int mouseY, int yOffset, int width, int height, int page, int mx, int my, int footnoteOffsetX, int footnoteOffsetY) {
        if(page < getPages()) {
            Font fontRenderer = gui.getFont();

            // Draw text content
            List<FormattedCharSequence> lines = getLocalizedPageLines(page);
            int l = 0;
            if (lines != null) {
                for (FormattedCharSequence line : lines) {
                    fontRenderer.drawInBatch(line, mouseX, mouseY + yOffset + l * 9, 0, false,
                            guiGraphics.pose().last().pose(), guiGraphics.bufferSource(), Font.DisplayMode.NORMAL, 0, 15728880);
                    l++;
                }
            }

            // Draw title if on first page
            if (isTitlePage(page)) {
                gui.drawScaledCenteredString(guiGraphics, getLocalizedTitle(), mouseX, mouseY + yOffset + 10, width, 1.5f, width, gui.getTitleColor());
                gui.drawHorizontalRule(guiGraphics, mouseX + width / 2, mouseY + yOffset);
                gui.drawHorizontalRule(guiGraphics, mouseX + width / 2, mouseY + yOffset + 21);
            }

            // Draw current page/section indication
            gui.drawScaledCenteredString(guiGraphics, getLocalizedTitle() + " - " + (page + 1) +  "/" + getPages(), mouseX + (((page % 2 == 0) ? 1 : -1) * footnoteOffsetX), mouseY + height + footnoteOffsetY, width, 0.6f, (int) (width * 0.75f), Helpers.RGBToInt(190, 190, 190));

            // Draw appendixes
            for (SectionAppendix appendix : appendixes) {
                if (appendix.getPage() == page) {
                    appendix.drawScreen(gui, guiGraphics, mouseX, mouseY + yOffset + getAppendixOffsetLine(fontRenderer, appendix),
                            width, height, page, mx, my, true);
                }
            }
        }
    }

    /**
     * Draw the overlays for the given page, for tooltips and such.
     * @param gui The gui.
     * @param guiGraphics The gui graphics object.
     * @param mouseX X.
     * @param mouseY Y.
     * @param width The width of the page.
     * @param height The height of the page.
     * @param page The page to render.
     * @param mx Mouse X.
     * @param my Mouse Y.
     */
    @OnlyIn(Dist.CLIENT)
    public void postDrawScreen(ScreenInfoBook gui, GuiGraphics guiGraphics, int mouseX, int mouseY, int width, int height, int page, int mx, int my) {
        if(page < getPages()) {
            Font fontRenderer = gui.getFont();
            // Post draw appendixes
            for (SectionAppendix appendix : appendixes) {
                if (appendix.getPage() == page) {
                    appendix.drawScreen(gui, guiGraphics, mouseX, mouseY + getAppendixOffsetLine(fontRenderer, appendix),
                            width, height, page, mx, my, false);
                }
            }
        }
    }

    /**
     * Get the next InfoSection relative to this one plus page in this tree hierarchy using pre-order traversal.
     * @param page The current page.
     * @param stepSection Take a complete section as traversal step.
     * @return The next location or the current location if this was the last location.
     */
    public InfoSection.Location getNext(int page, boolean stepSection) {
        if(page < getPages() - 1 && !stepSection) {
            return new InfoSection.Location(page + 1, this);
        } else if(getSubSections() > 0) {
            return new InfoSection.Location(0, getSubSection(0));
        } else {
            InfoSection current = this;
            while(!current.isRoot()) {
                if (current.getChildIndex() < current.getParent().getSubSections() - 1) {
                    return new Location(0, current.getParent().getSubSection(current.getChildIndex() + 1));
                }
                current = current.getParent();
            }
        }
        return new InfoSection.Location(page, this);
    }

    /**
     * Get the previous InfoSection relative to this one plus page in this tree hierarchy using pre-order traversal.
     * @param page The current page.
     * @param stepSection Take a complete section as traversal step.
     * @return The previous location or the current location if this was the last location.
     */
    public InfoSection.Location getPrevious(int page, boolean stepSection) {
        if(page > 0) {
            return new InfoSection.Location(stepSection ? 0 : page - getInfoBook().getPagesPerView(), this);
        } else if(!isRoot() && getChildIndex() == 0) {
            return new InfoSection.Location(0, getParent());
        } else if(!isRoot() && getChildIndex() > 0) {
            InfoSection current = getParent().getSubSection(getChildIndex() - 1);
            while(current.getSubSections() > 0) {
                current = current.getSubSection(current.getSubSections() - 1);
            }
            return new InfoSection.Location(0, current);
        }
        return new InfoSection.Location(page, this);
    }

    protected static int getAppendixOffsetLine(Font fontRenderer, SectionAppendix appendix) {
        return getFontHeight(fontRenderer) * appendix.getLineStart();
    }

    public ArrayList<String> getTags() {
        return tagList;
    }

    public List<AdvancedButton> getAdvancedButtons(int page) {
        if(!advancedButtons.containsKey(page)) {
            return Collections.emptyList();
        }
        return advancedButtons.get(page);
    }

    public void addAdvancedButton(int page, AdvancedButton advancedButton) {
        if(!advancedButtons.containsKey(page)) {
            advancedButtons.put(page, Lists.<AdvancedButton>newLinkedList());
        }
        advancedButtons.get(page).add(advancedButton);
    }

    public <T extends AdvancedButton> void addAdvancedButtons(int page, Collection<T> advancedButtons) {
        for(AdvancedButton advancedButton : advancedButtons) addAdvancedButton(page, advancedButton);
    }

    @Data @AllArgsConstructor public static class Location {

        private int page;
        private InfoSection infoSection;

        @Override
        public boolean equals(Object o) {
            if(o instanceof Location) {
                return ((Location) o).page == this.page && ((Location) o).infoSection == this.infoSection;
            }
            return false;
        }

        @Override
        public int hashCode() {
            return infoSection.hashCode() >> 4 & page;
        }

    }

}
