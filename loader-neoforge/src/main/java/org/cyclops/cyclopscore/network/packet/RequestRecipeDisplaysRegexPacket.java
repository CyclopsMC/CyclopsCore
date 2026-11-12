package org.cyclops.cyclopscore.network.packet;

import com.google.common.collect.Sets;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
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
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.regex.Pattern;

/**
 * Packet from client to server to request a recipe display.
 * @author rubensworks
 *
 */
public class RequestRecipeDisplaysRegexPacket extends PacketCodec<RequestRecipeDisplaysRegexPacket> {

    public static final Type<RequestRecipeDisplaysRegexPacket> TYPE = new Type<>(Identifier.fromNamespaceAndPath(Reference.MOD_ID, "request_recipe_displays_regex_packet"));
    public static final StreamCodec<RegistryFriendlyByteBuf, RequestRecipeDisplaysRegexPacket> CODEC = getCodec(RequestRecipeDisplaysRegexPacket::new);

    public static final int MAX_REGEX_LENGTH = 256;
    /**
     * Regexes must be a recipe id, or a recipe id prefix followed by '.*'.
     * Such regexes can be matched in linear time.
     */
    public static final Pattern ALLOWED_REGEX = Pattern.compile("[a-z0-9_.-]+:[a-z0-9_./-]*(\\.\\*)?");
    /**
     * The maximum number of distinct requests per connection.
     * Clients send each request once per login.
     */
    public static final int MAX_REQUESTS_PER_CONNECTION = 256;
    private static final Map<ServerGamePacketListenerImpl, Set<Pair<String, String>>> CONNECTION_REQUESTS = new WeakHashMap<>();

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
        if (isAllowedRequest(player)) {
            List<Pair<Identifier, RecipeDisplayEntry>> recipeDisplays = IModHelpers.get().getCraftingHelpers().getRecipeDisplays(
                    BuiltInRegistries.RECIPE_TYPE.getValue(Identifier.parse(this.recipeType)),
                    recipeRegex
            );
            for (Pair<Identifier, RecipeDisplayEntry> entry : recipeDisplays) {
                CyclopsCoreNeoForge._instance.getPacketHandler().sendToPlayer(
                        new SendRecipeDisplayPacket(recipeType, entry.getLeft().toString(), entry.getRight()), player);
            }
        }
        // Always reply, so that the client does not keep waiting
        CyclopsCoreNeoForge._instance.getPacketHandler().sendToPlayer(new SendRecipeDisplaysRegexDonePacket(recipeType, recipeRegex), player);
    }

    protected boolean isAllowedRequest(ServerPlayer player) {
        if (!isAllowedRegex(this.recipeRegex)) {
            return false;
        }
        // Ignore repeated requests, and limit the number of requests
        Set<Pair<String, String>> requests = CONNECTION_REQUESTS.computeIfAbsent(player.connection, connection -> Sets.newHashSet());
        if (requests.size() >= MAX_REQUESTS_PER_CONNECTION) {
            return false;
        }
        return requests.add(Pair.of(this.recipeType, this.recipeRegex));
    }

    public static boolean isAllowedRegex(String regex) {
        return regex != null && regex.length() <= MAX_REGEX_LENGTH && ALLOWED_REGEX.matcher(regex).matches();
    }

}
