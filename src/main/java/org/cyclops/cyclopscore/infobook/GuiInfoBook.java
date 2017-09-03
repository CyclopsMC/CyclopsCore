package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Lists;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.helper.*;
import org.cyclops.cyclopscore.network.packet.RequestPlayerNbtPacket;
import org.lwjgl.opengl.GL11;

import java.io.IOException;
import java.util.List;

/**
 * Base gui for {@link IInfoBook}.
 * @author rubensworks
 */
public abstract class GuiInfoBook extends GuiScreen {

    private static final int BUTTON_NEXT = 1;
    private static final int BUTTON_PREVIOUS = 2;
    private static final int BUTTON_PARENT = 3;
    private static final int BUTTON_BACK = 4;
    private static final int BUTTON_HYPERLINKS_START = 5;

    private static final int HR_WIDTH = 88;
    private static final int HR_HEIGHT = 10;
    private static final int BANNER_WIDTH = 91;
    private static final int BANNER_HEIGHT = 12;
    private static final int ARROW_WIDTH = 22;
    private static final int ARROW_HEIGHT = 13;

    private static final int BORDER_CORNER = 4;
    private static final int BORDER_WIDTH = 2;
    private static final int BORDER_X = 0;
    private static final int BORDER_Y = 206;

    protected final IInfoBook infoBook;
    protected final ItemStack itemStack;
    protected final ResourceLocation texture;

    protected NextPageButton buttonNextPage;
    protected NextPageButton buttonPreviousPage;
    protected NextPageButton buttonParent;
    protected NextPageButton buttonBack;

    private InfoSection nextSection;
    private int nextPage;
    private boolean goToLastPage;

    private int left, top;

    public GuiInfoBook(EntityPlayer player, int itemIndex, IInfoBook infoBook, ResourceLocation texture) {
        itemStack = InventoryHelpers.getItemFromIndex(player, itemIndex);
        this.infoBook = infoBook;
        this.texture = texture;
        if(infoBook.getCurrentSection() == null) {
            InfoSection root = infoBook.getMod().getRegistryManager().getRegistry(IInfoBookRegistry.class).getRoot(infoBook);
            if (root == null) {
                throw new IllegalStateException("Could not find the root of infobook " + infoBook);
            }
            infoBook.setCurrentSection(root);
            infoBook.setCurrentPage(0);
        }

        // Request an up-to-date persisted player NBT tag to make sure our advancement reward status is synced.
        CyclopsCore._instance.getPacketHandler().sendToServer(new RequestPlayerNbtPacket());
    }

    /**
     * @return The amount of pages to show at once.
     */
    protected int getPages() {
        return infoBook.getPagesPerView();
    }
    protected abstract int getGuiWidth();
    protected abstract int getGuiHeight();
    protected abstract int getPageWidth();

    protected int getPageYOffset() {
        return 16;
    }

    protected int getFootnoteOffsetX() {
        return 10;
    }

    protected int getFootnoteOffsetY() {
        return 0;
    }

    protected int getPrevNextOffsetY() {
        return 0;
    }

    protected int getPrevNextOffsetX() {
        return 0;
    }

    public int getTitleColor() {
        return Helpers.RGBToInt(120, 20, 30);
    }

    @Override
    public void initGui() {
        this.buttonList.clear();
        super.initGui();

        left = (width - getGuiWidth()) / 2;
        top = (height - getGuiHeight()) / 2;

        this.buttonList.add(this.buttonNextPage = new NextPageButton(BUTTON_NEXT, left + getPageWidth() + 100 + getPrevNextOffsetX(), top + 156 + getPrevNextOffsetY(), 0, 180, 18, 13, this));
        this.buttonList.add(this.buttonPreviousPage = new NextPageButton(BUTTON_PREVIOUS, left + 23 - getPrevNextOffsetX(), top + 156 + getPrevNextOffsetY(), 0, 193, 18, 13, this));
        this.buttonList.add(this.buttonParent = new NextPageButton(BUTTON_PARENT, left + 2, top + 2, 36, 180, 8, 8, this));
        this.buttonList.add(this.buttonBack = new NextPageButton(BUTTON_BACK, left + getPageWidth() + 127, top + 2, 0, 223, 13, 18, this));
        this.updateGui();

        if (goToLastPage) {
            int page = Math.max(0, infoBook.getCurrentSection().getPages() - getPages());
            page += page % getPages();
            infoBook.setCurrentPage(page);
        }

        int nextId = BUTTON_HYPERLINKS_START;
        int page = infoBook.getCurrentPage();
        for(int innerPage = page; innerPage <= page + getPages() - 1; innerPage++) {
            for (HyperLink link : infoBook.getCurrentSection().getLinks(innerPage)) {
                int xOffset = getOffsetXForPageWithWidths(innerPage % getPages());
                this.buttonList.add(new TextOverlayButton(nextId++, link, left + xOffset + link.getX(), top + getPageYOffset() / 2 + link.getY(),
                        InfoSection.getFontHeight(getFontRenderer()), this));
            }
            this.buttonList.addAll(infoBook.getCurrentSection().getAdvancedButtons(innerPage));
        }
    }

    protected abstract int getOffsetXForPageBase(int page);

    protected int getOffsetXForPageWithWidths(int page) {
        return getOffsetXForPageBase(page) + page * getPageWidth();
    }

    protected int getOffsetXTotal() {
        int total = 0;
        for(int i = 0; i < getPages(); i++) {
            total += getOffsetXForPageBase(i);
        }
        return total;
    }

    @Override
    public void drawScreen(int x, int y, float f) {
        GlStateManager.color(1F, 1F, 1F, 1F);
        mc.renderEngine.bindTexture(texture);

        drawTexturedModalRect(left, top, 0, 0, getPageWidth(), getGuiHeight());
        drawTexturedModalRectMirrored(left + getPageWidth() - 1, top, 0, 0, getPageWidth(), getGuiHeight());
        int width = getPageWidth() - getOffsetXTotal();
        for(int i = 0; i < getPages(); i++) {
            infoBook.getCurrentSection().drawScreen(this, left + getOffsetXForPageWithWidths(i), top, getPageYOffset(), width, getGuiHeight(), infoBook.getCurrentPage() + i, x, y, getFootnoteOffsetX(), getFootnoteOffsetY());
        }
        super.drawScreen(x, y, f);
        for(int i = 0; i < getPages(); i++) {
            infoBook.getCurrentSection().postDrawScreen(this, left + getOffsetXForPageWithWidths(i), top + getPageYOffset(), width, getGuiHeight(), infoBook.getCurrentPage() + i, x, y);
        }

        if (this.buttonNextPage.visible && RenderHelpers.isPointInButton(this.buttonNextPage, x, y)) {
            drawTooltip(x, y, Lists.newArrayList(L10NHelpers.localize("infobook.cyclopscore.next_page")));
        }
        if (this.buttonPreviousPage.visible && RenderHelpers.isPointInButton(this.buttonPreviousPage, x, y)) {
            drawTooltip(x, y, Lists.newArrayList(L10NHelpers.localize("infobook.cyclopscore.previous_page")));
        }
        if (this.buttonBack.visible && RenderHelpers.isPointInButton(this.buttonBack, x, y)) {
            drawTooltip(x, y, Lists.newArrayList(L10NHelpers.localize("infobook.cyclopscore.last_page")));
        }
        if (this.buttonParent.visible && RenderHelpers.isPointInButton(this.buttonParent, x, y)) {
            drawTooltip(x, y, Lists.newArrayList(L10NHelpers.localize("infobook.cyclopscore.parent_section")));
        }
    }

    public void drawTooltip(int mx, int my, List<String> lines) {
        GlStateManager.pushMatrix();
        drawHoveringText(lines, mx, my);
        GlStateManager.popMatrix();

        GlStateManager.disableLighting();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void drawTexturedModalRectMirrored(int x, int y, int u, int v, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos((double) (x + 0), (double) (y + height), (double) this.zLevel).tex((double) ((float) (u + width) * f), (double) ((float) (v + height) * f1)).endVertex();
        worldRenderer.pos((double) (x + width), (double) (y + height), (double) this.zLevel).tex((double) ((float) (u + 0) * f), (double) ((float) (v + height) * f1)).endVertex();
        worldRenderer.pos((double) (x + width), (double) (y + 0), (double) this.zLevel).tex((double) ((float) (u + 0) * f), (double) ((float) (v + 0) * f1)).endVertex();
        worldRenderer.pos((double) (x + 0), (double) (y + 0), (double) this.zLevel).tex((double) ((float) (u + width) * f), (double) ((float) (v + 0) * f1)).endVertex();
        tessellator.draw();
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }

    public FontRenderer getFontRenderer() {
        return this.fontRenderer;
    }

    public int getBannerWidth() {
        return BANNER_WIDTH;
    }

    private void updateGui() {
        boolean oldUnicode = mc.fontRenderer.getUnicodeFlag();
        mc.fontRenderer.setUnicodeFlag(true);
        int width = getPageWidth() - getOffsetXTotal();
        int lineHeight = InfoSection.getFontHeight(getFontRenderer());
        int maxLines = (getGuiHeight() - 2 * getPageYOffset() - 5) / lineHeight;

        // Bake current and all reachable sections.
        List<InfoSection> infoSectionsToBake = Lists.newLinkedList();
        infoSectionsToBake.add(infoBook.getCurrentSection());
        getPreviousSections(infoSectionsToBake);
        getNextSections(infoSectionsToBake);
        for(InfoSection infoSection : infoSectionsToBake) {
            if(infoSection != null) infoSection.bakeSection(getFontRenderer(), width, maxLines, lineHeight, getPageYOffset());
        }

        updateButtons();
        mc.fontRenderer.setUnicodeFlag(oldUnicode);
    }

    protected void getPreviousSections(List<InfoSection> sections) {
        InfoSection.Location location = infoBook.getCurrentSection().getPrevious(infoBook.getCurrentPage(), false);
        if(location != null) {
            sections.add(location.getInfoSection());
        }
    }

    protected void getNextSections(List<InfoSection> sections) {
        InfoSection.Location location = infoBook.getCurrentSection().getNext(infoBook.getCurrentPage() + getPages() - 1, false);
        if(location != null) {
            sections.add(location.getInfoSection());
        }
    }

    private void updateButtons() {
        InfoSection.Location current1 = new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection());
        InfoSection.Location current2 = new InfoSection.Location(infoBook.getCurrentPage() + getPages() - 1, infoBook.getCurrentSection());
        InfoSection.Location wouldBeNext = infoBook.getCurrentSection().getNext(infoBook.getCurrentPage() + getPages() - 1, false);
        this.buttonNextPage.visible = !current1.equals(wouldBeNext) && !current2.equals(wouldBeNext);
        this.buttonPreviousPage.visible = !current1.equals(infoBook.getCurrentSection().getPrevious(infoBook.getCurrentPage(), false));
        this.buttonParent.visible = infoBook.getCurrentSection() != null && infoBook.getCurrentSection().getParent() != null;
        this.buttonBack.visible = infoBook.getHistory().currentSize() > 0;
    }

    protected void actionPerformed(GuiButton button) throws IOException {
        goToLastPage = false;
        nextSection = infoBook.getCurrentSection();
        nextPage = infoBook.getCurrentPage();
        if(button.id == BUTTON_NEXT && button.visible) {
            InfoSection.Location location = infoBook.getCurrentSection().getNext(infoBook.getCurrentPage() + getPages() - 1, MinecraftHelpers.isShifted());
            nextSection = location.getInfoSection();
            nextPage = location.getPage();
            infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));
        } else if(button.id == BUTTON_PREVIOUS && button.visible) {
            InfoSection.Location location = infoBook.getCurrentSection().getPrevious(infoBook.getCurrentPage(), MinecraftHelpers.isShifted());
            nextSection = location.getInfoSection();
            nextPage = location.getPage();
            // We can not set the new 'page', because the infoBook.getCurrentSection() hasn't been baked yet and we do not know the last page yet.
            goToLastPage = nextSection != infoBook.getCurrentSection() && !MinecraftHelpers.isShifted();
            infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));
        } else if(button.id == BUTTON_PARENT && button.visible) {
            if(MinecraftHelpers.isShifted()) {
                nextSection = infoBook.getCurrentSection().getParent();
                while(nextSection.getParent() != null) {
                    nextSection = nextSection.getParent();
                }
            } else {
                nextSection = infoBook.getCurrentSection().getParent();
            }
            nextPage = 0;
            infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));
        } else if(button.id == BUTTON_BACK && button.visible && infoBook.getHistory().currentSize() > 0) {
            InfoSection.Location location = infoBook.getHistory().pop();
            nextSection = location.getInfoSection();
            nextPage = location.getPage();
        } else if(button instanceof TextOverlayButton) {
            nextSection = ((TextOverlayButton) button).getLink().getTarget();
            nextPage = 0;
            if(nextSection != infoBook.getCurrentSection()) infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));
        } else if(button instanceof AdvancedButton && ((AdvancedButton) button).isVisible()) {
            nextSection = ((AdvancedButton) button).getTarget();
            nextPage = 0;
            if(nextSection != infoBook.getCurrentSection()) infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));
            ((AdvancedButton) button).onClick();
        } else {
            super.actionPerformed(button);
        }
    }

    protected void mouseClicked(int x, int y, int p_73864_3_) throws IOException {
        super.mouseClicked(x, y, p_73864_3_);
        if(p_73864_3_ == 0 && (nextSection != null && (nextSection != infoBook.getCurrentSection() || infoBook.getCurrentPage() != nextPage))) {
            infoBook.setCurrentSection(nextSection);
            nextSection = null;
            infoBook.setCurrentPage(nextPage);
            this.initGui();
        }
    }

    public void drawScaledCenteredString(String string, int x, int y, int width, float originalScale, int maxWidth, int color) {
        float originalWidth = getFontRenderer().getStringWidth(string) * originalScale;
        float scale = Math.min(originalScale, maxWidth / originalWidth * originalScale);
        drawScaledCenteredString(string, x, y, width, scale, color);
    }

    public void drawScaledCenteredString(String string, int x, int y, int width, float scale, int color) {
        GlStateManager.pushMatrix();
        GlStateManager.scale(scale, scale, 1.0f);
        int titleLength = fontRenderer.getStringWidth(string);
        int titleHeight = fontRenderer.FONT_HEIGHT;
        fontRenderer.drawString(string, Math.round((x + width / 2) / scale - titleLength / 2), Math.round(y / scale - titleHeight / 2), color);
        GlStateManager.popMatrix();
    }

    public void drawHorizontalRule(int x, int y) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        this.drawTexturedModalRect(x - HR_WIDTH / 2, y - HR_HEIGHT / 2, 52, 180, HR_WIDTH, HR_HEIGHT);
        GlStateManager.disableBlend();
    }

    public void drawTextBanner(int x, int y) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        this.drawTexturedModalRect(x - BANNER_WIDTH / 2, y - BANNER_HEIGHT / 2, 52, 191, BANNER_WIDTH, BANNER_HEIGHT);
        GlStateManager.disableBlend();
    }

    public void drawArrowRight(int x, int y) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
        mc.getTextureManager().bindTexture(texture);
        this.drawTexturedModalRect(x, y, 0, 210, ARROW_WIDTH, ARROW_HEIGHT);
        GlStateManager.disableBlend();
    }

    public void drawOuterBorder(int x, int y, int width, int height) {
        drawOuterBorder(x, y, width, height, 1, 1, 1, 1);
    }

    public void drawOuterBorder(int x, int y, int width, int height, float r, float g, float b, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color(r, g, b, alpha);
        mc.getTextureManager().bindTexture(texture);

        // Corners
        this.drawTexturedModalRect(x - BORDER_WIDTH, y - BORDER_WIDTH, BORDER_X, BORDER_Y, BORDER_CORNER, BORDER_CORNER);
        this.drawTexturedModalRect(x + width - BORDER_WIDTH, y - BORDER_WIDTH, BORDER_X + BORDER_CORNER, BORDER_Y, BORDER_CORNER, BORDER_CORNER);
        this.drawTexturedModalRect(x - BORDER_WIDTH, y + height - BORDER_WIDTH, BORDER_X + 3 * BORDER_CORNER, BORDER_Y, BORDER_CORNER, BORDER_CORNER);
        this.drawTexturedModalRect(x + width - BORDER_WIDTH, y + height - BORDER_WIDTH, BORDER_X + 2 * BORDER_CORNER, BORDER_Y, BORDER_CORNER, BORDER_CORNER);

        // Sides
        for(int i = BORDER_WIDTH; i < width - BORDER_WIDTH; i+=BORDER_WIDTH) {
            int drawWidth = BORDER_WIDTH;
            if(i + BORDER_WIDTH >= width - BORDER_CORNER) {
                drawWidth -= i - (width - BORDER_CORNER);
            }
            this.drawTexturedModalRect(x + i, y - BORDER_WIDTH, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, drawWidth, BORDER_WIDTH);
            this.drawTexturedModalRect(x + i, y + height, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, drawWidth, BORDER_WIDTH);
        }
        for(int i = BORDER_WIDTH; i < height - BORDER_WIDTH; i+=BORDER_WIDTH) {
            int drawHeight = BORDER_WIDTH;
            if(i + BORDER_WIDTH >= height - BORDER_CORNER) {
                drawHeight -= i - (height - BORDER_CORNER);
            }
            if(drawHeight > 0) {
                this.drawTexturedModalRect(x - BORDER_WIDTH, y + i, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, BORDER_WIDTH, drawHeight);
                this.drawTexturedModalRect(x + width, y + i, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, BORDER_WIDTH, drawHeight);
            }
        }

        GlStateManager.color(1, 1, 1, 1);
    }

    public void renderToolTip(ItemStack itemStack, int x, int y) {
        super.renderToolTip(itemStack, x, y);
    }

    @Override
    public void drawHoveringText(List<String> textLines, int x, int y) {
        super.drawHoveringText(textLines, x, y);
    }

    public int getTick() {
        return (int) mc.world.getWorldTime();
    }

    @Override
    public void updateScreen() {
        super.updateScreen();

        if(!this.mc.player.isEntityAlive() || this.mc.player.isDead) {
            this.mc.player.closeScreen();
        }
    }

    public abstract void playPageFlipSound(SoundHandler soundHandler);
    public abstract void playPagesFlipSound(SoundHandler soundHandler);

    @SideOnly(Side.CLIENT)
    static class NextPageButton extends GuiButton {

        private final GuiInfoBook guiInfoBook;
        private int textureX, textureY;

        public NextPageButton(int id, int x, int y, int textureX, int textureY, int width, int height, GuiInfoBook guiInfoBook) {
            super(id, x, y, width, height, "");
            this.textureX = textureX;
            this.textureY = textureY;
            this.guiInfoBook = guiInfoBook;
        }

        @Override
        public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                boolean isHover = mouseX >= this.x && mouseY >= this.y &&
                               mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                minecraft.getTextureManager().bindTexture(guiInfoBook.texture);
                int k = textureX;
                int l = textureY;

                if (isHover) {
                    k += width;
                }

                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                this.drawTexturedModalRect(this.x, this.y, k, l, width, height);
                GlStateManager.disableBlend();
            }
        }

        @Override
        public void playPressSound(SoundHandler soundHandler) {
            guiInfoBook.playPageFlipSound(soundHandler);
        }

    }

    @SideOnly(Side.CLIENT)
    static class TextOverlayButton extends GuiButton {

        private final GuiInfoBook guiInfoBook;
        @Getter private HyperLink link;

        public TextOverlayButton(int id, HyperLink link, int x, int y, int height, GuiInfoBook guiInfoBook) {
            super(id, x, y, 0, height, InfoSection.formatString(L10NHelpers.localize(link.getUnlocalizedName())));
            this.guiInfoBook = guiInfoBook;
            this.link = link;
            FontRenderer fontRenderer = Minecraft.getMinecraft().fontRenderer;
            boolean oldUnicode = fontRenderer.getUnicodeFlag();
            fontRenderer.setUnicodeFlag(true);
            this.width = fontRenderer.getStringWidth(displayString);
            fontRenderer.setUnicodeFlag(oldUnicode);
        }

        @Override
        public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                boolean isHover = mouseX >= this.x && mouseY >= this.y &&
                        mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
                boolean oldUnicode = minecraft.fontRenderer.getUnicodeFlag();
                minecraft.fontRenderer.setUnicodeFlag(true);
                minecraft.fontRenderer.drawString((isHover ? "§n" : "") +
                                displayString + "§r", x, y,
                        Helpers.RGBToInt(isHover ? 100 : 0, isHover ? 100 : 0, isHover ? 150 : 125));
                minecraft.fontRenderer.setUnicodeFlag(oldUnicode);
            }
        }

        @Override
        public void playPressSound(SoundHandler soundHandler) {
            guiInfoBook.playPagesFlipSound(soundHandler);
        }

    }

}
