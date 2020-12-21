package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Maps;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.advancements.Advancement;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.StringTextComponent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.client.gui.image.Images;
import org.cyclops.cyclopscore.helper.AdvancementHelpers;
import org.cyclops.cyclopscore.helper.Helpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;
import org.cyclops.cyclopscore.init.ModBase;

import java.awt.*;
import java.util.Map;

/**
 * An advancement rewards appendix.
 * @author rubensworks
 */
public class AdvancementRewardsAppendix extends SectionAppendix {

    public static final int SLOT_SIZE = 16;
    private static final int SLOT_PADDING = 2;
    public static final int MAX_WIDTH = 80;
    private static final long ADVANCEMENT_INFO_REQUEST_TIMEOUT = 60;

    private static final AdvancedButtonEnum COLLECT = AdvancedButtonEnum.create();
    private final AdvancedButtonEnum[] rewards;
    private final AdvancedButtonEnum[] advancements;
    private final Point[] rewardPositions;

    private final AdvancementRewards advancementRewards;
    private final int height;
    private final boolean enableRewards;

    private boolean errored = false;
    private long lastAdvancementInfoRequest = -1;

    /**
     * This map holds advanced buttons that have a unique identifier.
     * The map has to be populated in the baking of this appendix.
     * The map values can be updated on each render tick.
     */
    protected Map<AdvancedButtonEnum, AdvancedButton> renderButtonHolders = Maps.newHashMap();

    public AdvancementRewardsAppendix(IInfoBook infoBook, AdvancementRewards advancementRewards) {
        super(infoBook);
        this.advancementRewards = advancementRewards;
        rewards = new AdvancedButtonEnum[advancementRewards.getRewards().size()];
        advancements = new AdvancedButtonEnum[advancementRewards.getAdvancements().size()];
        rewardPositions = new Point[advancementRewards.getRewards().size()];
        int x = 0;
        int y = 0;
        int row_max_y = 0;
        int max_width = 0;
        int max_height = 0;

        for (int i = 0; i < advancementRewards.getRewards().size(); i++) {
            IReward reward = advancementRewards.getRewards().get(i);
            rewards[i] = AdvancedButtonEnum.create();
            row_max_y = Math.max(row_max_y, reward.getHeight() + SLOT_PADDING * 2);
            if (x + reward.getWidth() > MAX_WIDTH) {
                y += row_max_y;
                max_width = Math.max(x, max_width);
                max_height = Math.max(row_max_y, max_height);
                x = 0;
                row_max_y = 0;
            }
            rewardPositions[i] = new Point(x, y);
            x += reward.getWidth();
        }
        for (int i = 0; i < advancementRewards.getAdvancements().size(); i++) {
            advancements[i] = AdvancedButtonEnum.create();
        }

        height = y + Math.max(row_max_y, max_height);

        this.enableRewards = infoBook.getMod().getReferenceValue(ModBase.REFKEY_INFOBOOK_REWARDS);
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
        return height + ((int) Math.ceil((advancementRewards.getAdvancements().size() * (SLOT_SIZE + SLOT_PADDING * 2)) / MAX_WIDTH + 1) * (SLOT_SIZE + SLOT_PADDING * 2)) + 23;
    }

    protected void requestAdvancementInfo() {
        if (Minecraft.getInstance().world.getGameTime() - lastAdvancementInfoRequest > ADVANCEMENT_INFO_REQUEST_TIMEOUT) {
            advancementRewards.getAdvancements().forEach(AdvancementHelpers::requestAdvancementUnlockInfo);
            lastAdvancementInfoRequest = Minecraft.getInstance().world.getGameTime();
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void drawElement(ScreenInfoBook gui, MatrixStack matrixStack, int x, int y, int width, int height, int page, int mx, int my) {
        requestAdvancementInfo();

        int offsetX = 0;
        int offsetY = 0;
        gui.drawOuterBorder(matrixStack, x - 1, y - 1, getWidth() + 2, getHeight() + 2, 0.5F, 0.5F, 0.5F, 0.4f);
        gui.drawTextBanner(matrixStack, x + width / 2, y - 2);
        gui.drawScaledCenteredString(matrixStack, L10NHelpers.localize("gui.advancements"), x, y - 2, width, 0.9f, gui.getBannerWidth() - 6, Helpers.RGBToInt(30, 20, 120));

        // Draw advancements
        offsetY += 10;
        boolean allAchievementsValid = true;
        for (int i = 0; i < advancementRewards.getAdvancements().size(); i++) {
            ResourceLocation advancementId = advancementRewards.getAdvancements().get(i);
            Advancement advancement = AdvancementHelpers.getAdvancement(Dist.CLIENT, advancementId);
            if (advancement == null) {
                allAchievementsValid = false;
            } else {
                if (offsetX + SLOT_SIZE > MAX_WIDTH) {
                    offsetY += SLOT_SIZE + SLOT_PADDING * 2;
                    offsetX = 0;
                }
                RecipeAppendix.renderItemForButton(gui, matrixStack, x + offsetX, y + offsetY, advancement.getDisplay().getIcon(), mx, my, true, null);
                if (AdvancementHelpers.hasAdvancementUnlocked(Minecraft.getInstance().player, advancementId)) {
                    Images.OK.draw(gui, matrixStack, x + offsetX + 1, y + offsetY + 2);
                } else {
                    allAchievementsValid = false;
                }
                renderButtonHolders.get(advancements[i]).update(x + offsetX, y + offsetY, new StringTextComponent(""), null, gui);
                offsetX += SLOT_SIZE + SLOT_PADDING * 2;
            }
        }

        boolean taken = advancementRewards.isObtained(Minecraft.getInstance().player);

        // Draw rewards button with fancy hover effect
        offsetY += SLOT_SIZE + SLOT_PADDING * 2 + 6;
        gui.drawTextBanner(matrixStack, x + width / 2, y - 2 + offsetY);
        boolean hovering = mx > x && mx < x + getWidth() && my > y + offsetY - 10 && my < y + offsetY + 5;
        gui.drawScaledCenteredString(matrixStack, L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".rewards"), x, y - 2 + offsetY, width, 0.9f, gui.getBannerWidth() - 6, Helpers.RGBToInt(30, 20, 120));
        renderButtonHolders.get(COLLECT).update(x, y - 8 + offsetY, new StringTextComponent(""), null, gui);
        if (allAchievementsValid && !taken) {
            float g = hovering ? 1.0F : (((float) (gui.getTick() % 20)) / 20) * 0.4F + 0.6F;
            float r = hovering ? 0.2F : 0.7F;
            float b = hovering ? 0.2F : 0.7F;
            RenderSystem.color3f(r, g, b);
            Images.ARROW_DOWN.draw(gui, matrixStack, x, y + offsetY - 11);
            Images.ARROW_DOWN.draw(gui, matrixStack, x + 60, y + offsetY - 11);

        }
        offsetY += 10;

        // Draw rewards
        for (int i = 0; i < advancementRewards.getRewards().size(); i++) {
            advancementRewards.getRewards().get(i).drawElementInner(gui, matrixStack, x + rewardPositions[i].x, y + rewardPositions[i].y + offsetY, width, height, page, mx, my, renderButtonHolders.get(rewards[i]));
            if (taken) {
                Images.OK.draw(gui, matrixStack, x + rewardPositions[i].x + 1, y + rewardPositions[i].y + offsetY + 2);
            } else if (!allAchievementsValid) {
                Images.ERROR.draw(gui, matrixStack, x + rewardPositions[i].x + 1, y + rewardPositions[i].y + offsetY + 2);
            }
        }
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    protected void postDrawElement(ScreenInfoBook gui, MatrixStack matrixStack, int x, int y, int width, int height, int page, int mx, int my) {
        renderToolTips(gui, matrixStack, mx, my);
    }

    @OnlyIn(Dist.CLIENT)
    protected void renderToolTips(ScreenInfoBook gui, MatrixStack matrixStack, int mx, int my) {
        for(AdvancedButton button : renderButtonHolders.values()) {
            button.renderTooltip(matrixStack, mx, my);
        }
    }

    @Override
    public void preBakeElement(InfoSection infoSection) {
        renderButtonHolders.clear();
    }

    @Override
    public void bakeElement(InfoSection infoSection) {
        renderButtonHolders.put(COLLECT, new AchievementCollectButton(advancementRewards, getInfoBook()));
        for (int i = 0; i < advancementRewards.getRewards().size(); i++) {
            renderButtonHolders.put(rewards[i], advancementRewards.getRewards().get(i).createButton(getInfoBook()));
        }
        for (int i = 0; i < advancementRewards.getAdvancements().size(); i++) {
            renderButtonHolders.put(advancements[i], new AdvancementButton(advancementRewards.getAdvancements().get(i)));
        }
        infoSection.addAdvancedButtons(getPage(), renderButtonHolders.values());
    }

}
