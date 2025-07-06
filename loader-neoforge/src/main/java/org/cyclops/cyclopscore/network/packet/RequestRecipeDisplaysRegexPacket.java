package org.cyclops.cyclopscore.network.packet;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.level.Level;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.helper.IModHelpers;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

import java.util.List;

/**
 * Packet from client to server to request a recipe display.
 * @author rubensworks
 *
 */
public class RequestRecipeDisplaysRegexPacket extends PacketCodec<RequestRecipeDisplaysRegexPacket> {

    public static final Type<RequestRecipeDisplaysRegexPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "request_recipe_displays_regex_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RequestRecipeDisplaysRegexPacket> CODEC = getCodec(RequestRecipeDisplaysRegexPacket::new);

    @CodecField
    private String recipeType;
    @CodecField
    private String recipeRegex;

    public RequestRecipeDisplaysRegexPacket() {
        super(TYPE);
    }

    public RequestRecipeDisplaysRegexPacket(RecipeType<?> recipeType, String recipeRegex) {
        this();
        this.recipeType = recipeType.toString();
        this.recipeRegex = recipeRegex;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    @Override
    public void actionClient(Level level, Player player) {

    }

    @Override
    public void actionServer(Level level, ServerPlayer player) {
        List<Pair<ResourceLocation, RecipeDisplayEntry>> recipeDisplays = IModHelpers.get().getCraftingHelpers().getRecipeDisplays(
                BuiltInRegistries.RECIPE_TYPE.getValue(ResourceLocation.parse(this.recipeType)),
                recipeRegex
        );
        for (Pair<ResourceLocation, RecipeDisplayEntry> entry : recipeDisplays) {
            CyclopsCoreNeoForge._instance.getPacketHandler().sendToPlayer(
                    new SendRecipeDisplayPacket(recipeType, entry.getLeft().toString(), entry.getRight()), player);
        }
        CyclopsCoreNeoForge._instance.getPacketHandler().sendToPlayer(new SendRecipeDisplaysRegexDonePacket(recipeType, recipeRegex), player);
    }

}
