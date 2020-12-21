package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import com.mojang.blaze3d.matrix.MatrixStack;
import com.mojang.blaze3d.platform.GlStateManager;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.IReorderingProcessor;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.Color;
import net.minecraft.util.text.ITextComponent;
import net.minecraft.util.text.Style;
import net.minecraft.util.text.TextFormatting;
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
    public void renderTooltip(MatrixStack matrixStack, int mx, int my) {
        super.renderTooltip(matrixStack, mx, my);
        GlStateManager.pushMatrix();
        if(mx >= x && my >= y && mx <= x + width && my <= y + height) {
            List<IReorderingProcessor> lines = Lists.newArrayList();
            if (advancementRewards.isObtained(Minecraft.getInstance().player)) {
                lines.add(IReorderingProcessor.fromString(
                        L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".rewards.collected"),
                        Style.EMPTY.setItalic(true)
                ));
            } else {
                lines.add(IReorderingProcessor.fromString(
                        L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".rewards.collect"),
                        Style.EMPTY.setBold(true)
                ));
                boolean canObtain = true;
                for (IReward reward : advancementRewards.getRewards()) {
                    if (!reward.canObtain(Minecraft.getInstance().player)) {
                        canObtain = false;
                    }
                }
                if (!canObtain) {
                    lines.add(IReorderingProcessor.fromString(
                            L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".rewards.collectFailure"),
                            Style.EMPTY.setColor(Color.fromTextFormatting(TextFormatting.RED))
                    ));
                }
            }
            gui.renderTooltip(matrixStack, lines, mx, my);
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
    public void onPress() {
        super.onPress();
        PlayerEntity player = Minecraft.getInstance().player;
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
    public void update(int x, int y, ITextComponent displayName, InfoSection target, ScreenInfoBook gui) {
        super.update(x, y, displayName, target, gui);
        this.width = AdvancementRewardsAppendix.MAX_WIDTH;
    }
}
