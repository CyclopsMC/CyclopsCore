package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.stats.Achievement;
import net.minecraft.util.text.TextFormatting;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.GuiInfoBook;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * A button that can be clicked for reward collection.
 */
public class AchievementCollectButton extends AdvancedButton {

    private final AchievementRewards achievementRewards;
    private final IInfoBook infoBook;

    public AchievementCollectButton(AchievementRewards achievementRewards, IInfoBook infoBook) {
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
            if (achievementRewards.isObtained(Minecraft.getMinecraft().player)) {
                lines.add(TextFormatting.ITALIC + L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".rewards.collected"));
            } else {
                lines.add(TextFormatting.BOLD + L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".rewards.collect"));
                boolean canObtain = true;
                for (IReward reward : achievementRewards.getRewards()) {
                    if (!reward.canObtain(Minecraft.getMinecraft().player)) {
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
        EntityPlayer player = Minecraft.getMinecraft().player;
        boolean canObtain = true;
        for (Achievement achievement : achievementRewards.getAchievements()) {
            if (!Minecraft.getMinecraft().player.getStatFileWriter().hasAchievementUnlocked(achievement)) {
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
        this.width = AchievementRewardsAppendix.MAX_WIDTH;
    }
}
