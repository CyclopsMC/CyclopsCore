package org.cyclops.cyclopscore.network.packet;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.helper.EntityHelpers;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet from server to client to update persisted player NBT data.
 * @author rubensworks
 *
 */
public class SendPlayerNbtPacket extends PacketCodec<SendPlayerNbtPacket> {

    public static final Type<SendPlayerNbtPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "send_player_nbt"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SendPlayerNbtPacket> CODEC = getCodec(SendPlayerNbtPacket::new);

    @CodecField
    private CompoundTag nbtData;

    public SendPlayerNbtPacket() {
        super(TYPE);
    }

    public SendPlayerNbtPacket(Player player) {
        this();
        this.nbtData = EntityHelpers.getPersistedPlayerNbt(player);
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void actionClient(Level level, Player player) {
        player.getPersistentData().put(Player.PERSISTED_NBT_TAG, nbtData);
    }

    @Override
    public void actionServer(Level level, ServerPlayer player) {

    }

}
