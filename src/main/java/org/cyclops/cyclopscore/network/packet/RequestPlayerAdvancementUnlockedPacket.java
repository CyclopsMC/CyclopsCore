package org.cyclops.cyclopscore.network.packet;

import net.minecraft.advancements.Advancement;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.helper.AdvancementHelpers;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet from client to server to request an update to advancement unlocked info.
 * @author rubensworks
 *
 */
public class RequestPlayerAdvancementUnlockedPacket extends PacketCodec {

	@CodecField
	private String advancementId;

    public RequestPlayerAdvancementUnlockedPacket() {

    }

	public RequestPlayerAdvancementUnlockedPacket(String advancementId) {
		this.advancementId = advancementId;
	}

	@Override
	public boolean isAsync() {
		return false;
	}

	@Override
	@OnlyIn(Dist.CLIENT)
	public void actionClient(Level level, Player player) {

	}

	@Override
	public void actionServer(Level level, ServerPlayer player) {
		Advancement advancement = AdvancementHelpers.getAdvancement(Dist.DEDICATED_SERVER, new ResourceLocation(advancementId));
		if (advancement == null) {
			CyclopsCore.clog(org.apache.logging.log4j.Level.ERROR, "Received an invalid advancement " + advancementId + " from " + player.getName());
			return;
		}
		CyclopsCore._instance.getPacketHandler().sendToPlayer(
				new SendPlayerAdvancementUnlockedPacket(advancementId, AdvancementHelpers
						.hasAdvancementUnlocked(player, advancement)), player);
	}
	
}