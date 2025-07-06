package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.pipeline.RenderPipeline;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent;
import net.minecraft.client.gui.screens.inventory.tooltip.DefaultTooltipPositioner;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.sounds.SoundManager;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.inventory.container.ContainerExtended;
import org.cyclops.cyclopscore.network.packet.RequestPlayerNbtPacket;

import java.util.List;

/**
 * Base gui for {@link IInfoBook}.
 *
 * @author rubensworks
 */
public abstract class ScreenInfoBook<T extends ContainerExtended> extends AbstractContainerScreen<T> {

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
    protected final ResourceLocation texture;

    protected NextPageButton buttonNextPage;
    protected NextPageButton buttonPreviousPage;
    protected NextPageButton buttonParent;
    protected NextPageButton buttonBack;
    protected NextPageButton buttonExternal;

    private InfoSection nextSection;
    private int nextPage;
    private boolean goToLastPage;

    private int left, top;

    public ScreenInfoBook(T container, Inventory playerInventory, Component title, IInfoBook infoBook) {
        super(container, playerInventory, title);
        this.infoBook = infoBook;
        this.texture = constructGuiTexture();
        if (infoBook.getCurrentSection() == null) {
            InfoSection root = infoBook.getMod().getRegistryManager().getRegistry(IInfoBookRegistry.class).getRoot(infoBook);
            if (root == null) {
                throw new IllegalStateException("Could not find the root of infobook " + infoBook);
            }
            infoBook.setCurrentSection(root);
            infoBook.setCurrentPage(0);
        }

        // Request an up-to-date persisted player NBT tag to make sure our advancement reward status is synced.
        CyclopsCoreNeoForge._instance.getPacketHandler().sendToServer(new RequestPlayerNbtPacket());
    }

    protected abstract ResourceLocation constructGuiTexture();

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
        return IModHelpers.get().getBaseHelpers().RGBAToInt(120, 20, 30, 255);
    }

    @Override
    public void init() {
        super.init();

        this.clearWidgets();

        left = (width - getGuiWidth()) / 2;
        top = (height - getGuiHeight()) / 2;
        imageWidth = getGuiWidth();
        imageHeight = getGuiHeight();

        this.addRenderableWidget(this.buttonNextPage = new NextPageButton(left + getPageWidth() + 100 + getPrevNextOffsetX(), top + 156 + getPrevNextOffsetY(), 0, 180, 18, 10, (button) -> {
            InfoSection.Location location = infoBook.getCurrentSection().getNext(infoBook.getCurrentPage() + getPages() - 1, IModHelpers.get().getMinecraftClientHelpers().isShifted());
            goToLastPage = false;
            nextSection = location.getInfoSection();
            nextPage = location.getPage();
            infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));
            applyNavigation();
        }, this));
        this.addRenderableWidget(this.buttonPreviousPage = new NextPageButton(left + 23 - getPrevNextOffsetX(), top + 156 + getPrevNextOffsetY(), 0, 193, 18, 10, (button) -> {
            InfoSection.Location location = infoBook.getCurrentSection().getPrevious(infoBook.getCurrentPage(), IModHelpers.get().getMinecraftClientHelpers().isShifted());
            nextSection = location.getInfoSection();
            nextPage = location.getPage();
            // We can not set the new 'page', because the infoBook.getCurrentSection() hasn't been baked yet and we do not know the last page yet.
            goToLastPage = nextSection != infoBook.getCurrentSection() && !IModHelpers.get().getMinecraftClientHelpers().isShifted();
            infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));
            applyNavigation();
        }, this));
        this.addRenderableWidget(this.buttonParent = new NextPageButton(left + 2, top + 2, 36, 180, 8, 8, (button) -> {
            goToLastPage = false;
            if (IModHelpers.get().getMinecraftClientHelpers().isShifted()) {
                nextSection = infoBook.getCurrentSection().getParent();
                while (nextSection.getParent() != null) {
                    nextSection = nextSection.getParent();
                }
            } else {
                nextSection = infoBook.getCurrentSection().getParent();
            }
            nextPage = 0;
            infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));
            applyNavigation();
        }, this));
        this.addRenderableWidget(this.buttonBack = new NextPageButton(left + getPageWidth() + 127, top + 2, 0, 223, 13, 18, (button) -> {
            InfoSection.Location location = infoBook.getHistory().pop();
            goToLastPage = false;
            nextSection = location.getInfoSection();
            nextPage = location.getPage();
            applyNavigation();
        }, this));
        this.addRenderableWidget(this.buttonExternal = new NextPageButton(left + 130, top, 26, 203, 11, 11, (button) -> {
            IModHelpers.get().getBaseHelpers().openUrl(infoBook.getBaseUrl() + infoBook.getCurrentSection().getRelativeWebPath());
        }, this));
        this.updateGui();

        if (goToLastPage) {
            int page = Math.max(0, infoBook.getCurrentSection().getPages() - getPages());
            page += page % getPages();
            infoBook.setCurrentPage(page);
        }

        int page = infoBook.getCurrentPage();
        for (int innerPage = page; innerPage <= page + getPages() - 1; innerPage++) {
            for (HyperLink link : infoBook.getCurrentSection().getLinks(innerPage)) {
                if (link.getTranslationKey().equals(IModHelpers.get().getL10NHelpers().localize(link.getTranslationKey()))) {
                    CyclopsCoreNeoForge.clog(Level.WARN, "Could not find hyperlink localization for " + link.getTranslationKey());
                }
                int xOffset = getOffsetXForPageWithWidths(innerPage % getPages());
                this.addRenderableWidget(new TextOverlayButton(link, left + xOffset + link.getX(), top + getPageYOffset() / 2 + link.getY(),
                        InfoSection.getFontHeight(getFont()), getPageWidth() - getOffsetXTotal() - link.getX(), (button) -> {
                    goToLastPage = false;
                    nextSection = ((TextOverlayButton) button).getLink().getTarget();
                    nextPage = 0;
                    if (nextSection != infoBook.getCurrentSection())
                        infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));
                    applyNavigation();
                }, this));
            }
            for (AdvancedButton advancedButton : infoBook.getCurrentSection().getAdvancedButtons(innerPage)) {
                advancedButton.setOnPress((button) -> {
                    goToLastPage = false;
                    nextSection = ((AdvancedButton) button).getTarget();
                    nextPage = 0;
                    if (nextSection != infoBook.getCurrentSection())
                        infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));
                    applyNavigation();
                });
                this.addRenderableWidget(advancedButton);
            }
        }
    }

    protected abstract int getOffsetXForPageBase(int page);

    protected int getOffsetXForPageWithWidths(int page) {
        return getOffsetXForPageBase(page) + page * getPageWidth();
    }

    protected int getOffsetXTotal() {
        int total = 0;
        for (int i = 0; i < getPages(); i++) {
            total += getOffsetXForPageBase(i);
        }
        return total;
    }

    @Override
    public void render(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);

        guiGraphics.blit(RenderPipelines.GUI_TEXTURED, texture, left, top, 0, 0, getPageWidth(), getGuiHeight(), 256, 256);
        blitMirrored(guiGraphics, RenderPipelines.GUI_TEXTURED, texture, left + getPageWidth() - 1, top, 0, 0, getPageWidth(), getGuiHeight(), 256, 256, -1);
        int width = getPageWidth() - getOffsetXTotal();
        for (int i = 0; i < getPages(); i++) {
            infoBook.getCurrentSection().constructInfoSectionClient().drawScreen(this, guiGraphics, left + getOffsetXForPageWithWidths(i), top, getPageYOffset(), width, getGuiHeight(), infoBook.getCurrentPage() + i, mouseX, mouseY, getFootnoteOffsetX(), getFootnoteOffsetY());
        }

        super.render(guiGraphics, mouseX, mouseY, partialTicks);

        for (int i = 0; i < getPages(); i++) {
            infoBook.getCurrentSection().constructInfoSectionClient().postDrawScreen(this, guiGraphics, left + getOffsetXForPageWithWidths(i), top + getPageYOffset(), width, getGuiHeight(), infoBook.getCurrentPage() + i, mouseX, mouseY);
        }

        if (this.buttonNextPage.visible && IModHelpers.get().getRenderHelpers().isPointInButton(this.buttonNextPage, mouseX, mouseY)) {
            drawTooltip(guiGraphics, mouseX, mouseY, Component.translatable("infobook.cyclopscore.next_page"));
        }
        if (this.buttonPreviousPage.visible && IModHelpers.get().getRenderHelpers().isPointInButton(this.buttonPreviousPage, mouseX, mouseY)) {
            drawTooltip(guiGraphics, mouseX, mouseY, Component.translatable("infobook.cyclopscore.previous_page"));
        }
        if (this.buttonBack.visible && IModHelpers.get().getRenderHelpers().isPointInButton(this.buttonBack, mouseX, mouseY)) {
            drawTooltip(guiGraphics, mouseX, mouseY, Component.translatable("infobook.cyclopscore.last_page"));
        }
        if (this.buttonParent.visible && IModHelpers.get().getRenderHelpers().isPointInButton(this.buttonParent, mouseX, mouseY)) {
            drawTooltip(guiGraphics, mouseX, mouseY, Component.translatable("infobook.cyclopscore.parent_section"));
        }
        if (this.buttonExternal.visible && IModHelpers.get().getRenderHelpers().isPointInButton(this.buttonExternal, mouseX, mouseY)) {
            drawTooltip(guiGraphics, mouseX, mouseY, Component.translatable("infobook.cyclopscore.external"));
        }
    }

    protected void renderBackgroundSuper(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
        super.renderBackground(guiGraphics, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderBackground(GuiGraphics p_295206_, int p_295457_, int p_294596_, float p_296351_) {
        // Do nothing
    }

    @Override
    protected void renderBg(GuiGraphics guiGraphics, float partialTicks, int mouseX, int mouseY) {
        // Do nothing
    }

    @Override
    protected void renderLabels(GuiGraphics guiGraphics, int x, int y) {
        // Do nothing
    }

    public void drawTooltip(GuiGraphics guiGraphics, int mx, int my, Component component) {
        ClientTooltipComponent clientTooltipComponent = ClientTooltipComponent.create(component.getVisualOrderText());
        guiGraphics.renderTooltip(getFont(), List.of(clientTooltipComponent), mx, my, DefaultTooltipPositioner.INSTANCE, null);
    }

    public void blitMirrored(GuiGraphics guiGraphics, RenderPipeline renderPipeline, ResourceLocation atlasLocation, int x, int y, int u, int v, int width, int height, int textureWidth, int textureHeight, int color) {
        guiGraphics.innerBlit(renderPipeline, atlasLocation, x, x + width, y, y + height, (u + width) / (float) textureWidth, u / (float) textureWidth, v / (float) textureHeight, (v + height)/ (float) textureHeight, color);
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public Font getFont() {
        return this.font;
    }

    public int getBannerWidth() {
        return BANNER_WIDTH;
    }

    private void updateGui() {
        int width = getPageWidth() - getOffsetXTotal();
        int lineHeight = InfoSection.getFontHeight(getFont());
        int maxLines = (getGuiHeight() - 2 * getPageYOffset() - 5) / lineHeight;

        // Bake current and all reachable sections.
        List<InfoSection> infoSectionsToBake = Lists.newLinkedList();
        infoSectionsToBake.add(infoBook.getCurrentSection());
        getPreviousSections(infoSectionsToBake);
        getNextSections(infoSectionsToBake);
        for (InfoSection infoSection : infoSectionsToBake) {
            if (infoSection != null) infoSection.bakeSection(getFont(), width, maxLines, lineHeight, getPageYOffset());
        }

        updateButtons();
    }

    protected void getPreviousSections(List<InfoSection> sections) {
        InfoSection.Location location = infoBook.getCurrentSection().getPrevious(infoBook.getCurrentPage(), false);
        if (location != null) {
            sections.add(location.getInfoSection());
        }
    }

    protected void getNextSections(List<InfoSection> sections) {
        InfoSection.Location location = infoBook.getCurrentSection().getNext(infoBook.getCurrentPage() + getPages() - 1, false);
        if (location != null) {
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

    protected void applyNavigation() {
        if (nextSection != null && (nextSection != infoBook.getCurrentSection() || infoBook.getCurrentPage() != nextPage)) {
            infoBook.setCurrentSection(nextSection);
            nextSection = null;
            infoBook.setCurrentPage(nextPage);
            this.init();
        }
    }

    public void drawScaledCenteredString(GuiGraphics guiGraphics, String string, int x, int y, int width, float originalScale, int maxWidth, int color) {
        drawScaledCenteredString(guiGraphics, string, x, y, width, originalScale, maxWidth, color, false);
    }

    public void drawScaledCenteredString(GuiGraphics guiGraphics, String string, int x, int y, int width, float originalScale, int maxWidth, int color, boolean shadow) {
        float originalWidth = getFont().width(string) * originalScale;
        float scale = Math.min(originalScale, maxWidth / originalWidth * originalScale);
        drawScaledCenteredString(guiGraphics, string, x, y, width, scale, color, shadow);
    }

    public void drawScaledCenteredString(GuiGraphics guiGraphics, String string, int x, int y, int width, float scale, int color) {
        drawScaledCenteredString(guiGraphics, string, x, y, width, scale, color, false);
    }

    public void drawScaledCenteredString(GuiGraphics guiGraphics, String string, int x, int y, int width, float scale, int color, boolean shadow) {
        IModHelpers.get().getRenderHelpers().drawScaledCenteredString(guiGraphics, getFont(), string, x, y, width, scale, color, shadow, Font.DisplayMode.NORMAL);
    }

    public void drawHorizontalRule(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(RenderPipelines.GUI_OPAQUE_TEXTURED_BACKGROUND, texture, x - HR_WIDTH / 2, y - HR_HEIGHT / 2, 52, 180, HR_WIDTH, HR_HEIGHT, 256, 256);
    }

    public void drawTextBanner(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(RenderPipelines.GUI_OPAQUE_TEXTURED_BACKGROUND, texture, x - BANNER_WIDTH / 2, y - BANNER_HEIGHT / 2, 52, 191, BANNER_WIDTH, BANNER_HEIGHT, 256, 256);
    }

    public void drawArrowRight(GuiGraphics guiGraphics, int x, int y) {
        guiGraphics.blit(RenderPipelines.GUI_OPAQUE_TEXTURED_BACKGROUND, texture, x, y, 0, 210, ARROW_WIDTH, ARROW_HEIGHT, 256, 256);
    }

    public void drawOuterBorder(GuiGraphics guiGraphics, int x, int y, int width, int height) {
        drawOuterBorder(guiGraphics, x, y, width, height, 1, 1, 1, 1);
    }

    public void drawOuterBorder(GuiGraphics guiGraphics, int x, int y, int width, int height, float r, float g, float b, float alpha) {
        int z = 0; // Was blitOffset

        // Corners
        IModHelpers.get().getRenderHelpers().blitColored(guiGraphics, texture, x - BORDER_WIDTH, y - BORDER_WIDTH, BORDER_X, BORDER_Y, BORDER_CORNER, BORDER_CORNER, r, g, b, alpha);
        IModHelpers.get().getRenderHelpers().blitColored(guiGraphics, texture, x + width - BORDER_WIDTH, y - BORDER_WIDTH, BORDER_X + BORDER_CORNER, BORDER_Y, BORDER_CORNER, BORDER_CORNER, r, g, b, alpha);
        IModHelpers.get().getRenderHelpers().blitColored(guiGraphics, texture, x - BORDER_WIDTH, y + height - BORDER_WIDTH, BORDER_X + 3 * BORDER_CORNER, BORDER_Y, BORDER_CORNER, BORDER_CORNER, r, g, b, alpha);
        IModHelpers.get().getRenderHelpers().blitColored(guiGraphics, texture, x + width - BORDER_WIDTH, y + height - BORDER_WIDTH, BORDER_X + 2 * BORDER_CORNER, BORDER_Y, BORDER_CORNER, BORDER_CORNER, r, g, b, alpha);

        // Sides
        for (int i = BORDER_WIDTH; i < width - BORDER_WIDTH; i += BORDER_WIDTH) {
            int drawWidth = BORDER_WIDTH;
            if (i + BORDER_WIDTH >= width - BORDER_CORNER) {
                drawWidth -= i - (width - BORDER_CORNER);
            }
            IModHelpers.get().getRenderHelpers().blitColored(guiGraphics, texture, x + i, y - BORDER_WIDTH, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, drawWidth, BORDER_WIDTH, r, g, b, alpha);
            IModHelpers.get().getRenderHelpers().blitColored(guiGraphics, texture, x + i, y + height, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, drawWidth, BORDER_WIDTH, r, g, b, alpha);
        }
        for (int i = BORDER_WIDTH; i < height - BORDER_WIDTH; i += BORDER_WIDTH) {
            int drawHeight = BORDER_WIDTH;
            if (i + BORDER_WIDTH >= height - BORDER_CORNER) {
                drawHeight -= i - (height - BORDER_CORNER);
            }
            if (drawHeight > 0) {
                IModHelpers.get().getRenderHelpers().blitColored(guiGraphics, texture, x - BORDER_WIDTH, y + i, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, BORDER_WIDTH, drawHeight, r, g, b, alpha);
                IModHelpers.get().getRenderHelpers().blitColored(guiGraphics, texture, x + width, y + i, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, BORDER_WIDTH, drawHeight, r, g, b, alpha);
            }
        }
    }

    public void renderTooltip(GuiGraphics guiGraphics, ItemStack itemStack, int x, int y) {
        guiGraphics.setTooltipForNextFrame(getFont(), itemStack, x, y);
    }

    public int getTick() {
        return (int) getMinecraft().level.getGameTime();
    }


    @Override
    public void containerTick() {
        super.containerTick();
        if (!this.minecraft.player.isAlive()) {
            this.minecraft.player.closeContainer();
        }
    }

    public abstract void playPageFlipSound(SoundManager soundHandler);

    public abstract void playPagesFlipSound(SoundManager soundHandler);

    @Override
    public boolean mouseScrolled(double mouseX, double mouseY, double mouseZ, double scrollDelta) {
        if (scrollDelta < 0) {
            this.buttonNextPage.onClick(mouseX, mouseY);
            return true;
        }
        if (scrollDelta > 0) {
            this.buttonPreviousPage.onClick(mouseX, mouseY);
            return true;
        }
        return super.mouseScrolled(mouseX, mouseY, mouseZ, scrollDelta);
    }

    static class NextPageButton extends Button {

        private final ScreenInfoBook guiInfoBook;
        private int textureX, textureY;

        public NextPageButton(int x, int y, int textureX, int textureY, int width, int height,
                              Button.OnPress onPress, ScreenInfoBook guiInfoBook) {
            super(x, y, width, height, Component.literal(""), onPress, Button.DEFAULT_NARRATION);
            this.textureX = textureX;
            this.textureY = textureY;
            this.guiInfoBook = guiInfoBook;
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                boolean isHover = mouseX >= this.getX() && mouseY >= this.getY() &&
                        mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
                int k = textureX;
                int l = textureY;

                if (isHover) {
                    k += width;
                }

                guiGraphics.blit(RenderPipelines.GUI_OPAQUE_TEXTURED_BACKGROUND, guiInfoBook.texture, this.getX(), this.getY(), k, l, width, height, 256, 256);
            }
        }

        @Override
        public void playDownSound(SoundManager soundHandler) {
            guiInfoBook.playPageFlipSound(soundHandler);
        }

    }

    static class TextOverlayButton extends Button {

        private final ScreenInfoBook guiInfoBook;
        private HyperLink link;

        public TextOverlayButton(HyperLink link, int x, int y, int height, int maxWidth, Button.OnPress onPress,
                                 ScreenInfoBook guiInfoBook) {
            super(x, y, 0, height, Component.literal(InfoSection.formatString(IModHelpers.get().getL10NHelpers().localize(link.getTranslationKey()))), onPress, Button.DEFAULT_NARRATION);
            this.guiInfoBook = guiInfoBook;
            this.link = link;
            Font fontRenderer = Minecraft.getInstance().font;

            // MCP: getStringWidth
            this.width = fontRenderer.width(getMessage().getVisualOrderText());
            // Trim string if it is too long
            if (this.width > maxWidth) {
                String originalMessage = getMessage().getString();
                originalMessage = originalMessage.substring(0, (int) (((float) maxWidth) / this.width * originalMessage.length()) - 1);
                originalMessage = originalMessage + "…";
                setMessage(Component.literal(originalMessage));
                this.width = maxWidth;
            }
        }

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                boolean isHover = mouseX >= this.getX() && mouseY >= this.getY() &&
                        mouseX < this.getX() + this.width && mouseY < this.getY() + this.height;
                Minecraft minecraft = Minecraft.getInstance();
                MutableComponent msg = ((MutableComponent) getMessage());
                if (isHover) {
                    msg = msg.withStyle(ChatFormatting.UNDERLINE);
                }
                guiGraphics.drawString(minecraft.font, msg, getX(), getY(), IModHelpers.get().getBaseHelpers().RGBAToInt(isHover ? 100 : 0, isHover ? 100 : 0, isHover ? 150 : 125, 255), false);
            }
        }

        @Override
        public void playDownSound(SoundManager soundHandler) {
            guiInfoBook.playPagesFlipSound(soundHandler);
        }

        public HyperLink getLink() {
            return link;
        }
    }

}
