package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Lists;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.text.TextFormatting;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;
import org.cyclops.cyclopscore.helper.AdvancementHelpers;
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
@SideOnly(Side.CLIENT)
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
    public void renderTooltip(int mx, int my) {
        super.renderTooltip(mx, my);
        GlStateManager.pushMatrix();
        if(mx >= x && my >= y && mx <= x + width && my <= y + height) {
            List<String> lines = Lists.newArrayList();
            if (advancementRewards.isObtained(Minecraft.getMinecraft().player)) {
                lines.add(TextFormatting.ITALIC + L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".rewards.collected"));
            } else {
                lines.add(TextFormatting.BOLD + L10NHelpers.localize("gui." + getInfoBook().getMod().getModId() + ".rewards.collect"));
                boolean canObtain = true;
                for (IReward reward : advancementRewards.getRewards()) {
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
        for (ResourceLocation advancement : advancementRewards.getAdvancements()) {
            if (!AdvancementHelpers.hasAdvancementUnlocked(Minecraft.getMinecraft().player, advancement)) {
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
    public void update(int x, int y, String displayName, InfoSection target, GuiInfoBook gui) {
        super.update(x, y, displayName, target, gui);
        this.width = AdvancementRewardsAppendix.MAX_WIDTH;
    }
}
