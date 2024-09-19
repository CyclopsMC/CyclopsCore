package org.cyclops.cyclopscore.helper;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;

/**
 * @author rubensworks
 */
public class WorldHelpersCommon implements IWorldHelpers {

    private static final double TICK_LAG_REDUCTION_MODULUS_MODIFIER = 1.0D;

    private final IModHelpers modHelpers;

    public WorldHelpersCommon(IModHelpers modHelpers) {
        this.modHelpers = modHelpers;
    }

    @Override
    public int getChunkSize() {
        return 16;
    }

    @Override
    public boolean efficientTick(Level world, int baseModulus, int... params) {
        int mod = (int) (baseModulus * TICK_LAG_REDUCTION_MODULUS_MODIFIER);
        if(mod == 0) mod = 1;
        int offset = 0;
        for(int param : params) offset += param;
        return world.random.nextInt(mod) == Math.abs(offset) % mod;
    }

    @Override
    public boolean efficientTick(Level world, int baseModulus, BlockPos blockPos) {
        return efficientTick(world, baseModulus, blockPos.getX(), blockPos.getY(), blockPos.getZ());
    }

    @Override
    public <T, W extends LevelAccessor> T foldArea(W world, int[] areaMin, int[] areaMax, BlockPos blockPos, WorldFoldingFunction<T, T, W> folder, T value) {
        int x = blockPos.getX();
        int y = blockPos.getY();
        int z = blockPos.getZ();
        for(int xc = x - areaMin[0]; xc <= x + areaMax[0]; xc++) {
            for(int yc = y - areaMin[1]; yc <= y + areaMax[1]; yc++) {
                for(int zc = z - areaMin[2]; zc <= z + areaMax[2]; zc++) {
                    value = folder.apply(value, world, new BlockPos(xc, yc, zc));
                }
            }
        }
        return value;
    }

    @Override
    public <T, W extends LevelAccessor> T foldArea(W world, int area, BlockPos blockPos, WorldFoldingFunction<T, T, W> folder, T value) {
        return foldArea(world, new int[]{area, area, area}, new int[]{area, area, area}, blockPos, folder, value);
    }

    @Override
    public Level getActiveLevel() {
        if (this.modHelpers.getMinecraftHelpers().isClientSide()) {
            return WorldHelpersCommonClient.getActiveLevel();
        }
        return WorldHelpersCommonServer.getActiveLevel(this.modHelpers);
    }
}
