package org.cyclops.cyclopscore.helper;

import net.fabricmc.fabric.api.transfer.v1.fluid.FluidConstants;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariant;
import net.fabricmc.fabric.api.transfer.v1.fluid.FluidVariantAttributes;
import net.fabricmc.fabric.api.transfer.v1.storage.Storage;
import net.fabricmc.fabric.api.transfer.v1.storage.StorageView;
import net.fabricmc.fabric.api.transfer.v1.transaction.Transaction;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.Nullable;

import java.util.Iterator;

/**
 * @author rubensworks
 */
public class FluidHelpersFabric implements IFluidHelpersFabric {
    @Override
    public int getBucketVolume() {
        return (int) FluidConstants.BUCKET;
    }

    @Override
    @Nullable
    public StorageView<FluidVariant> getContainedFluid(@javax.annotation.Nullable Storage<FluidVariant> storage) {
        if (storage == null) {
            return null;
        }
        Iterator<StorageView<FluidVariant>> it = storage.nonEmptyIterator();
        return it.hasNext() ? it.next() : null;
    }

    @Override
    public long moveFluid(Storage<FluidVariant> source, Storage<FluidVariant> target, long amount, @Nullable Player player, boolean simulate) {
        try (Transaction transaction = Transaction.openOuter()) {
            StorageView<FluidVariant> fluidView = getContainedFluid(source);
            if (fluidView != null) {
                FluidVariant fluidVariant = fluidView.getResource();
                long extracted = source.extract(fluidVariant, amount, transaction);
                long inserted = target.insert(fluidVariant, extracted, transaction);
                if (!simulate) {
                    transaction.commit();

                    if (player != null) {
                        player.level().playSound(null, player.getX(), player.getY() + 0.5, player.getZ(), FluidVariantAttributes.getEmptySound(fluidVariant), SoundSource.BLOCKS, 1.0F, 1.0F);
                    }
                } else {
                    transaction.close();
                }
                return inserted;
            }
        }
        return 0;
    }

    @Override
    public long moveFluid(Storage<FluidVariant> source, Storage<FluidVariant> target, long amount, @Nullable Player player) {
        long movedSimulate = moveFluid(source, target, amount, player, true);
        if (movedSimulate > 0) {
            return moveFluid(source, target, movedSimulate, player, false);
        }
        return 0;
    }

    @Override
    public long moveFluid(Storage<FluidVariant> source, Storage<FluidVariant> target, long amount) {
        return moveFluid(source, target, amount, null);
    }
}
