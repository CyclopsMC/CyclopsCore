package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeType;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;

import javax.annotation.Nullable;
import java.util.Collection;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * @author rubensworks
 */
public interface IMinecraftHelpers {

    /**
     * @return The length of one Minecraft day.
     */
    public int getDayLength();

    /**
     * @return The amount of steps there are in a vanilla comparator.
     */
    public int getComparatorMultiplier();

    /**
     * @return The amount of ticks that go in one second.
     */
    public int getSecondInTicks();

    /**
     * @return Cause a regular blockState update.
     */
    public int getBlockNotify();

    /**
     * @return Send a blockState update to the client.
     */
    public int getBlockNotifyClient();

    /**
     * @return Stop the blockState from re-rendering.
     */
    public int getBlockNotifyNoRerender();

    /**
     * Set the world time to day or night.
     * @param world the world to manipulate time in.
     * @param toDay if true, set to day, otherwise to night.
     */
    public void setDay(ServerLevel world, boolean toDay);

    /**
     * Check if the given player inventory is full.
     * @param player The player.
     * @return If the player does not have a free spot in it's inventory.
     */
    public boolean isPlayerInventoryFull(Player player);

    /**
     * @return The Minecraft version (e.g. "1.14.4")
     */
    public String getMinecraftVersion();

    /**
     * @return The Minecraft major and minor version (e.g. "1.14")
     */
    public String getMinecraftVersionMajorMinor();

    /**
     * Comparator for {@link BlockPos}.
     * @param pos1 First pos.
     * @param pos2 Second pos.
     * @return The compared value.
     */
    public int compareBlockPos(BlockPos pos1, BlockPos pos2);

    /**
     * @return If we are currently running inside a deobfuscated development environment.
     */
    public boolean isDevEnvironment();

    /**
     * @return If minecraft has been fully loaded.
     */
    public boolean isMinecraftInitialized();

    /**
     * Check if we are inside a modded minecraft environment.
     * @return If in minecraft.
     */
    public boolean isModdedEnvironment();

    /**
     * @return If we are physically running on a client.
     */
    public boolean isClientSide();

    /**
     * @return If we are physically running on a client and are running in the client thread.
     */
    public boolean isClientSideThread();

    /**
     * Check if the given mod is available and loaded.
     * @param modId A mod's id.
     * @return If it is loaded.
     */
    public boolean isModLoaded(String modId);

    /**
     * Open a menu with the given extra data.
     * @param containerSupplier A supplier of container properties including the registry name of the container
     * @param extraDataWriter Consumer to write any additional data the GUI needs
     */
    public void openMenu(ServerPlayer player, MenuProvider containerSupplier, Consumer<FriendlyByteBuf> extraDataWriter);

    /**
     * @return The current server instance.
     */
    public MinecraftServer getCurrentServer();

    /**
     * @param player A player
     * @return If the given player is a fake player.
     */
    public boolean isFakePlayer(Player player);

    /**
     * Get the output item of a recipe.
     * @param recipe A recipe.
     * @param level The world.
     * @return An output item.
     */
    public ItemStack getRecipeOutput(RecipeHolder<?> recipe, Level level);

    /**
     * Get the output item of a recipe.
     * @param recipe A recipe.
     * @param level The world.
     * @return An output item.
     */
    public ItemStack getRecipeOutput(Recipe<?> recipe, Level level);

    /**
     * Serialize the given value output to an NBT tag.
     * @param valueOutputConsumer Value output consumer.
     * @return An NBT tag.
     */
    public default CompoundTag valueOutputToNbt(Consumer<ValueOutput> valueOutputConsumer) {
        return valueOutputToNbt(valueOutputConsumer, null);
    }

    /**
     * Serialize the given value output to an NBT tag.
     * @param valueOutputConsumer Value output consumer.
     * @param lookupProvider A lookup provider.
     * @return An NBT tag.
     */
    public CompoundTag valueOutputToNbt(Consumer<ValueOutput> valueOutputConsumer, @Nullable HolderLookup.Provider lookupProvider);

    /**
     * Deserialize something from the given NBT tag as value input.
     * @param tag An NBT tag.
     * @param lookupProvider A lookup provider.
     * @param valueInputConsumer Value input function.
     */
    @Deprecated // TODO: rm in next major
    public default void valueInputFromNbt(CompoundTag tag, HolderLookup.Provider lookupProvider, Consumer<ValueInput> valueInputConsumer) {
        valueInputFromNbt(tag, lookupProvider, i -> {
            valueInputConsumer.accept(i);
            return null;
        });
    }

    /**
     * Deserialize something from the given NBT tag as value input.
     * @param tag An NBT tag.
     * @param lookupProvider A lookup provider.
     * @param valueInputConsumer Value input function.
     */
    public default void valueInputFromNbtVoid(CompoundTag tag, HolderLookup.Provider lookupProvider, Consumer<ValueInput> valueInputConsumer) {
        valueInputFromNbt(tag, lookupProvider, i -> {
            valueInputConsumer.accept(i);
            return null;
        });
    }

    /**
     * Deserialize something from the given NBT tag as value input.
     * @param tag An NBT tag.
     * @param lookupProvider A lookup provider.
     * @param valueInputConsumer Value input function.
     * @return The deserialized thing.
     * @param <T> The thing to deserialize.
     */
    public <T> T valueInputFromNbt(CompoundTag tag, HolderLookup.Provider lookupProvider, Function<ValueInput, T> valueInputConsumer);

    /**
     * Indicate that the following recipe types should be sent from the server to clients.
     * After this, clients can safely get {@link IMinecraftClientHelpers#getRecipes} for the given recipe types.
     *
     * @param recipeTypes Recipe types.
     */
    public void sendRecipesToClients(Supplier<Collection<RecipeType<?>>> recipeTypes);

}
