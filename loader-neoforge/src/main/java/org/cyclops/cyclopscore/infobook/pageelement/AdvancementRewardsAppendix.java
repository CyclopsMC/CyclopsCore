package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Maps;
import org.cyclops.cyclopscore.infobook.*;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

import java.awt.*;
import java.util.Map;

/**
 * An advancement rewards appendix.
 * @author rubensworks
 */
public class AdvancementRewardsAppendix extends SectionAppendix<AdvancementRewardsAppendixClient> {

    public static final int SLOT_SIZE = 16;
    public static final int SLOT_PADDING = 2;
    public static final int MAX_WIDTH = 80;
    public static final long ADVANCEMENT_INFO_REQUEST_TIMEOUT = 60;

    public static final AdvancedButtonEnum COLLECT = AdvancedButtonEnum.create();
    private final AdvancedButtonEnum[] rewards;
    private final AdvancedButtonEnum[] advancements;
    private final Point[] rewardPositions;

    private final AdvancementRewards advancementRewards;
    private final int height;
    private final boolean enableRewards;

    public boolean errored = false;
    public long lastAdvancementInfoRequest = -1;

    /**
     * This map holds advanced buttons that have a unique identifier.
     * The map has to be populated in the baking of this appendix.
     * The map values can be updated on each render tick.
     */
    protected Map<AdvancedButtonEnum, AdvancedButton> renderButtonHolders = Maps.newHashMap();

    public AdvancementRewardsAppendix(IInfoBook infoBook, AdvancementRewards advancementRewards) throws InfoBookParser.InvalidAppendixException {
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

        this.enableRewards = infoBook.getMod().getReferenceValue(ModBaseNeoForge.REFKEY_INFOBOOK_REWARDS);
    }

    public AdvancedButtonEnum[] getRewards() {
        return rewards;
    }

    public AdvancedButtonEnum[] getAdvancements() {
        return advancements;
    }

    public Point[] getRewardPositions() {
        return rewardPositions;
    }

    public AdvancementRewards getAdvancementRewards() {
        return advancementRewards;
    }

    public Map<AdvancedButtonEnum, AdvancedButton> getRenderButtonHolders() {
        return renderButtonHolders;
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

    @Override
    public AdvancementRewardsAppendixClient constructSectionAppendixClient() {
        return new AdvancementRewardsAppendixClient(this);
    }

    @Override
    public void preBakeElement(InfoSection infoSection) {
        renderButtonHolders.clear();
    }

    @Override
    public void bakeElement(InfoSection infoSection) {
        renderButtonHolders.put(COLLECT, new AchievementCollectButton(advancementRewards, getInfoBook()));
        for (int i = 0; i < advancementRewards.getRewards().size(); i++) {
            renderButtonHolders.put(rewards[i], advancementRewards.getRewards().get(i).constructRewardClient().createButton(getInfoBook()));
        }
        for (int i = 0; i < advancementRewards.getAdvancements().size(); i++) {
            renderButtonHolders.put(advancements[i], new AdvancementButton(advancementRewards.getAdvancements().get(i)));
        }
        infoSection.addAdvancedButtons(getPage(), renderButtonHolders.values());
    }

}
