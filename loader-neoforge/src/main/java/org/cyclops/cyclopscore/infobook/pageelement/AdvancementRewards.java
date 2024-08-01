package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Maps;
import lombok.Data;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.network.packet.AdvancementRewardsObtainPacket;

import java.util.List;
import java.util.Map;

/**
 * A holder for advancements and rewards.
 * @author rubensworks
 */
@Data
public class AdvancementRewards {

    private static final Map<String, AdvancementRewards> ACHIEVEMENT_REWARDS = Maps.newHashMap();
    public static void reset() {
        ACHIEVEMENT_REWARDS.clear();
    }

    public static String NBT_KEY_OBTAINED_PREFIX = Reference.MOD_ID + ":" + "obtainedAdvancements:";

    private final String id;
    private final List<ResourceLocation> advancements;
    private final List<IReward> rewards;

    public AdvancementRewards(String id, List<ResourceLocation> advancements, List<IReward> rewards) {
        this.id = id;
        this.advancements = advancements;
        this.rewards = rewards;
        if (ACHIEVEMENT_REWARDS.put(id, this) != null) {
            throw new IllegalArgumentException(String.format("Duplicate advancements rewards id '%s' was found.", id));
        }
    }

    public static AdvancementRewards getAdvancementRewards(String achievementRewardsId) {
        return ACHIEVEMENT_REWARDS.get(achievementRewardsId);
    }

    protected String getNbtTagKey() {
        return NBT_KEY_OBTAINED_PREFIX + id;
    }

    public boolean isObtained(Player player) {
        return EntityHelpers.getPersistedPlayerNbt(player).getBoolean(getNbtTagKey());
    }

    public void obtain(Player player) {
        if (!isObtained(player)) {
            // If client-side, send packet to server, otherwise, obtain the rewards server-side
            if (player.level().isClientSide()) {
                CyclopsCore._instance.getPacketHandler().sendToServer(new AdvancementRewardsObtainPacket(id));
            } else {
                for (IReward reward : getRewards()) {
                    reward.obtain(player);
                }
            }

            // Set NBT
            CompoundTag tag = player.getPersistentData();
            if (!tag.contains(Player.PERSISTED_NBT_TAG)) {
                tag.put(Player.PERSISTED_NBT_TAG, new CompoundTag());
            }
            CompoundTag persistedTag = tag.getCompound(Player.PERSISTED_NBT_TAG);
            persistedTag.putBoolean(getNbtTagKey(), true);
        }
    }
}
