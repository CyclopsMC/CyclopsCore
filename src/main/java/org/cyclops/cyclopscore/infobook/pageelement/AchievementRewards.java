package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Maps;
import lombok.Data;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.Achievement;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.helper.MinecraftHelpers;
import org.cyclops.cyclopscore.network.packet.AchievementRewardsObtainPacket;

import java.util.List;
import java.util.Map;

/**
 * A holder for achievements and rewards.
 * @author rubensworks
 */
@Data
public class AchievementRewards {

    private static final Map<String, AchievementRewards> ACHIEVEMENT_REWARDS = Maps.newHashMap();

    public static String NBT_KEY_OBTAINED_PREFIX = Reference.MOD_ID + ":" + "obtainedAchievements:";

    private final String id;
    private final List<Achievement> achievements;
    private final List<IReward> rewards;

    public AchievementRewards(String id, List<Achievement> achievements, List<IReward> rewards) {
        this.id = id;
        this.achievements = achievements;
        this.rewards = rewards;
        if (ACHIEVEMENT_REWARDS.put(id, this) != null) {
            throw new IllegalArgumentException(String.format("Duplicate achievement rewards id '%s' was found.", id));
        }
    }

    public static AchievementRewards getAchievementRewards(String achievementRewardsId) {
        return ACHIEVEMENT_REWARDS.get(achievementRewardsId);
    }

    protected String getNbtTagKey() {
        return NBT_KEY_OBTAINED_PREFIX + id;
    }

    public boolean isObtained(EntityPlayer player) {
        NBTTagCompound tag = player.getEntityData();
        if (tag != null) {
            NBTTagCompound persistedTag = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            return persistedTag != null && persistedTag.getBoolean(getNbtTagKey());
        }
        return false;
    }

    public void obtain(EntityPlayer player) {
        if (!isObtained(player)) {
            // If client-side, send packet to server, otherwise, obtain the rewards server-side
            if (MinecraftHelpers.isClientSide()) {
                CyclopsCore._instance.getPacketHandler().sendToServer(new AchievementRewardsObtainPacket(id));
            } else {
                for (IReward reward : getRewards()) {
                    reward.obtain(player);
                }
            }

            // Set NBT
            NBTTagCompound tag = player.getEntityData();
            if (!tag.hasKey(EntityPlayer.PERSISTED_NBT_TAG)) {
                tag.setTag(EntityPlayer.PERSISTED_NBT_TAG, new NBTTagCompound());
            }
            NBTTagCompound persistedTag = tag.getCompoundTag(EntityPlayer.PERSISTED_NBT_TAG);
            persistedTag.setBoolean(getNbtTagKey(), true);
        }
    }
}
