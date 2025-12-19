package org.cyclops.cyclopscore.helper;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.item.crafting.display.RecipeDisplayEntry;
import org.apache.commons.lang3.tuple.Pair;
import org.cyclops.cyclopscore.CyclopsCoreNeoForge;
import org.cyclops.cyclopscore.infobook.IInfoBookRegistry;
import org.cyclops.cyclopscore.infobook.pageelement.AdvancementRewards;
import org.cyclops.cyclopscore.network.packet.RequestRecipeDisplayPacket;
import org.cyclops.cyclopscore.network.packet.RequestRecipeDisplaysRegexPacket;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author rubensworks
 */
public class RecipeHelpers {

    public static final Map<Pair<RecipeType<?>, Identifier>, RecipeDisplayEntry> CLIENT_RECIPE_DISPLAYS = Maps.newHashMap();
    private static final Set<Pair<RecipeType<?>, Identifier>> CLIENT_RECIPE_DISPLAYS_REQUESTING = Sets.newHashSet();
    private static final Set<Pair<RecipeType<?>, String>> CLIENT_RECIPE_DISPLAYS_REGEX_REQUESTING = Sets.newHashSet();
    private static final Set<Pair<RecipeType<?>, String>> CLIENT_RECIPE_DISPLAYS_REGEX_DONE = Sets.newHashSet();

    public static void reset() {
        CLIENT_RECIPE_DISPLAYS.clear();
        CLIENT_RECIPE_DISPLAYS_REQUESTING.clear();
        CLIENT_RECIPE_DISPLAYS_REGEX_REQUESTING.clear();
        CLIENT_RECIPE_DISPLAYS_REGEX_DONE.clear();
    }

    public static void requestRecipeDisplay(RecipeType<?> recipeType, Identifier recipe) {
        Pair<RecipeType<?>, Identifier> key = Pair.of(recipeType, recipe);
        if (!CLIENT_RECIPE_DISPLAYS.containsKey(key) && !CLIENT_RECIPE_DISPLAYS_REQUESTING.contains(key)) {
            CLIENT_RECIPE_DISPLAYS_REQUESTING.add(key);
            CyclopsCoreNeoForge._instance.getPacketHandler().sendToServer(new RequestRecipeDisplayPacket(recipeType, recipe));
        }
    }

    public static void requestRecipeDisplays(RecipeType<?> recipeType, String recipeRegex) {
        Pair<RecipeType<?>, String> key = Pair.of(recipeType, recipeRegex);
        if (!CLIENT_RECIPE_DISPLAYS_REGEX_REQUESTING.contains(key)) {
            CLIENT_RECIPE_DISPLAYS_REGEX_REQUESTING.add(key);
            CyclopsCoreNeoForge._instance.getPacketHandler().sendToServer(new RequestRecipeDisplaysRegexPacket(recipeType, recipeRegex));
        }
    }

    public static void setRecipeDisplay(RecipeType<?> recipeType, Identifier recipe, RecipeDisplayEntry recipeDisplay) {
        CLIENT_RECIPE_DISPLAYS.put(Pair.of(recipeType, recipe), recipeDisplay);
    }

    @Nullable
    public static RecipeDisplayEntry getRecipeDisplay(RecipeType<?> recipeType, Identifier recipe) {
        if (!IModHelpers.get().getMinecraftHelpers().isClientSide()) {
            return null;
        }

        requestRecipeDisplay(recipeType, recipe);

        Pair<RecipeType<?>, Identifier> key = Pair.of(recipeType, recipe);
        return CLIENT_RECIPE_DISPLAYS.get(key);
    }

    public static List<RecipeDisplayEntry> getRecipeDisplays(RecipeType<?> recipeType, String recipeRegex) {
        if (!IModHelpers.get().getMinecraftHelpers().isClientSide()) {
            return Lists.newArrayList();
        }

        requestRecipeDisplays(recipeType, recipeRegex);

        List<RecipeDisplayEntry> displays = Lists.newArrayList();
        for (Map.Entry<Pair<RecipeType<?>, Identifier>, RecipeDisplayEntry> entry : CLIENT_RECIPE_DISPLAYS.entrySet()) {
            if (entry.getKey().getLeft() == recipeType && (recipeRegex.isEmpty() || entry.getKey().getRight().toString().matches(recipeRegex))) {
                displays.add(entry.getValue());
            }
        }
        return displays;
    }

    public static void setRecipeDisplaysRegexDone(RecipeType<?> value, String recipe) {
        CLIENT_RECIPE_DISPLAYS_REGEX_DONE.add(Pair.of(value, recipe));

        // Re-initialize all books once all regex requests are done
        if (!RecipeHelpers.areRecipeDisplayRegexRequestsPending()) {
            AdvancementRewards.reset();
            for (ModBaseCommon<?> modBaseCommon : ModBaseCommon.getCommonMods().values()) {
                IInfoBookRegistry registry = modBaseCommon.getRegistryManager().getRegistry(IInfoBookRegistry.class);
                if (registry != null) {
                    registry.initializeAllBooks(true);
                }
            }
        }
    }

    public static boolean areRecipeDisplayRegexRequestsPending() {
        return CLIENT_RECIPE_DISPLAYS_REGEX_REQUESTING.size() > CLIENT_RECIPE_DISPLAYS_REGEX_DONE.size();
    }
}
