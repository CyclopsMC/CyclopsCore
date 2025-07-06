package org.cyclops.cyclopscore.network.packet;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import net.minecraft.world.level.Level;
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
public class RequestRecipeDisplayPacket extends PacketCodec<RequestRecipeDisplayPacket> {

    public static final Type<RequestRecipeDisplayPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "request_recipe_display_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RequestRecipeDisplayPacket> CODEC = getCodec(RequestRecipeDisplayPacket::new);

    @CodecField
    private String recipeType;
    @CodecField
    private String recipe;

    public RequestRecipeDisplayPacket() {
        super(TYPE);
    }

    public RequestRecipeDisplayPacket(RecipeType<?> recipeType, ResourceLocation recipe) {
        this();
        this.recipeType = recipeType.toString();
        this.recipe = recipe.toString();
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
        List<RecipeDisplayEntry> recipeDisplays = IModHelpers.get().getCraftingHelpers().getRecipeDisplays(
                BuiltInRegistries.RECIPE_TYPE.getValue(ResourceLocation.parse(this.recipeType)),
                ResourceKey.create(Registries.RECIPE, ResourceLocation.parse(this.recipe))
        );
        if (recipeDisplays.isEmpty()) {
            CyclopsCoreNeoForge.clog(org.apache.logging.log4j.Level.ERROR, "Received an invalid recipe request for recipe type " + recipeType.toString() + " with recipe id " + recipe + " from " + player.getName());
            return;
        }
        CyclopsCoreNeoForge._instance.getPacketHandler().sendToPlayer(
                new SendRecipeDisplayPacket(recipeType, recipe, recipeDisplays.getFirst()), player);
    }

}
