package org.cyclops.cyclopscore.infobook.pageelement;

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.neoforged.api.distmarker.Dist;
import org.cyclops.cyclopscore.client.gui.image.Images;
import org.cyclops.cyclopscore.helper.AdvancementHelpers;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.AdvancedButtonEnum;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;

import java.awt.*;
import java.util.Map;

/**
 * @author rubensworks
 */
public class AdvancementRewardsAppendixClient extends SectionAppendixClient<AdvancementRewardsAppendix> {
    protected AdvancementRewardsAppendixClient(AdvancementRewardsAppendix sectionAppendix) {
        super(sectionAppendix);
    }

    @Override
    protected void drawElement(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        requestAdvancementInfo();

        int offsetX = 0;
        int offsetY = 0;
        gui.drawOuterBorder(guiGraphics, x - 1, y - 1, getSectionAppendix().getWidth() + 2, getSectionAppendix().getHeight() + 2, 0.5F, 0.5F, 0.5F, 0.4f);
        gui.drawTextBanner(guiGraphics, x + width / 2, y - 2);
        gui.drawScaledCenteredString(guiGraphics, IModHelpers.get().getL10NHelpers().localize("gui.advancements"), x, y - 2, width, 0.9f, gui.getBannerWidth() - 6, IModHelpers.get().getBaseHelpers().RGBAToInt(30, 20, 120, 255));

        // Draw advancements
        offsetY += 10;
        boolean allAchievementsValid = true;
        Map<AdvancedButtonEnum, AdvancedButton> renderButtonHolders = getSectionAppendix().getRenderButtonHolders();
        AdvancedButtonEnum[] advancements = getSectionAppendix().getAdvancements();
        for (int i = 0; i < getSectionAppendix().getAdvancementRewards().getAdvancements().size(); i++) {
            Identifier advancementId = getSectionAppendix().getAdvancementRewards().getAdvancements().get(i);
            AdvancementHolder advancement = AdvancementHelpers.getAdvancement(Dist.CLIENT, advancementId);
            if (offsetX + AdvancementRewardsAppendix.SLOT_SIZE > AdvancementRewardsAppendix.MAX_WIDTH) {
                offsetY += AdvancementRewardsAppendix.SLOT_SIZE + AdvancementRewardsAppendix.SLOT_PADDING * 2;
                offsetX = 0;
            }
            if (advancement == null) {
                allAchievementsValid = false;
                Images.LOCKED.draw(guiGraphics, x + offsetX + 2, y + offsetY + 1);
                renderButtonHolders.get(advancements[i]).update(x + offsetX, y + offsetY, Component.literal(""), null, gui);
            } else {
                int finalOffsetX = offsetX;
                int finalOffsetY = offsetY;
                advancement.value().display().ifPresent(display -> RecipeAppendixClient.renderItemForButton(gui, guiGraphics, x + finalOffsetX, y + finalOffsetY, display.getIcon(), mx, my, true, null));
                if (AdvancementHelpers.hasAdvancementUnlocked(Minecraft.getInstance().player, advancementId)) {
                    Images.OK.draw(guiGraphics, x + offsetX + 1, y + offsetY + 2);
                } else {
                    allAchievementsValid = false;
                }
                renderButtonHolders.get(advancements[i]).update(x + offsetX, y + offsetY, Component.literal(""), null, gui);
                offsetX += AdvancementRewardsAppendix.SLOT_SIZE + AdvancementRewardsAppendix.SLOT_PADDING * 2;
            }
        }

        boolean taken = getSectionAppendix().getAdvancementRewards().isObtained(Minecraft.getInstance().player);

        // Draw rewards button with fancy hover effect
        offsetY += AdvancementRewardsAppendix.SLOT_SIZE + AdvancementRewardsAppendix.SLOT_PADDING * 2 + 6;
        gui.drawTextBanner(guiGraphics, x + width / 2, y - 2 + offsetY);
        boolean hovering = mx > x && mx < x + getSectionAppendix().getWidth() && my > y + offsetY - 10 && my < y + offsetY + 5;
        gui.drawScaledCenteredString(guiGraphics, IModHelpers.get().getL10NHelpers().localize("gui." + getSectionAppendix().getInfoBook().getMod().getModId() + ".rewards"), x, y - 2 + offsetY, width, 0.9f, gui.getBannerWidth() - 6, IModHelpers.get().getBaseHelpers().RGBAToInt(30, 20, 120, 255));
        renderButtonHolders.get(AdvancementRewardsAppendix.COLLECT).update(x, y - 8 + offsetY, Component.literal(""), null, gui);
        if (allAchievementsValid && !taken) {
            float g = hovering ? 1.0F : (((float) (gui.getTick() % 20)) / 20) * 0.4F + 0.6F;
            float r = hovering ? 0.2F : 0.7F;
            float b = hovering ? 0.2F : 0.7F;
            Images.ARROW_DOWN.drawWithColor(guiGraphics, x, y + offsetY - 11, r, g, b, 1);
            Images.ARROW_DOWN.drawWithColor(guiGraphics, x + 60, y + offsetY - 11, r, g, b, 1);

        }
        offsetY += 10;

        // Draw rewards
        Point[] rewardPositions = getSectionAppendix().getRewardPositions();
        for (int i = 0; i < getSectionAppendix().getAdvancementRewards().getRewards().size(); i++) {
            getSectionAppendix().getAdvancementRewards().getRewards().get(i).constructRewardClient().drawElementInner(gui, guiGraphics, x + rewardPositions[i].x, y + rewardPositions[i].y + offsetY, width, height, page, mx, my, renderButtonHolders.get(getSectionAppendix().getRewards()[i]));
            if (taken) {
                Images.OK.draw(guiGraphics, x + rewardPositions[i].x + 1, y + rewardPositions[i].y + offsetY + 2);
            } else if (!allAchievementsValid) {
                Images.ERROR.draw(guiGraphics, x + rewardPositions[i].x + 1, y + rewardPositions[i].y + offsetY + 2);
            }
        }
    }

    @Override
    protected void postDrawElement(ScreenInfoBook gui, GuiGraphics guiGraphics, int x, int y, int width, int height, int page, int mx, int my) {
        renderToolTips(gui, guiGraphics, mx, my);
    }

    protected void requestAdvancementInfo() {
        if (Minecraft.getInstance().level.getGameTime() - getSectionAppendix().lastAdvancementInfoRequest > AdvancementRewardsAppendix.ADVANCEMENT_INFO_REQUEST_TIMEOUT) {
            getSectionAppendix().getAdvancementRewards().getAdvancements().forEach(AdvancementHelpers::requestAdvancementUnlockInfo);
            getSectionAppendix().lastAdvancementInfoRequest = Minecraft.getInstance().level.getGameTime();
        }
    }

    protected void renderToolTips(ScreenInfoBook gui, GuiGraphics guiGraphics, int mx, int my) {
        for(AdvancedButton button : getSectionAppendix().getRenderButtonHolders().values()) {
            button.renderTooltip(guiGraphics, gui.getFont(), mx, my);
        }
    }
}
