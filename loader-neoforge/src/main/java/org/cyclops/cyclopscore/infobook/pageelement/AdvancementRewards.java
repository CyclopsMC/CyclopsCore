package org.cyclops.cyclopscore.infobook.pageelement;

import com.google.common.collect.Maps;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.network.packet.AdvancementRewardsObtainPacket;

import java.util.List;
import java.util.Map;

/**
 * A holder for advancements and rewards.
 *
 * @author rubensworks
 */
public class AdvancementRewards {

    private static final Map<String, AdvancementRewards> ACHIEVEMENT_REWARDS = Maps.newHashMap();

    public static void reset() {
        ACHIEVEMENT_REWARDS.clear();
    }

    public static String NBT_KEY_OBTAINED_PREFIX = Reference.MOD_ID + ":" + "obtainedAdvancements:";

    private final String id;
    private final List<Identifier> advancements;
    private final List<IReward> rewards;

    public AdvancementRewards(String id, List<Identifier> advancements, List<IReward> rewards) {
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
        return EntityHelpers.getPersistedPlayerNbt(player).getBooleanOr(getNbtTagKey(), false);
    }

    public void obtain(Player player) {
        if (!isObtained(player)) {
            // If client-side, send packet to server, otherwise, obtain the rewards server-side
            if (player.level().isClientSide()) {
                CyclopsCoreNeoForge._instance.getPacketHandler().sendToServer(new AdvancementRewardsObtainPacket(id));
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
            CompoundTag persistedTag = tag.getCompound(Player.PERSISTED_NBT_TAG).orElseThrow();
            persistedTag.putBoolean(getNbtTagKey(), true);
        }
    }

    public String getId() {
        return this.id;
    }

    public List<Identifier> getAdvancements() {
        return this.advancements;
    }

    public List<IReward> getRewards() {
        return this.rewards;
    }

    public boolean equals(final Object o) {
        if (o == this) return true;
        if (!(o instanceof AdvancementRewards)) return false;
        final AdvancementRewards other = (AdvancementRewards) o;
        if (!other.canEqual((Object) this)) return false;
        final Object this$id = this.getId();
        final Object other$id = other.getId();
        if (this$id == null ? other$id != null : !this$id.equals(other$id)) return false;
        final Object this$advancements = this.getAdvancements();
        final Object other$advancements = other.getAdvancements();
        if (this$advancements == null ? other$advancements != null : !this$advancements.equals(other$advancements))
            return false;
        final Object this$rewards = this.getRewards();
        final Object other$rewards = other.getRewards();
        if (this$rewards == null ? other$rewards != null : !this$rewards.equals(other$rewards)) return false;
        return true;
    }

    protected boolean canEqual(final Object other) {
        return other instanceof AdvancementRewards;
    }

    public int hashCode() {
        final int PRIME = 59;
        int result = 1;
        final Object $id = this.getId();
        result = result * PRIME + ($id == null ? 43 : $id.hashCode());
        final Object $advancements = this.getAdvancements();
        result = result * PRIME + ($advancements == null ? 43 : $advancements.hashCode());
        final Object $rewards = this.getRewards();
        result = result * PRIME + ($rewards == null ? 43 : $rewards.hashCode());
        return result;
    }

    public String toString() {
        return "AdvancementRewards(id=" + this.getId() + ", advancements=" + this.getAdvancements() + ", rewards=" + this.getRewards() + ")";
    }
}
