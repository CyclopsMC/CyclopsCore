package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mojang.blaze3d.platform.GlStateManager;
import com.mojang.blaze3d.platform.Lighting;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.GuiHelpers;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;
import org.lwjgl.opengl.GL11;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * Recipes that can be added to sections.
 * @author rubensworks
 */
public abstract class RecipeAppendix<T extends Recipe<?>> extends SectionAppendix {

    protected static final int SLOT_SIZE = 16;
    protected static final int TICK_DELAY = 30;

    protected RecipeHolder<? extends T> recipe;

    /**
     * This map holds advanced buttons that have a unique identifier.
     * The map has to be populated in the baking of this appendix.
     * The map values can be updated on each render tick.
     */
    protected Map<AdvancedButtonEnum, AdvancedButton> renderItemHolders = Maps.newHashMap();

    public RecipeAppendix(IInfoBook infoBook, RecipeHolder<? extends T> recipe) {
        super(infoBook);
        this.recipe = recipe;
    }

    protected int getTick(ScreenInfoBook gui) {
        return gui.getTick() / TICK_DELAY;
    }

    protected ItemStack prepareItemStacks(ItemStack[] itemStacks, int tick) {
        return prepareItemStacks(Arrays.asList(itemStacks), tick);
    }

    protected ItemStack prepareItemStacks(List<ItemStack> itemStacks, int tick) {
        if(itemStacks.size() == 0) return ItemStack.EMPTY;
        return prepareItemStack(itemStacks.get(tick % itemStacks.size()).copy(), tick);
    }

    protected ItemStack prepareItemStack(ItemStack itemStack, int tick) {
        return itemStack;
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderItem(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, ItemStack itemStack, int mx, int my, AdvancedButtonEnum buttonEnum) {
        renderItem(gui, guiGraphics, x, y, itemStack, mx, my, buttonEnum, 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderItem(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, ItemStack itemStack, int mx, int my, boolean renderOverlays, AdvancedButtonEnum buttonEnum) {
        renderItem(gui, guiGraphics, x, y, itemStack, mx, my, renderOverlays, buttonEnum, 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderItem(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, ItemStack itemStack, int mx, int my, AdvancedButtonEnum buttonEnum, float chance) {
        renderItem(gui, guiGraphics, x, y, itemStack, mx, my, true, buttonEnum, chance);
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderItem(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, ItemStack itemStack, int mx, int my, boolean renderOverlays, AdvancedButtonEnum buttonEnum, float chance) {
        renderItemForButton(gui, guiGraphics, x, y, itemStack, mx, my, renderOverlays, buttonEnum != null ? (ItemButton) renderItemHolders.get(buttonEnum) : null, chance);
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderItemForButton(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, ItemStack itemStack, int mx, int my, boolean renderOverlays, ItemButton button) {
        renderItemForButton(gui, guiGraphics, x, y, itemStack, mx, my, renderOverlays, button, 1.0F);
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderItemForButton(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, ItemStack itemStack, int mx, int my, boolean renderOverlays, ItemButton button, float chance) {
        if(renderOverlays) gui.drawOuterBorder(guiGraphics, x, y, SLOT_SIZE, SLOT_SIZE, 1, 1, 1, 0.2f);

        if (!itemStack.isEmpty()) {
            ItemRenderer renderItem = Minecraft.getInstance().getItemRenderer();
            guiGraphics.pose().pushPose();
            GlStateManager._enableBlend();
            GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            Lighting.setupFor3DItems();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            guiGraphics.renderItem(itemStack, x, y);
            if (renderOverlays)
                guiGraphics.renderItemDecorations(Minecraft.getInstance().font, itemStack, x, y);
            Lighting.setupForFlatItems();
            guiGraphics.pose().popPose();
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            if (chance != 1.0F) {
                String chanceString = chance * 100F + "%";
                gui.drawScaledCenteredString(guiGraphics, chanceString, x - 4, y + 3, gui.getFont().width(chanceString), 1f, 18, Helpers.RGBToInt(255, 255, 255), true);
            }

            if (button != null && renderOverlays) button.update(x, y, itemStack, gui);
        }
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderFluid(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, FluidStack fluidStack, int mx, int my, AdvancedButtonEnum buttonEnum) {
        renderFluid(gui, guiGraphics, x, y, fluidStack, mx, my, true, buttonEnum);
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderFluid(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, FluidStack fluidStack, int mx, int my, boolean renderOverlays, AdvancedButtonEnum buttonEnum) {
        renderFluidForButton(gui, guiGraphics, x, y, fluidStack, mx, my, renderOverlays, buttonEnum != null ? (FluidButton) renderItemHolders.get(buttonEnum) : null);
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderFluidForButton(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, FluidStack fluidStack, int mx, int my, boolean renderOverlays, FluidButton button) {
        if(renderOverlays) gui.drawOuterBorder(guiGraphics, x, y, SLOT_SIZE, SLOT_SIZE, 1, 1, 1, 0.2f);

        if (!fluidStack.isEmpty()) {
            GuiHelpers.renderFluidSlot(guiGraphics, fluidStack, x, y);

            if (button != null && renderOverlays) button.update(x, y, fluidStack, gui);
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderItemTooltip(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, ItemStack itemStack, int mx, int my) {
        guiGraphics.pose().pushPose();
        if(mx >= x && my >= y && mx <= x + SLOT_SIZE && my <= y + SLOT_SIZE && !itemStack.isEmpty() ) {
            gui.renderTooltip(guiGraphics, itemStack, mx, my);
        }
        guiGraphics.pose().popPose();

        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    @OnlyIn(Dist.CLIENT)
    public static void renderFluidTooltip(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, FluidStack fluidStack, int mx, int my) {
        guiGraphics.pose().pushPose();
        if(mx >= x && my >= y && mx <= x + SLOT_SIZE && my <= y + SLOT_SIZE && !fluidStack.isEmpty() ) {
            List<FormattedCharSequence> lines = Lists.newArrayList();
            lines.add(fluidStack.getHoverName().copy()
                    .withColor(TextColor.fromLegacyFormat(fluidStack.getFluid().getFluidType().getRarity(fluidStack).color()).getValue())
                    .getVisualOrderText());
            lines.add(FormattedCharSequence.forward(
                    fluidStack.getAmount() + " mB",
                    Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY)))
            );
            guiGraphics.renderTooltip(gui.getFont(), lines, mx, my);
        }
        guiGraphics.pose().popPose();

        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    protected int getHeight() {
        return getHeightInner() + getAdditionalHeight();
    }

    protected abstract int getHeightInner();

    protected int getAdditionalHeight() {
        return 5;
    }

    @Override
    protected int getOffsetY() {
        return getAdditionalHeight();
    }

    protected abstract String getUnlocalizedTitle();

    @Override
    @OnlyIn(Dist.CLIENT)
    public final void drawElement(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        int yOffset = getAdditionalHeight();
        gui.drawOuterBorder(guiGraphics, x - 1, y - 1 - yOffset, getWidth() + 2, getHeight() + 2, 0.5F, 0.5F, 0.5F, 0.4f);
        gui.drawTextBanner(guiGraphics, x + width / 2, y - 2 - yOffset);
        gui.drawScaledCenteredString(guiGraphics, L10NHelpers.localize(getUnlocalizedTitle()), x, y - 2 - yOffset, width, 0.9f, gui.getBannerWidth() - 6, gui.getTitleColor());

        drawElementInner(gui, guiGraphics, x, y, width, height, page, mx, my);
    }

    @OnlyIn(Dist.CLIENT)
    protected void postDrawElement(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        renderToolTips(guiGraphics, gui.getFont(), mx, my);
    }

    @OnlyIn(Dist.CLIENT)
    protected abstract void drawElementInner(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my);

    @OnlyIn(Dist.CLIENT)
    protected void renderToolTips(GuiGraphics guiGraphics, Font font, int mx, int my) {
        for(AdvancedButton renderItemHolder : renderItemHolders.values()) {
            renderItemHolder.renderTooltip(guiGraphics, font, mx, my);
        }
    }

    @Override
    public void preBakeElement(InfoSection infoSection) {
        renderItemHolders.clear();
    }

    @Override
    public void bakeElement(InfoSection infoSection) {
        infoSection.addAdvancedButtons(getPage(), renderItemHolders.values());
    }

    @OnlyIn(Dist.CLIENT)
    public static abstract class ElementButton<E> extends AdvancedButton {

        private final IInfoBook infoBook;
        private E element;

        public ElementButton(IInfoBook infoBook) {
            this.infoBook = infoBook;
        }

        public E getElement() {
            return element;
        }

        /**
         * This is called each render tick to update the button to the latest render state.
         * @param x The X position.
         * @param y The Y position.
         * @param element The element to display.
         * @param gui The gui.
         */
        public void update(int x, int y, E element, ScreenInfoBook gui) {
            this.element = element;
            InfoSection target = null;
            if(this.element != null) {
                String translationKey = getTranslationKey(element);
                Pair<InfoSection, Integer> pair = this.infoBook.getConfigLinks().get(translationKey);
                if(pair != null) {
                    target = pair.getLeft();
                }
            }
            super.update(x, y, Component.literal("empty"), target, gui);
        }

        protected abstract String getTranslationKey(E element);

        @Override
        public void renderWidget(GuiGraphics guiGraphics, int mouseX, int mouseY, float partialTicks) {
            if(isVisible() && isHover(mouseX, mouseY)) {
                gui.drawOuterBorder(guiGraphics, getX(), getY(), 16, 16, 0.392f, 0.392f, 0.6f, 0.9f);
            }
        }

        @Override
        public boolean isVisible() {
            return super.isVisible() && element != null;
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class ItemButton extends ElementButton<ItemStack> {

        public ItemButton(IInfoBook infoBook) {
            super(infoBook);
        }

        @Override
        public void update(int x, int y, ItemStack element, ScreenInfoBook gui) {
            super.update(x, y, element.isEmpty() ? null : element, gui);
        }

        @Override
        public void renderTooltip(GuiGraphics guiGraphics, Font font, int mx, int my) {
            RecipeAppendix.renderItemTooltip(gui, guiGraphics, getX(), getY(), getElement(), mx, my);
        }

        @Override
        protected String getTranslationKey(ItemStack element) {
            return element.getDescriptionId();
        }
    }

    @OnlyIn(Dist.CLIENT)
    public static class FluidButton extends ElementButton<FluidStack> {

        public FluidButton(IInfoBook infoBook) {
            super(infoBook);
        }

        @Override
        protected String getTranslationKey(FluidStack element) {
            return element.getTranslationKey();
        }

        @Override
        public void renderTooltip(GuiGraphics guiGraphics, Font font, int mx, int my) {
            RecipeAppendix.renderFluidTooltip(gui, guiGraphics, getX(), getY(), getElement(), mx, my);
        }
    }

}
