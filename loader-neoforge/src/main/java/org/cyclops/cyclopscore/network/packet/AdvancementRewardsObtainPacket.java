package org.cyclops.cyclopscore.network.packet;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.CyclopsCore;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.infobook.pageelement.AdvancementRewards;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet for obtaining advancement rewards.
 * @author rubensworks
 *
 */
public class AdvancementRewardsObtainPacket extends PacketCodec<AdvancementRewardsObtainPacket> {

    public static final Type<AdvancementRewardsObtainPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "advancement_rewards_obtain_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, AdvancementRewardsObtainPacket> CODEC = getCodec(AdvancementRewardsObtainPacket::new);

    @CodecField
    private String advancementRewardsId;

    public AdvancementRewardsObtainPacket() {
        super(TYPE);
    }

    public AdvancementRewardsObtainPacket(String advancementRewardsId) {
        this();
        this.advancementRewardsId = advancementRewardsId;
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
        AdvancementRewards advancementRewards = AdvancementRewards.getAdvancementRewards(advancementRewardsId);
        if (advancementRewards != null) {
            advancementRewards.obtain(player);
        } else {
            CyclopsCore.clog(org.apache.logging.log4j.Level.WARN, String.format("Received an invalid advancement reward id '%s' from %s.", advancementRewardsId, player.getDisplayName().getString()));
        }
    }

}
