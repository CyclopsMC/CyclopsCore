package org.cyclops.cyclopscore.infobook;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import lombok.Getter;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.screen.inventory.ContainerScreen;
import net.minecraft.client.gui.widget.button.Button;
import net.minecraft.client.renderer.BufferBuilder;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.vertex.DefaultVertexFormats;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.apache.logging.log4j.Level;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.helper.RenderHelpers;
import org.cyclops.cyclopscore.inventory.container.ContainerExtended;
import org.cyclops.cyclopscore.network.packet.RequestPlayerNbtPacket;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * Base gui for {@link IInfoBook}.
 * @author rubensworks
 */
public abstract class ScreenInfoBook<T extends ContainerExtended> extends ContainerScreen<T> {

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

    public ScreenInfoBook(T container, PlayerInventory playerInventory, ITextComponent title, IInfoBook infoBook) {
        super(container, playerInventory, title);
        this.infoBook = infoBook;
        this.texture = constructGuiTexture();
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
        return Helpers.RGBToInt(120, 20, 30);
    }

    @Override
    public void init() {
        super.init();

        this.buttons.clear();
        this.children.clear();

        left = (width - getGuiWidth()) / 2;
        top = (height - getGuiHeight()) / 2;

        this.addButton(this.buttonNextPage = new NextPageButton(left + getPageWidth() + 100 + getPrevNextOffsetX(), top + 156 + getPrevNextOffsetY(), 0, 180, 18, 10, (button) -> {
            InfoSection.Location location = infoBook.getCurrentSection().getNext(infoBook.getCurrentPage() + getPages() - 1, MinecraftHelpers.isShifted());
            goToLastPage = false;
            nextSection = location.getInfoSection();
            nextPage = location.getPage();
            infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));
            applyNavigation();
        }, this));
        this.addButton(this.buttonPreviousPage = new NextPageButton(left + 23 - getPrevNextOffsetX(), top + 156 + getPrevNextOffsetY(), 0, 193, 18, 10, (button) -> {
            InfoSection.Location location = infoBook.getCurrentSection().getPrevious(infoBook.getCurrentPage(), MinecraftHelpers.isShifted());
            nextSection = location.getInfoSection();
            nextPage = location.getPage();
            // We can not set the new 'page', because the infoBook.getCurrentSection() hasn't been baked yet and we do not know the last page yet.
            goToLastPage = nextSection != infoBook.getCurrentSection() && !MinecraftHelpers.isShifted();
            infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));
            applyNavigation();
        }, this));
        this.addButton(this.buttonParent = new NextPageButton(left + 2, top + 2, 36, 180, 8, 8, (button) -> {
            goToLastPage = false;
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
            applyNavigation();
        }, this));
        this.addButton(this.buttonBack = new NextPageButton(left + getPageWidth() + 127, top + 2, 0, 223, 13, 18, (button) -> {
            InfoSection.Location location = infoBook.getHistory().pop();
            goToLastPage = false;
            nextSection = location.getInfoSection();
            nextPage = location.getPage();
            applyNavigation();
        }, this));
        this.addButton(this.buttonExternal = new NextPageButton(left + 130, top, 26, 203, 11, 11, (button) -> {
            Helpers.openUrl(infoBook.getBaseUrl() + infoBook.getCurrentSection().getRelativeWebPath());
        }, this));
        this.updateGui();

        if (goToLastPage) {
            int page = Math.max(0, infoBook.getCurrentSection().getPages() - getPages());
            page += page % getPages();
            infoBook.setCurrentPage(page);
        }

        int page = infoBook.getCurrentPage();
        for(int innerPage = page; innerPage <= page + getPages() - 1; innerPage++) {
            for (HyperLink link : infoBook.getCurrentSection().getLinks(innerPage)) {
                if (link.getTranslationKey().equals(L10NHelpers.localize(link.getTranslationKey()))) {
                    CyclopsCore.clog(Level.WARN, "Could not find hyperlink localization for " + link.getTranslationKey());
                }
                int xOffset = getOffsetXForPageWithWidths(innerPage % getPages());
                this.addButton(new TextOverlayButton(link, left + xOffset + link.getX(), top + getPageYOffset() / 2 + link.getY(),
                        InfoSection.getFontHeight(getFontRenderer()), getPageWidth() - getOffsetXTotal() - link.getX(), (button) -> {
                    goToLastPage = false;
                    nextSection = ((TextOverlayButton) button).getLink().getTarget();
                    nextPage = 0;
                    if(nextSection != infoBook.getCurrentSection()) infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));
                    applyNavigation();
                }, this));
            }
            for (AdvancedButton advancedButton : infoBook.getCurrentSection().getAdvancedButtons(innerPage)) {
                advancedButton.setOnPress((button) -> {
                    goToLastPage = false;
                    nextSection = ((AdvancedButton) button).getTarget();
                    nextPage = 0;
                    if(nextSection != infoBook.getCurrentSection()) infoBook.getHistory().push(new InfoSection.Location(infoBook.getCurrentPage(), infoBook.getCurrentSection()));
                    applyNavigation();
                });
                this.addButton(advancedButton);
            }
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
    public void render(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
        RenderHelpers.bindTexture(texture);

        blit(matrixStack, left, top, 0, 0, getPageWidth(), getGuiHeight());
        blitMirrored(left + getPageWidth() - 1, top, 0, 0, getPageWidth(), getGuiHeight());
        int width = getPageWidth() - getOffsetXTotal();
        for(int i = 0; i < getPages(); i++) {
            infoBook.getCurrentSection().drawScreen(this, matrixStack, left + getOffsetXForPageWithWidths(i), top, getPageYOffset(), width, getGuiHeight(), infoBook.getCurrentPage() + i, mouseX, mouseY, getFootnoteOffsetX(), getFootnoteOffsetY());
        }
        super.render(matrixStack, mouseX, mouseY, partialTicks);
        for(int i = 0; i < getPages(); i++) {
            infoBook.getCurrentSection().postDrawScreen(this, matrixStack, left + getOffsetXForPageWithWidths(i), top + getPageYOffset(), width, getGuiHeight(), infoBook.getCurrentPage() + i, mouseX, mouseY);
        }

        if (this.buttonNextPage.visible && RenderHelpers.isPointInButton(this.buttonNextPage, mouseX, mouseY)) {
            drawTooltip(matrixStack, mouseX, mouseY, new TranslationTextComponent("infobook.cyclopscore.next_page"));
        }
        if (this.buttonPreviousPage.visible && RenderHelpers.isPointInButton(this.buttonPreviousPage, mouseX, mouseY)) {
            drawTooltip(matrixStack, mouseX, mouseY, new TranslationTextComponent("infobook.cyclopscore.previous_page"));
        }
        if (this.buttonBack.visible && RenderHelpers.isPointInButton(this.buttonBack, mouseX, mouseY)) {
            drawTooltip(matrixStack, mouseX, mouseY, new TranslationTextComponent("infobook.cyclopscore.last_page"));
        }
        if (this.buttonParent.visible && RenderHelpers.isPointInButton(this.buttonParent, mouseX, mouseY)) {
            drawTooltip(matrixStack, mouseX, mouseY, new TranslationTextComponent("infobook.cyclopscore.parent_section"));
        }
        if (this.buttonExternal.visible && RenderHelpers.isPointInButton(this.buttonExternal, mouseX, mouseY)) {
            drawTooltip(matrixStack, mouseX, mouseY, new TranslationTextComponent("infobook.cyclopscore.external"));
        }
    }

    @Override
    protected void drawGuiContainerBackgroundLayer(MatrixStack matrixStack, float partialTicks, int mouseX, int mouseY) {
        // Do nothing
    }

    public void drawTooltip(MatrixStack matrixStack, int mx, int my, ITextComponent lines) {
        GlStateManager.pushMatrix();
        renderTooltip(matrixStack, lines, mx, my);
        GlStateManager.popMatrix();

        GlStateManager.disableLighting();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public void blitMirrored(int x, int y, int u, int v, int width, int height) {
        float f = 0.00390625F;
        float f1 = 0.00390625F;
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder worldRenderer = tessellator.getBuffer();
        worldRenderer.begin(GL11.GL_QUADS, DefaultVertexFormats.POSITION_TEX);
        worldRenderer.pos(x + 0, y + height, this.getBlitOffset()).tex(((float) (u + width) * f), ((float) (v + height) * f1)).endVertex();
        worldRenderer.pos(x + width, y + height, this.getBlitOffset()).tex(((float) (u + 0) * f), ((float) (v + height) * f1)).endVertex();
        worldRenderer.pos(x + width, y + 0, this.getBlitOffset()).tex(((float) (u + 0) * f), ((float) (v + 0) * f1)).endVertex();
        worldRenderer.pos(x + 0, y + 0, this.getBlitOffset()).tex(((float) (u + width) * f), ((float) (v + 0) * f1)).endVertex();
        tessellator.draw();
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }

    public FontRenderer getFontRenderer() {
        return this.font;
    }

    public int getBannerWidth() {
        return BANNER_WIDTH;
    }

    private void updateGui() {
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

    protected void applyNavigation() {
        if(nextSection != null && (nextSection != infoBook.getCurrentSection() || infoBook.getCurrentPage() != nextPage)) {
            infoBook.setCurrentSection(nextSection);
            nextSection = null;
            infoBook.setCurrentPage(nextPage);
            this.init();
        }
    }

    public void drawScaledCenteredString(MatrixStack matrixStack, String string, int x, int y, int width, float originalScale, int maxWidth, int color) {
        drawScaledCenteredString(matrixStack, string, x, y, width, originalScale, maxWidth, color, false);
    }

    public void drawScaledCenteredString(MatrixStack matrixStack, String string, int x, int y, int width, float originalScale, int maxWidth, int color, boolean shadow) {
        float originalWidth = getFontRenderer().getStringWidth(string) * originalScale;
        float scale = Math.min(originalScale, maxWidth / originalWidth * originalScale);
        drawScaledCenteredString(matrixStack, string, x, y, width, scale, color, shadow);
    }

    public void drawScaledCenteredString(MatrixStack matrixStack, String string, int x, int y, int width, float scale, int color) {
        drawScaledCenteredString(matrixStack, string, x, y, width, scale, color, false);
    }

    public void drawScaledCenteredString(MatrixStack matrixStack, String string, int x, int y, int width, float scale, int color, boolean shadow) {
        GlStateManager.pushMatrix();
        GlStateManager.scalef(scale, scale, 1.0f);
        int titleLength = font.getStringWidth(string);
        int titleHeight = font.FONT_HEIGHT;
        if (shadow) {
            font.drawStringWithShadow(matrixStack, string, Math.round((x + width / 2) / scale - titleLength / 2), Math.round(y / scale - titleHeight / 2), color);
        } else {
            font.drawString(matrixStack, string, Math.round((x + width / 2) / scale - titleLength / 2), Math.round(y / scale - titleHeight / 2), color);
        }
        GlStateManager.popMatrix();
    }

    public void drawHorizontalRule(MatrixStack matrixStack, int x, int y) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelpers.bindTexture(texture);
        this.blit(matrixStack, x - HR_WIDTH / 2, y - HR_HEIGHT / 2, 52, 180, HR_WIDTH, HR_HEIGHT);
        GlStateManager.disableBlend();
    }

    public void drawTextBanner(MatrixStack matrixStack, int x, int y) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelpers.bindTexture(texture);
        this.blit(matrixStack, x - BANNER_WIDTH / 2, y - BANNER_HEIGHT / 2, 52, 191, BANNER_WIDTH, BANNER_HEIGHT);
        GlStateManager.disableBlend();
    }

    public void drawArrowRight(MatrixStack matrixStack, int x, int y) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        RenderHelpers.bindTexture(texture);
        this.blit(matrixStack, x, y, 0, 210, ARROW_WIDTH, ARROW_HEIGHT);
        GlStateManager.disableBlend();
    }

    public void drawOuterBorder(MatrixStack matrixStack, int x, int y, int width, int height) {
        drawOuterBorder(matrixStack, x, y, width, height, 1, 1, 1, 1);
    }

    public void drawOuterBorder(MatrixStack matrixStack, int x, int y, int width, int height, float r, float g, float b, float alpha) {
        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        GlStateManager.color4f(r, g, b, alpha);
        RenderHelpers.bindTexture(texture);

        // Corners
        this.blit(matrixStack, x - BORDER_WIDTH, y - BORDER_WIDTH, BORDER_X, BORDER_Y, BORDER_CORNER, BORDER_CORNER);
        this.blit(matrixStack, x + width - BORDER_WIDTH, y - BORDER_WIDTH, BORDER_X + BORDER_CORNER, BORDER_Y, BORDER_CORNER, BORDER_CORNER);
        this.blit(matrixStack, x - BORDER_WIDTH, y + height - BORDER_WIDTH, BORDER_X + 3 * BORDER_CORNER, BORDER_Y, BORDER_CORNER, BORDER_CORNER);
        this.blit(matrixStack, x + width - BORDER_WIDTH, y + height - BORDER_WIDTH, BORDER_X + 2 * BORDER_CORNER, BORDER_Y, BORDER_CORNER, BORDER_CORNER);

        // Sides
        for(int i = BORDER_WIDTH; i < width - BORDER_WIDTH; i+=BORDER_WIDTH) {
            int drawWidth = BORDER_WIDTH;
            if(i + BORDER_WIDTH >= width - BORDER_CORNER) {
                drawWidth -= i - (width - BORDER_CORNER);
            }
            this.blit(matrixStack, x + i, y - BORDER_WIDTH, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, drawWidth, BORDER_WIDTH);
            this.blit(matrixStack, x + i, y + height, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, drawWidth, BORDER_WIDTH);
        }
        for(int i = BORDER_WIDTH; i < height - BORDER_WIDTH; i+=BORDER_WIDTH) {
            int drawHeight = BORDER_WIDTH;
            if(i + BORDER_WIDTH >= height - BORDER_CORNER) {
                drawHeight -= i - (height - BORDER_CORNER);
            }
            if(drawHeight > 0) {
                this.blit(matrixStack, x - BORDER_WIDTH, y + i, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, BORDER_WIDTH, drawHeight);
                this.blit(matrixStack, x + width, y + i, BORDER_X + 4 * BORDER_CORNER, BORDER_Y, BORDER_WIDTH, drawHeight);
            }
        }

        GlStateManager.color4f(1, 1, 1, 1);
    }

    public void renderTooltip(MatrixStack matrixStack, ItemStack itemStack, int x, int y) {
        super.renderTooltip(matrixStack, itemStack, x, y);
    }

    public int getTick() {
        return (int) getMinecraft().world.getGameTime();
    }


    @Override
    public void tick() {
        super.tick();
        if(!this.minecraft.player.isAlive()) {
            this.minecraft.player.closeScreen();
        }
    }

    public abstract void playPageFlipSound(SoundHandler soundHandler);
    public abstract void playPagesFlipSound(SoundHandler soundHandler);

    @OnlyIn(Dist.CLIENT)
    static class NextPageButton extends Button {

        private final ScreenInfoBook guiInfoBook;
        private int textureX, textureY;

        public NextPageButton(int x, int y, int textureX, int textureY, int width, int height,
                              Button.IPressable onPress, ScreenInfoBook guiInfoBook) {
            super(x, y, width, height, new StringTextComponent(""), onPress);
            this.textureX = textureX;
            this.textureY = textureY;
            this.guiInfoBook = guiInfoBook;
        }

        @Override
        public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                boolean isHover = mouseX >= this.x && mouseY >= this.y &&
                               mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                RenderHelpers.bindTexture(guiInfoBook.texture);
                int k = textureX;
                int l = textureY;

                if (isHover) {
                    k += width;
                }

                GlStateManager.enableBlend();
                GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
                this.blit(matrixStack, this.x, this.y, k, l, width, height);
                GlStateManager.disableBlend();
            }
        }

        @Override
        public void playDownSound(SoundHandler soundHandler) {
            guiInfoBook.playPageFlipSound(soundHandler);
        }

    }

    @OnlyIn(Dist.CLIENT)
    static class TextOverlayButton extends Button {

        private final ScreenInfoBook guiInfoBook;
        @Getter private HyperLink link;

        public TextOverlayButton(HyperLink link, int x, int y, int height, int maxWidth, Button.IPressable onPress,
                                 ScreenInfoBook guiInfoBook) {
            super(x, y, 0, height, new StringTextComponent(InfoSection.formatString(L10NHelpers.localize(link.getTranslationKey()))), onPress);
            this.guiInfoBook = guiInfoBook;
            this.link = link;
            FontRenderer fontRenderer = Minecraft.getInstance().fontRenderer;

            // MCP: getStringWidth
            this.width = fontRenderer.func_243245_a(getMessage().func_241878_f());
            // Trim string if it is too long
            if (this.width > maxWidth) {
                String originalMessage = getMessage().getString();
                originalMessage = originalMessage.substring(0, (int) (((float) maxWidth) / this.width * originalMessage.length()) - 1);
                originalMessage = originalMessage + "…";
                setMessage(new StringTextComponent(originalMessage));
                this.width = maxWidth;
            }
        }

        @Override
        public void renderButton(MatrixStack matrixStack, int mouseX, int mouseY, float partialTicks) {
            if (this.visible) {
                boolean isHover = mouseX >= this.x && mouseY >= this.y &&
                        mouseX < this.x + this.width && mouseY < this.y + this.height;
                GlStateManager.color4f(1.0F, 1.0F, 1.0F, 1.0F);
                Minecraft minecraft = Minecraft.getInstance();
                minecraft.fontRenderer.drawString(matrixStack, (isHover ? "§n" : "") + getMessage() + "§r", x, y,
                        Helpers.RGBToInt(isHover ? 100 : 0, isHover ? 100 : 0, isHover ? 150 : 125));
            }
        }

        @Override
        public void playDownSound(SoundHandler soundHandler) {
            guiInfoBook.playPagesFlipSound(soundHandler);
        }

    }

}
