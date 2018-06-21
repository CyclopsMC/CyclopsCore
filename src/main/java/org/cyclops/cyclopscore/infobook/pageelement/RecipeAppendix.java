package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.RenderItem;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.ItemStack;
import net.minecraft.util.NonNullList;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.config.ConfigHandler;
import org.cyclops.cyclopscore.config.extendedconfig.ExtendedConfig;
import org.cyclops.cyclopscore.helper.GuiHelpers;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.GuiInfoBook;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.lwjgl.opengl.GL11;

import java.util.List;
import java.util.Map;

/**
 * Recipes that can be added to sections.
 * @author rubensworks
 */
public abstract class RecipeAppendix<T> extends SectionAppendix {

    protected static final int SLOT_SIZE = 16;
    protected static final int TICK_DELAY = 30;

    protected T recipe;

    /**
     * This map holds advanced buttons that have a unique identifier.
     * The map has to be populated in the baking of this appendix.
     * The map values can be updated on each render tick.
     */
    protected Map<AdvancedButton.Enum, AdvancedButton> renderItemHolders = Maps.newHashMap();

    public RecipeAppendix(IInfoBook infoBook, T recipe) {
        super(infoBook);
        this.recipe = recipe;
    }

    protected int getTick(GuiInfoBook gui) {
        return gui.getTick() / TICK_DELAY;
    }

    protected ItemStack prepareItemStacks(List<ItemStack> itemStacks, int tick) {
        if(itemStacks.size() == 0) return ItemStack.EMPTY;
        return prepareItemStack(itemStacks.get(tick % itemStacks.size()).copy(), tick);
    }

    protected ItemStack prepareItemStack(ItemStack itemStack, int tick) {
        if(itemStack.getItemDamage() == OreDictionary.WILDCARD_VALUE) {
            NonNullList<ItemStack> itemStacks = NonNullList.create();
            itemStack.getItem().getSubItems(CreativeTabs.SEARCH, itemStacks);
            if(itemStacks.isEmpty()) return itemStack;
            return itemStacks.get(tick % itemStacks.size());
        }
        return itemStack;
    }

    protected void renderItem(GuiInfoBook gui, int x, int y, ItemStack itemStack, int mx, int my, AdvancedButton.Enum buttonEnum) {
        renderItem(gui, x, y, itemStack, mx, my, buttonEnum, 1.0F);
    }

    protected void renderItem(GuiInfoBook gui, int x, int y, ItemStack itemStack, int mx, int my, boolean renderOverlays, AdvancedButton.Enum buttonEnum) {
        renderItem(gui, x, y, itemStack, mx, my, renderOverlays, buttonEnum, 1.0F);
    }

    protected void renderItem(GuiInfoBook gui, int x, int y, ItemStack itemStack, int mx, int my, AdvancedButton.Enum buttonEnum, float chance) {
        renderItem(gui, x, y, itemStack, mx, my, true, buttonEnum, chance);
    }

    protected void renderItem(GuiInfoBook gui, int x, int y, ItemStack itemStack, int mx, int my, boolean renderOverlays, AdvancedButton.Enum buttonEnum, float chance) {
        renderItemForButton(gui, x, y, itemStack, mx, my, renderOverlays, buttonEnum != null ? (ItemButton) renderItemHolders.get(buttonEnum) : null, chance);
    }

    public static void renderItemForButton(GuiInfoBook gui, int x, int y, ItemStack itemStack, int mx, int my, boolean renderOverlays, ItemButton button) {
        renderItemForButton(gui, x, y, itemStack, mx, my, renderOverlays, button, 1.0F);
    }

    public static void renderItemForButton(GuiInfoBook gui, int x, int y, ItemStack itemStack, int mx, int my, boolean renderOverlays, ItemButton button, float chance) {
        if(renderOverlays) gui.drawOuterBorder(x, y, SLOT_SIZE, SLOT_SIZE, 1, 1, 1, 0.2f);

        if (itemStack != null) {
            RenderItem renderItem = Minecraft.getMinecraft().getRenderItem();
            GlStateManager.pushMatrix();
            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
            RenderHelper.enableGUIStandardItemLighting();
            GlStateManager.enableRescaleNormal();
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            renderItem.renderItemAndEffectIntoGUI(itemStack, x, y);
            if (renderOverlays)
                renderItem.renderItemOverlays(Minecraft.getMinecraft().fontRenderer, itemStack, x, y);
            RenderHelper.disableStandardItemLighting();
            GlStateManager.popMatrix();
            GL11.glDisable(GL11.GL_DEPTH_TEST);

            if (chance != 1.0F) {
                String chanceString = chance * 100F + "%";
                gui.drawScaledCenteredString(chanceString, x - 3, y + 4, gui.getFontRenderer().getStringWidth(chanceString), 1f, 18, 0);
                gui.drawScaledCenteredString(chanceString, x - 4, y + 3, gui.getFontRenderer().getStringWidth(chanceString), 1f, 18, Helpers.RGBToInt(255, 255, 255));
            }

            if (button != null && renderOverlays) button.update(x, y, itemStack, gui);
        }
    }
    protected void renderFluid(GuiInfoBook gui, int x, int y, FluidStack fluidStack, int mx, int my, AdvancedButton.Enum buttonEnum) {
        renderFluid(gui, x, y, fluidStack, mx, my, true, buttonEnum);
    }

    protected void renderFluid(GuiInfoBook gui, int x, int y, FluidStack fluidStack, int mx, int my, boolean renderOverlays, AdvancedButton.Enum buttonEnum) {
        renderFluidForButton(gui, x, y, fluidStack, mx, my, renderOverlays, buttonEnum != null ? (FluidButton) renderItemHolders.get(buttonEnum) : null);
    }

    public static void renderFluidForButton(GuiInfoBook gui, int x, int y, FluidStack fluidStack, int mx, int my, boolean renderOverlays, FluidButton button) {
        if(renderOverlays) gui.drawOuterBorder(x, y, SLOT_SIZE, SLOT_SIZE, 1, 1, 1, 0.2f);

        if (fluidStack != null) {
            GuiHelpers.renderFluidSlot(gui, fluidStack, x, y);

            if (button != null && renderOverlays) button.update(x, y, fluidStack, gui);
        }
    }

    public static void renderItemTooltip(GuiInfoBook gui, int x, int y, ItemStack itemStack, int mx, int my) {
        GlStateManager.pushMatrix();
        if(mx >= x && my >= y && mx <= x + SLOT_SIZE && my <= y + SLOT_SIZE && itemStack != null ) {
            gui.renderToolTip(itemStack, mx, my);
        }
        GlStateManager.popMatrix();

        GlStateManager.disableLighting();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    public static void renderFluidTooltip(GuiInfoBook gui, int x, int y, FluidStack fluidStack, int mx, int my) {
        GlStateManager.pushMatrix();
        if(mx >= x && my >= y && mx <= x + SLOT_SIZE && my <= y + SLOT_SIZE && fluidStack != null ) {
            List<String> lines = Lists.newArrayList();
            lines.add(fluidStack.getFluid().getRarity().rarityColor + fluidStack.getLocalizedName());
            lines.add(TextFormatting.GRAY.toString() + fluidStack.amount + " mB");
            gui.drawHoveringText(lines, mx, my);
        }
        GlStateManager.popMatrix();

        GlStateManager.disableLighting();

        GlStateManager.enableBlend();
        GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
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
    public final void drawElement(GuiInfoBook gui, int x, int y, int width, int height, int page, int mx, int my) {
        int yOffset = getAdditionalHeight();
        gui.drawOuterBorder(x - 1, y - 1 - yOffset, getWidth() + 2, getHeight() + 2, 0.5F, 0.5F, 0.5F, 0.4f);
        gui.drawTextBanner(x + width / 2, y - 2 - yOffset);
        gui.drawScaledCenteredString(L10NHelpers.localize(getUnlocalizedTitle()), x, y - 2 - yOffset, width, 0.9f, gui.getBannerWidth() - 6, gui.getTitleColor());

        drawElementInner(gui, x, y, width, height, page, mx, my);
    }

    protected void postDrawElement(GuiInfoBook gui, int x, int y, int width, int height, int page, int mx, int my) {
        renderToolTips(gui, mx, my);
    }

    protected abstract void drawElementInner(GuiInfoBook gui, int x, int y, int width, int height, int page, int mx, int my);

    protected void renderToolTips(GuiInfoBook gui, int mx, int my) {
        for(AdvancedButton renderItemHolder : renderItemHolders.values()) {
            renderItemHolder.renderTooltip(mx, my);
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
        public void update(int x, int y, E element, GuiInfoBook gui) {
            this.element = element;
            InfoSection target = null;
            if(this.element != null) {
                ExtendedConfig<?> config = getConfigFromElement(element);
                if (config != null) {
                    Pair<InfoSection, Integer> pair = this.infoBook.getConfigLinks().get(config.getFullUnlocalizedName());
                    if(pair != null) {
                        target = pair.getLeft();
                    }
                }
            }
            super.update(x, y, "empty", target, gui);
        }

        protected abstract ExtendedConfig<?> getConfigFromElement(E element);

        @Override
        public void drawButton(Minecraft minecraft, int mouseX, int mouseY, float partialTicks) {
            if(isVisible() && isHover(mouseX, mouseY)) {
                gui.drawOuterBorder(x, y, 16, 16, 0.392f, 0.392f, 0.6f, 0.9f);
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
        public void update(int x, int y, ItemStack element, GuiInfoBook gui) {
            super.update(x, y, element.isEmpty() ? null : element, gui);
        }

        @Override
        public void renderTooltip(int mx, int my) {
            RecipeAppendix.renderItemTooltip(gui, x, y, getElement(), mx, my);
        }

        @Override
        protected ExtendedConfig<?> getConfigFromElement(ItemStack element) {
            return ConfigHandler.getConfigFromItem(element.getItem());
        }
    }

    public static class FluidButton extends ElementButton<FluidStack> {

        public FluidButton(IInfoBook infoBook) {
            super(infoBook);
        }

        @Override
        protected ExtendedConfig<?> getConfigFromElement(FluidStack element) {
            return ConfigHandler.getConfigFromFluid(element.getFluid());
        }

        @Override
        public void renderTooltip(int mx, int my) {
            RecipeAppendix.renderFluidTooltip(gui, x, y, getElement(), mx, my);
        }
    }

}
