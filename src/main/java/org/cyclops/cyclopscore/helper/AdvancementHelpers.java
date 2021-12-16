package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Sets;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.CriterionTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancements;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.ServerAdvancementManager;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.server.ServerLifecycleHooks;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.network.packet.RequestPlayerAdvancementUnlockedPacket;

import java.util.Set;

/**
 * Helpers related to advancements
 * @author rubensworks
 */
public class AdvancementHelpers {

    public static final Set<ResourceLocation> ACHIEVED_ADVANCEMENTS = Sets.newHashSet();

    public static boolean hasAdvancementUnlocked(Player player, Advancement advancement) {
        return player instanceof ServerPlayer
                && ((ServerPlayer) player).server.getPlayerList()
                .getPlayerAdvancements((ServerPlayer) player).getOrStartProgress(advancement).isDone();
    }

    public static boolean hasAdvancementUnlocked(Player player, ResourceLocation advancementId) {
        return ACHIEVED_ADVANCEMENTS.contains(advancementId);
    }

    public static void requestAdvancementUnlockInfo(ResourceLocation advancementId) {
        CyclopsCore._instance.getPacketHandler().sendToServer(new RequestPlayerAdvancementUnlockedPacket(advancementId.toString()));
    }

    public static Advancement getAdvancement(Dist dist, ResourceLocation resourceLocation) {
        if (dist.isClient()) {
            return getAdvancementManagerClient().getAdvancements().get(resourceLocation);
        }
        return getAdvancementManagerServer().getAdvancement(resourceLocation);
    }

    public static ServerAdvancementManager getAdvancementManagerServer() {
        return ServerLifecycleHooks.getCurrentServer().getAdvancements();
    }

    public static ClientAdvancements getAdvancementManagerClient() {
        return Minecraft.getInstance().player.connection.getAdvancements();
    }

    public static <T extends CriterionTrigger<?>> T registerCriteriaTrigger(T criterion) {
        return CriteriaTriggers.register(criterion);
    }

}
