package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraft.util.text.TextFormatting;
import org.cyclops.cyclopscore.client.gui.image.Images;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.GuiInfoBook;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.init.ModBase;
import org.lwjgl.opengl.GL11;

import java.awt.*;
import java.util.List;
import java.util.Map;

/**
 * An achievement rewards appendix.
 * @author rubensworks
 */
public class AchievementRewardsAppendix extends SectionAppendix {

    public static final ModBase.EnumReferenceKey<Boolean> REFKEY_REWARDS = ModBase.EnumReferenceKey.create("rewards", Boolean.class);

    private static final int SLOT_SIZE = 16;
    private static final int SLOT_PADDING = 2;

    private static final int MAX_WIDTH = 80;

    private static final AdvancedButton.Enum COLLECT = AdvancedButton.Enum.create();
    private final AdvancedButton.Enum[] rewards;
    private final AdvancedButton.Enum[] achievements;
    private final Point[] rewardPositions;

    private final AchievementRewards achievementRewards;
    private final int height;
    private final boolean enableRewards;

    /**
     * This map holds advanced buttons that have a unique identifier.
     * The map has to be populated in the baking of this appendix.
     * The map values can be updated on each render tick.
     */
    protected Map<AdvancedButton.Enum, AdvancedButton> renderButtonHolders = Maps.newHashMap();

    public AchievementRewardsAppendix(IInfoBook infoBook, AchievementRewards achievementRewards) {
        super(infoBook);
        this.achievementRewards = achievementRewards;
        rewards = new AdvancedButton.Enum[achievementRewards.getRewards().size()];
        achievements = new AdvancedButton.Enum[achievementRewards.getAchievements().size()];
        rewardPositions = new Point[achievementRewards.getRewards().size()];
        int x = 0;
        int y = 0;
        int row_max_y = 0;
        int max_width = 0;
        int max_height = 0;

        for (int i = 0; i < achievementRewards.getRewards().size(); i++) {
            IReward reward = achievementRewards.getRewards().get(i);
            rewards[i] = AdvancedButton.Enum.create();
            row_max_y = Math.max(row_max_y, reward.getHeight() + SLOT_PADDING * 2);
            if (x + reward.getWidth() > MAX_WIDTH) {
                y += row_max_y;
                max_width = Math.max(x, max_width);
                max_height = Math.max(row_max_y, max_height);
                x = 0;
                row_max_y= 0;
            }
            rewardPositions[i] = new Point(x, y);
            x += reward.getWidth();
        }
        for (int i = 0; i < achievementRewards.getAchievements().size(); i++) {
            achievements[i] = AdvancedButton.Enum.create();
        }

        height = Math.max(row_max_y, max_height);

        this.enableRewards = infoBook.getMod().getReferenceValue(REFKEY_REWARDS);
    }

    @Override
    protected int getOffsetY() {
        return 0;
    }

    @Override
    protected int getWidth() {
        return MAX_WIDTH;
    }

    @Override
    protected int getHeight() {
        return height + ((achievementRewards.getAchievements().size() * (SLOT_SIZE + SLOT_PADDING)) / MAX_WIDTH + 1) * (SLOT_SIZE + SLOT_PADDING) + 23;
    }

    @Override
    protected void drawElement(GuiInfoBook gui, int x, int y, int width, int height, int page, int mx, int my) {
        int offsetX = 0;
        int offsetY = 0;
        gui.drawOuterBorder(x - 1, y - 1, getWidth() + 2, getHeight() + 2, 0.5F, 0.5F, 0.5F, 0.4f);
        gui.drawTextBanner(x + width / 2, y - 2);
        gui.drawScaledCenteredString(L10NHelpers.localize("gui.achievements"), x, y - 2, width, 0.9f, gui.getBannerWidth() - 6, Helpers.RGBToInt(30, 20, 120));

        // Draw achievements
        offsetY += 10;
        boolean allAchievementsValid = true;
        for (int i = 0; i < achievementRewards.getAchievements().size(); i++) {
            Achievement achievement = achievementRewards.getAchievements().get(i);
            if (offsetX + SLOT_SIZE > MAX_WIDTH) {
                offsetY += SLOT_SIZE + SLOT_PADDING * 2;
                offsetX = 0;
            }
            RecipeAppendix.renderItemForButton(gui, x + offsetX, y + offsetY, achievement.theItemStack, mx, my, true, null);
            if (Minecraft.getMinecraft().thePlayer.getStatFileWriter().hasAchievementUnlocked(achievement)) {
                Images.OK.draw(gui, x + offsetX + 1, y + offsetY + 2);
            } else {
                allAchievementsValid = false;
            }
            renderButtonHolders.get(achievements[i]).update(x + offsetX, y + offsetY, "", null, gui);
            offsetX += SLOT_SIZE + SLOT_PADDING * 2;
        }

        boolean taken = achievementRewards.isObtained(Minecraft.getMinecraft().thePlayer);

        // Draw rewards button with fancy hover effect
        offsetY += SLOT_SIZE + SLOT_PADDING * 2 + 6;
        gui.drawTextBanner(x + width / 2, y - 2 + offsetY);
        boolean hovering = mx > x && mx < x + getWidth() && my > y + offsetY - 10 && my < y + offsetY + 5;
        gui.drawScaledCenteredString(L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".rewards"), x, y - 2 + offsetY, width, 0.9f, gui.getBannerWidth() - 6, Helpers.RGBToInt(30, 20, 120));
        renderButtonHolders.get(COLLECT).update(x, y - 8 + offsetY, "", null, gui);
        if (allAchievementsValid && !taken) {
            float g = hovering ? 1.0F : (((float) (gui.getTick() % 20)) / 20) * 0.4F + 0.6F;
            float r = hovering ? 0.2F : 0.7F;
            float b = hovering ? 0.2F : 0.7F;
            GlStateManager.color(r, g, b);
            Images.ARROW_DOWN.draw(gui, x, y + offsetY - 11);
            Images.ARROW_DOWN.draw(gui, x + 60, y + offsetY - 11);

        }
        offsetY += 10;

        // Draw rewards
        for (int i = 0; i < achievementRewards.getRewards().size(); i++) {
            achievementRewards.getRewards().get(i).drawElementInner(gui, x + rewardPositions[i].x, y + rewardPositions[i].y + offsetY, width, height, page, mx, my, renderButtonHolders.get(rewards[i]));
            if (taken) {
                Images.OK.draw(gui, x + rewardPositions[i].x + 1, y + rewardPositions[i].y + offsetY + 2);
            } else if (!allAchievementsValid) {
                Images.ERROR.draw(gui, x + rewardPositions[i].x + 1, y + rewardPositions[i].y + offsetY + 2);
            }
        }
    }

    @Override
    protected void postDrawElement(GuiInfoBook gui, int x, int y, int width, int height, int page, int mx, int my) {
        renderToolTips(gui, mx, my);
    }

    protected void renderToolTips(GuiInfoBook gui, int mx, int my) {
        for(AdvancedButton button : renderButtonHolders.values()) {
            button.renderTooltip(mx, my);
        }
    }

    @Override
    public void preBakeElement(InfoSection infoSection) {
        renderButtonHolders.clear();
    }

    @Override
    public void bakeElement(InfoSection infoSection) {
        renderButtonHolders.put(COLLECT, new CollectButton(achievementRewards, getInfoBook()));
        for (int i = 0; i < achievementRewards.getRewards().size(); i++) {
            renderButtonHolders.put(rewards[i], achievementRewards.getRewards().get(i).createButton(getInfoBook()));
        }
        for (int i = 0; i < achievementRewards.getAchievements().size(); i++) {
            renderButtonHolders.put(achievements[i], new AchievementButton(achievementRewards.getAchievements().get(i)));
        }
        infoSection.addAdvancedButtons(getPage(), renderButtonHolders.values());
    }

    /**
     * A button for the achievements, so they can be hovered.
     */
    public static class AchievementButton extends AdvancedButton {

        private final Achievement achievement;

        public AchievementButton(Achievement achievement) {
            this.achievement = achievement;
        }

        @Override
        public boolean isVisible() {
            return this.visible;
        }

        @Override
        public void renderTooltip(int mx, int my) {
            super.renderTooltip(mx, my);
            GlStateManager.pushMatrix();
            int x = xPosition;
            int y = yPosition;
            if(mx >= x && my >= y && mx <= x + SLOT_SIZE && my <= y + SLOT_SIZE) {
                List<String> lines = Lists.newArrayList();
                lines.add(achievement.getStatName().getFormattedText());
                lines.add(TextFormatting.GRAY.toString() + achievement.getDescription());
                gui.drawHoveringText(lines, mx, my);
            }
            GlStateManager.popMatrix();

            GlStateManager.disableLighting();

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }
    }

    /**
     * A button that can be clicked for reward collection.
     */
    public static class CollectButton extends AdvancedButton {

        private final AchievementRewards achievementRewards;
        private final IInfoBook infoBook;

        public CollectButton(AchievementRewards achievementRewards, IInfoBook infoBook) {
            this.achievementRewards = achievementRewards;
            this.infoBook = infoBook;
        }

        protected IInfoBook getInfoBook() {
            return infoBook;
        }

        @Override
        public void renderTooltip(int mx, int my) {
            super.renderTooltip(mx, my);
            GlStateManager.pushMatrix();
            int x = xPosition;
            int y = yPosition;
            if(mx >= x && my >= y && mx <= x + width && my <= y + height) {
                List<String> lines = Lists.newArrayList();
                if (achievementRewards.isObtained(Minecraft.getMinecraft().thePlayer)) {
                    lines.add(TextFormatting.ITALIC + L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".rewards.collected"));
                } else {
                    lines.add(TextFormatting.BOLD + L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".rewards.collect"));
                    boolean canObtain = true;
                    for (IReward reward : achievementRewards.getRewards()) {
                        if (!reward.canObtain(Minecraft.getMinecraft().thePlayer)) {
                            canObtain = false;
                        }
                    }
                    if (!canObtain) {
                        lines.add(TextFormatting.RED + L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".rewards.collectFailure"));
                    }
                }
                gui.drawHoveringText(lines, mx, my);
            }
            GlStateManager.popMatrix();

            GlStateManager.disableLighting();

            GlStateManager.enableBlend();
            GlStateManager.blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        }

        @Override
        public boolean isVisible() {
            return this.visible;
        }

        @Override
        public void onClick() {
            EntityPlayer player = Minecraft.getMinecraft().thePlayer;
            boolean canObtain = true;
            for (Achievement achievement : achievementRewards.getAchievements()) {
                if (!Minecraft.getMinecraft().thePlayer.getStatFileWriter().hasAchievementUnlocked(achievement)) {
                    canObtain = false;
                }
            }
            for (IReward reward : achievementRewards.getRewards()) {
                if (!reward.canObtain(player)) {
                    canObtain = false;
                }
            }
            if (canObtain) {
                achievementRewards.obtain(player);
            }
        }

        @Override
        public void update(int x, int y, String displayName, InfoSection target, GuiInfoBook gui) {
            super.update(x, y, displayName, target, gui);
            this.width = MAX_WIDTH;
        }
    }

}
