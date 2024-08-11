package org.cyclops.cyclopscore.helper;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.minecraft.world.entity.player.Player;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public interface IFluidHelpersFabric {

    public int getBucketVolume();

    @Nullable
    public StorageView<FluidVariant> getContainedFluid(@javax.annotation.Nullable Storage<FluidVariant> storage);

    public long moveFluid(Storage<FluidVariant> source, Storage<FluidVariant> target, long amount, @Nullable Player player, boolean simulate);

    public long moveFluid(Storage<FluidVariant> source, Storage<FluidVariant> target, long amount, @Nullable Player player);

    public long moveFluid(Storage<FluidVariant> source, Storage<FluidVariant> target, long amount);

}
