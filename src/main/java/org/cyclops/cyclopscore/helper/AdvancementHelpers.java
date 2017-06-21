package org.cyclops.cyclopscore.helper;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.FMLCommonHandler;

/**
 * Helpers related to advancements
 * @author rubensworks
 */
public class AdvancementHelpers {

    public static boolean hasAdvancementUnlocked(EntityPlayer player, Advancement advancement) {
        if (player instanceof EntityPlayerMP) {
            return ((EntityPlayerMP) player).getAdvancements().getProgress(advancement).func_192108_b();
        }
        return false; // TODO
    }

    public static Advancement getAdvancement(ResourceLocation resourceLocation) {
        return getAdvancementManager().getAdvancement(resourceLocation);
    }

    public static AdvancementManager getAdvancementManager() {
        return FMLCommonHandler.instance().getMinecraftServerInstance().getAdvancementManager();
    }

}
