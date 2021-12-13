package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Sets;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.CriteriaTriggers;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientAdvancementManager;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.server.ServerLifecycleHooks;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.network.packet.RequestPlayerAdvancementUnlockedPacket;

import java.util.Set;

/**
 * Helpers related to advancements
 * @author rubensworks
 */
public class AdvancementHelpers {

    public static final Set<ResourceLocation> ACHIEVED_ADVANCEMENTS = Sets.newHashSet();

    public static boolean hasAdvancementUnlocked(PlayerEntity player, Advancement advancement) {
        return player instanceof ServerPlayerEntity
                && ((ServerPlayerEntity) player).server.getPlayerList()
                .getPlayerAdvancements((ServerPlayerEntity) player).getOrStartProgress(advancement).isDone();
    }

    public static boolean hasAdvancementUnlocked(PlayerEntity player, ResourceLocation advancementId) {
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

    public static AdvancementManager getAdvancementManagerServer() {
        return ServerLifecycleHooks.getCurrentServer().getAdvancements();
    }

    public static ClientAdvancementManager getAdvancementManagerClient() {
        return Minecraft.getInstance().player.connection.getAdvancements();
    }

    public static <T extends ICriterionTrigger<?>> T registerCriteriaTrigger(T criterion) {
        return CriteriaTriggers.register(criterion);
    }

}
