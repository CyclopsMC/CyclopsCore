package org.cyclops.cyclopscore.helper;

import net.minecraft.DetectedVersion;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.storage.ServerLevelData;

import java.util.Arrays;

/**
 * Contains helper methods for various minecraft specific things.
 * @author rubensworks, immortaleeb
 *
 */
public abstract class MinecraftHelpersCommon implements IMinecraftHelpers {

    @Override
    public int getDayLength() {
        return 24000;
    }

    @Override
    public int getComparatorMultiplier() {
        return 15;
    }

    @Override
    public int getSecondInTicks() {
        return 20;
    }

    @Override
    public int getBlockNotify() {
        return 1;
    }

    @Override
    public int getBlockNotifyClient() {
        return 2;
    }

    @Override
    public int getBlockNotifyNoRerender() {
        return 4;
    }

    @Override
    public void setDay(ServerLevel world, boolean toDay) {
        int currentTime = (int) world.getGameTime();
        int newTime = currentTime - (currentTime % (getDayLength() / 2)) + getDayLength() / 2;
        ((ServerLevelData) world.getLevelData()).setGameTime(newTime);
    }

    @Override
    public boolean isPlayerInventoryFull(Player player) {
        return player.getInventory().getFreeSlot() == -1;
    }

    @Override
    public String getMinecraftVersion() {
        return DetectedVersion.BUILT_IN.getName();
    }

    @Override
    public String getMinecraftVersionMajorMinor() {
        return String.join(".", Arrays.asList(getMinecraftVersion().split("\\.")).subList(0, 2));
    }

    @Override
    public int compareBlockPos(BlockPos pos1, BlockPos pos2) {
        int compX = Integer.compare(pos1.getX(), pos2.getX());
        if(compX == 0) {
            int compY = Integer.compare(pos1.getY(), pos2.getY());
            if(compY == 0) {
                return Integer.compare(pos1.getZ(), pos2.getZ());
            }
            return compY;
        }
        return compX;
    }

    @Override
    public <T> InteractionResultHolder<T> successAction(T result) {
        return new InteractionResultHolder<>(InteractionResult.SUCCESS, result);
    }

}
