package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Sets;
import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.advancements.ICriterionTrigger;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.helper.obfuscation.ObfuscationHelpers;
import org.cyclops.cyclopscore.network.packet.RequestPlayerAdvancementUnlockedPacket;

import java.util.Set;

/**
 * Helpers related to advancements
 * @author rubensworks
 */
public class AdvancementHelpers {

    public static final Set<ResourceLocation> ACHIEVED_ADVANCEMENTS = Sets.newHashSet();

    public static boolean hasAdvancementUnlocked(EntityPlayer player, Advancement advancement) {
        return player instanceof EntityPlayerMP
                && FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList()
                .getPlayerAdvancements((EntityPlayerMP) player).getProgress(advancement).isDone();
    }

    public static boolean hasAdvancementUnlocked(EntityPlayer player, ResourceLocation advancementId) {
        return ACHIEVED_ADVANCEMENTS.contains(advancementId);
    }

    public static void requestAdvancementUnlockInfo(ResourceLocation advancementId) {
        CyclopsCore._instance.getPacketHandler().sendToServer(new RequestPlayerAdvancementUnlockedPacket(advancementId.toString()));
    }

    public static Advancement getAdvancement(ResourceLocation resourceLocation) {
        return getAdvancementManager().getAdvancement(resourceLocation);
    }

    public static AdvancementManager getAdvancementManager() {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getAdvancementManager();
    }

    public static <T extends ICriterionTrigger> T registerCriteriaTrigger(T criterion) {
        return ObfuscationHelpers.registerCriteriaTrigger(criterion);
    }

}
