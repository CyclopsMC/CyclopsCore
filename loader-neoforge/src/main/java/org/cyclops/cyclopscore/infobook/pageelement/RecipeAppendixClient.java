package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.core.HolderSet;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.fluids.FluidStack;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.helper.IModHelpersNeoForge;
import org.cyclops.cyclopscore.infobook.*;

import java.util.List;

/**
 * @author rubensworks
 */
public abstract class RecipeAppendixClient<S extends RecipeAppendix<?>> extends SectionAppendixClient<S> {

    protected static final int TICK_DELAY = 30;

    protected RecipeAppendixClient(S sectionAppendix) {
        super(sectionAppendix);
    }

    protected int getTick(ScreenInfoBook gui) {
        return gui.getTick() / TICK_DELAY;
    }

    protected ItemStack prepareItemStacks(HolderSet<Item> items, int tick) {
        return prepareItemStacks(items.stream().map(ItemStack::new).toList(), tick);
    }

    protected ItemStack prepareItemStacks(List<ItemStack> itemStacks, int tick) {
        if(itemStacks.size() == 0) return ItemStack.EMPTY;
        return prepareItemStack(itemStacks.get(tick % itemStacks.size()).copy(), tick);
    }

    protected ItemStack prepareItemStack(ItemStack itemStack, int tick) {
        return itemStack;
    }

    protected void renderItem(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, ItemStack itemStack, int mx, int my, AdvancedButtonEnum buttonEnum) {
        renderItem(gui, guiGraphics, x, y, itemStack, mx, my, buttonEnum, 1.0F);
    }

    protected void renderItem(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, ItemStack itemStack, int mx, int my, boolean renderOverlays, AdvancedButtonEnum buttonEnum) {
        renderItem(gui, guiGraphics, x, y, itemStack, mx, my, renderOverlays, buttonEnum, 1.0F);
    }

    protected void renderItem(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, ItemStack itemStack, int mx, int my, AdvancedButtonEnum buttonEnum, float chance) {
        renderItem(gui, guiGraphics, x, y, itemStack, mx, my, true, buttonEnum, chance);
    }

    protected void renderItem(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, ItemStack itemStack, int mx, int my, boolean renderOverlays, AdvancedButtonEnum buttonEnum, float chance) {
        renderItemForButton(gui, guiGraphics, x, y, itemStack, mx, my, renderOverlays, buttonEnum != null ? (RecipeAppendixClient.ItemButton) getSectionAppendix().getRenderItemHolders().get(buttonEnum) : null, chance);
    }

    public static void renderItemForButton(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, ItemStack itemStack, int mx, int my, boolean renderOverlays, RecipeAppendixClient.ItemButton button) {
        renderItemForButton(gui, guiGraphics, x, y, itemStack, mx, my, renderOverlays, button, 1.0F);
    }

    public static void renderItemForButton(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, ItemStack itemStack, int mx, int my, boolean renderOverlays, RecipeAppendixClient.ItemButton button, float chance) {
        if(renderOverlays) gui.drawOuterBorder(guiGraphics, x, y, RecipeAppendix.SLOT_SIZE, RecipeAppendix.SLOT_SIZE, 1, 1, 1, 0.2f);

        if (!itemStack.isEmpty()) {
            guiGraphics.pose().pushMatrix();
            guiGraphics.item(itemStack, x, y);
            if (renderOverlays)
                guiGraphics.itemDecorations(Minecraft.getInstance().font, itemStack, x, y);
            guiGraphics.pose().popMatrix();

            if (chance != 1.0F) {
                String chanceString = chance * 100F + "%";
                gui.drawScaledCenteredString(guiGraphics, chanceString, x - 4, y + 3, gui.getFont().width(chanceString), 1f, 18, IModHelpers.get().getBaseHelpers().RGBAToInt(255, 255, 255, 255), true);
            }

            if (button != null && renderOverlays) button.update(x, y, itemStack, gui);
        }
    }

    protected void renderFluid(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, FluidStack fluidStack, int mx, int my, AdvancedButtonEnum buttonEnum) {
        renderFluid(gui, guiGraphics, x, y, fluidStack, mx, my, true, buttonEnum);
    }

    protected void renderFluid(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, FluidStack fluidStack, int mx, int my, boolean renderOverlays, AdvancedButtonEnum buttonEnum) {
        renderFluidForButton(gui, guiGraphics, x, y, fluidStack, mx, my, renderOverlays, buttonEnum != null ? (RecipeAppendixClient.FluidButton) getSectionAppendix().getRenderItemHolders().get(buttonEnum) : null);
    }

    public static void renderFluidForButton(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, FluidStack fluidStack, int mx, int my, boolean renderOverlays, RecipeAppendixClient.FluidButton button) {
        if(renderOverlays) gui.drawOuterBorder(guiGraphics, x, y, RecipeAppendix.SLOT_SIZE, RecipeAppendix.SLOT_SIZE, 1, 1, 1, 0.2f);

        if (!fluidStack.isEmpty()) {
            IModHelpersNeoForge.get().getGuiHelpers().renderFluidSlot(guiGraphics, fluidStack, x, y);

            if (button != null && renderOverlays) button.update(x, y, fluidStack, gui);
        }
    }

    public static void renderItemTooltip(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, ItemStack itemStack, int mx, int my) {
        guiGraphics.pose().pushMatrix();
        if(mx >= x && my >= y && mx <= x + RecipeAppendix.SLOT_SIZE && my <= y + RecipeAppendix.SLOT_SIZE && !itemStack.isEmpty() ) {
            guiGraphics.setTooltipForNextFrame(gui.getFont(), itemStack, mx, my);
        }
        guiGraphics.pose().popMatrix();
    }

    public static void renderFluidTooltip(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, FluidStack fluidStack, int mx, int my) {
        if(mx >= x && my >= y && mx <= x + RecipeAppendix.SLOT_SIZE && my <= y + RecipeAppendix.SLOT_SIZE && !fluidStack.isEmpty() ) {
            List<FormattedCharSequence> lines = Lists.newArrayList();
            lines.add(fluidStack.getHoverName().copy()
                    .withColor(TextColor.fromLegacyFormat(fluidStack.getFluid().getFluidType().getRarity(fluidStack).color()).getValue())
                    .getVisualOrderText());
            lines.add(FormattedCharSequence.forward(
                    fluidStack.getAmount() + " mB",
                    Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.GRAY)))
            );
            guiGraphics.setTooltipForNextFrame(gui.getFont(), lines, mx, my);
        }
    }

    @Override
    public final void drawElement(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        int yOffset = getSectionAppendix().getAdditionalHeight();
        gui.drawOuterBorder(guiGraphics, x - 1, y - 1 - yOffset, getSectionAppendix().getWidth() + 2, getSectionAppendix().getHeight() + 2, 0.5F, 0.5F, 0.5F, 0.4f);
        gui.drawTextBanner(guiGraphics, x + width / 2, y - 2 - yOffset);
        gui.drawScaledCenteredString(guiGraphics, IModHelpers.get().getL10NHelpers().localize(getSectionAppendix().getUnlocalizedTitle()), x, y - 2 - yOffset, width, 0.9f, gui.getBannerWidth() - 6, gui.getTitleColor());

        drawElementInner(gui, guiGraphics, x, y, width, height, page, mx, my);
    }

    protected abstract void drawElementInner(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height, int page, int mx, int my);

    protected void postDrawElement(ScreenInfoBook gui, GuiGraphicsExtractor guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        renderToolTips(guiGraphics, gui.getFont(), mx, my);
    }

    protected void renderToolTips(GuiGraphicsExtractor guiGraphics, Font font, int mx, int my) {
        for(AdvancedButton renderItemHolder : getSectionAppendix().getRenderItemHolders().values()) {
            renderItemHolder.renderTooltip(guiGraphics, font, mx, my);
        }
    }

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
        public void extractContents(GuiGraphicsExtractor guiGraphics, int mouseX, int mouseY, float partialTicks) {
            if(isVisible() && isHover(mouseX, mouseY)) {
                gui.drawOuterBorder(guiGraphics, getX(), getY(), 16, 16, 0.392f, 0.392f, 0.6f, 0.9f);
            }
        }

        @Override
        public boolean isVisible() {
            return super.isVisible() && element != null;
        }
    }

    public static class ItemButton extends ElementButton<ItemStack> {

        public ItemButton(IInfoBook infoBook) {
            super(infoBook);
        }

        @Override
        public void update(int x, int y, ItemStack element, ScreenInfoBook gui) {
            super.update(x, y, element.isEmpty() ? null : element, gui);
        }

        @Override
        public void renderTooltip(GuiGraphicsExtractor guiGraphics, Font font, int mx, int my) {
            if (getElement() != null) {
                RecipeAppendixClient.renderItemTooltip(gui, guiGraphics, getX(), getY(), getElement(), mx, my);
            }
        }

        @Override
        protected String getTranslationKey(ItemStack element) {
            return element.getItem().getDescriptionId();
        }
    }

    public static class FluidButton extends ElementButton<FluidStack> {

        public FluidButton(IInfoBook infoBook) {
            super(infoBook);
        }

        @Override
        protected String getTranslationKey(FluidStack element) {
            return element.getFluidType().getDescriptionId(element);
        }

        @Override
        public void renderTooltip(GuiGraphicsExtractor guiGraphics, Font font, int mx, int my) {
            if (getElement() != null) {
                RecipeAppendixClient.renderFluidTooltip(gui, guiGraphics, getX(), getY(), getElement(), mx, my);
            }
        }
    }

}
