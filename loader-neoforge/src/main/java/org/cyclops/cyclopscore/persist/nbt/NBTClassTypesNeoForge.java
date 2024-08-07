package org.cyclops.cyclopscore.persist.nbt;

import net.minecraft.core.BlockPos;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.datastructure.DimPos;

import javax.annotation.Nullable;

/**
 * @author rubensworks
 */
public class NBTClassTypesNeoForge {
    public static void load() {
        NBTClassType.NBTYPES.put(FluidStack.class, new NBTClassType<FluidStack>() {
            @Override
            public void writePersistedField(String name, @Nullable FluidStack object, CompoundTag tag, HolderLookup.Provider provider) {
                if (object != null) {
                    Tag subTag = object.saveOptional(provider);
                    tag.put(name, subTag);
                }
            }

            @Override
            public FluidStack readPersistedField(String name, CompoundTag tag, HolderLookup.Provider provider) {
                return FluidStack.parseOptional(provider, tag.getCompound(name));
            }

            @Override
            public FluidStack getDefaultValue() {
                return null;
            }
        });

        NBTClassType.NBTYPES.put(DimPos.class, new NBTClassType<DimPos>() {

            @Override
            public void writePersistedField(String name, DimPos object, CompoundTag tag, HolderLookup.Provider provider) {
                CompoundTag dimPos = new CompoundTag();
                dimPos.putString("dim", object.getLevel());
                dimPos.putInt("x", object.getBlockPos().getX());
                dimPos.putInt("y", object.getBlockPos().getY());
                dimPos.putInt("z", object.getBlockPos().getZ());
                tag.put(name, dimPos);
            }

            @Override
            public DimPos readPersistedField(String name, CompoundTag tag, HolderLookup.Provider provider) {
                CompoundTag dimPos = tag.getCompound(name);
                String dimensionName = dimPos.getString("dim");
                ResourceKey<Level> dimensionType = ResourceKey.create(Registries.DIMENSION, ResourceLocation.parse(dimensionName));
                return DimPos.of(dimensionType, new BlockPos(dimPos.getInt("x"), dimPos.getInt("y"), dimPos.getInt("z")));
            }

            @Override
            public DimPos getDefaultValue() {
                return null;
            }
        });
    }
}
