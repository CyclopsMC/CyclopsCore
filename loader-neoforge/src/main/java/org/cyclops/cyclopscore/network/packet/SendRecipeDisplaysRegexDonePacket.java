package org.cyclops.cyclopscore.network.packet;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.helper.RecipeHelpers;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet from server to client to update player advancement unlocked info.
 * @author rubensworks
 *
 */
public class SendRecipeDisplaysRegexDonePacket extends PacketCodec<SendRecipeDisplaysRegexDonePacket> {

    public static final Type<SendRecipeDisplaysRegexDonePacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "send_recipe_displays_regex_done_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SendRecipeDisplaysRegexDonePacket> CODEC = getCodec(SendRecipeDisplaysRegexDonePacket::new);

    @CodecField
    private String recipeType;
    @CodecField
    private String regex;

    public SendRecipeDisplaysRegexDonePacket() {
        super(TYPE);
    }

    public SendRecipeDisplaysRegexDonePacket(String recipeType, String regex) {
        this();
        this.recipeType = recipeType;
        this.regex = regex;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void actionClient(Level level, Player player) {
        RecipeHelpers.setRecipeDisplaysRegexDone(
                BuiltInRegistries.RECIPE_TYPE.getValue(ResourceLocation.parse(this.recipeType)),
                this.regex
        );
    }

    @Override
    public void actionServer(Level level, ServerPlayer player) {

    }

}
