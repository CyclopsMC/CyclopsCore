package org.cyclops.cyclopscore.network.packet;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
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
public class SendRecipeDisplayPacket extends PacketCodec<SendRecipeDisplayPacket> {

    public static final Type<SendRecipeDisplayPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "send_recipe_display_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, SendRecipeDisplayPacket> CODEC = getCodec(SendRecipeDisplayPacket::new);

    @CodecField
    private String recipeType;
    @CodecField
    private String recipe;
    private RecipeDisplayEntry recipeDisplay;

    public SendRecipeDisplayPacket() {
        super(TYPE);
    }

    public SendRecipeDisplayPacket(String recipeType, String recipe, RecipeDisplayEntry recipeDisplay) {
        this();
        this.recipeType = recipeType;
        this.recipe = recipe;
        this.recipeDisplay = recipeDisplay;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public void encode(RegistryFriendlyByteBuf output) {
        super.encode(output);
        RecipeDisplayEntry.STREAM_CODEC.encode(output, recipeDisplay);
    }

    @Override
    public void decode(RegistryFriendlyByteBuf input) {
        super.decode(input);
        recipeDisplay = RecipeDisplayEntry.STREAM_CODEC.decode(input);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void actionClient(Level level, Player player) {
        RecipeHelpers.setRecipeDisplay(
                BuiltInRegistries.RECIPE_TYPE.getValue(ResourceLocation.parse(this.recipeType)),
                ResourceLocation.parse(this.recipe),
                recipeDisplay
        );
    }

    @Override
    public void actionServer(Level level, ServerPlayer player) {

    }

}
