package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Style;
import net.minecraft.network.chat.TextColor;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.FormattedCharSequence;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.helper.AdvancementHelpers;
import org.cyclops.cyclopscore.helper.L10NHelpers;
import org.cyclops.cyclopscore.infobook.AdvancedButton;
import org.cyclops.cyclopscore.infobook.IInfoBook;
import org.cyclops.cyclopscore.infobook.InfoSection;
import org.cyclops.cyclopscore.infobook.ScreenInfoBook;
import org.lwjgl.opengl.GL11;

import java.util.List;

/**
 * A button that can be clicked for reward collection.
 */
@OnlyIn(Dist.CLIENT)
public class AchievementCollectButton extends AdvancedButton {

    private final AdvancementRewards advancementRewards;
    private final IInfoBook infoBook;

    public AchievementCollectButton(AdvancementRewards advancementRewards, IInfoBook infoBook) {
        this.advancementRewards = advancementRewards;
        this.infoBook = infoBook;
    }

    protected IInfoBook getInfoBook() {
        return infoBook;
    }

    @Override
    public void renderTooltip(GuiGraphics guiGraphics, Font font, int mx, int my) {
        super.renderTooltip(guiGraphics, font, mx, my);
        guiGraphics.pose().pushPose();
        if(mx >= getX() && my >= getY() && mx <= getX() + width && my <= getY() + height) {
            List<FormattedCharSequence> lines = Lists.newArrayList();
            if (advancementRewards.isObtained(Minecraft.getInstance().player)) {
                lines.add(FormattedCharSequence.forward(
                        L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".rewards.collected"),
                        Style.EMPTY.withItalic(true)
                ));
            } else {
                lines.add(FormattedCharSequence.forward(
                        L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".rewards.collect"),
                        Style.EMPTY.withBold(true)
                ));
                boolean canObtain = true;
                for (IReward reward : advancementRewards.getRewards()) {
                    if (!reward.canObtain(Minecraft.getInstance().player)) {
                        canObtain = false;
                    }
                }
                if (!canObtain) {
                    lines.add(FormattedCharSequence.forward(
                            L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".rewards.collectFailure"),
                            Style.EMPTY.withColor(TextColor.fromLegacyFormat(ChatFormatting.RED))
                    ));
                }
            }
            guiGraphics.renderTooltip(font, lines, mx, my);
        }
        guiGraphics.pose().popPose();

        GlStateManager._enableBlend();
        GlStateManager._blendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    }

    @Override
    public boolean isVisible() {
        return this.visible;
    }

    @Override
    public void onPress() {
        super.onPress();
        Player player = Minecraft.getInstance().player;
        boolean canObtain = true;
        for (ResourceLocation advancement : advancementRewards.getAdvancements()) {
            if (!AdvancementHelpers.hasAdvancementUnlocked(Minecraft.getInstance().player, advancement)) {
                canObtain = false;
            }
        }
        for (IReward reward : advancementRewards.getRewards()) {
            if (!reward.canObtain(player)) {
                canObtain = false;
            }
        }
        if (canObtain) {
            advancementRewards.obtain(player);
        }
    }

    @Override
    public void update(int x, int y, Component displayName, InfoSection target, ScreenInfoBook gui) {
        super.update(x, y, displayName, target, gui);
        this.width = AdvancementRewardsAppendix.MAX_WIDTH;
    }
}
