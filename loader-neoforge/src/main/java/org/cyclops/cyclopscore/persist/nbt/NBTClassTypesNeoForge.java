package org.cyclops.cyclopscore.persist.nbt;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceKey;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
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
            public void writePersistedField(String name, @Nullable FluidStack object, ValueOutput tag) {
                if (object != null) {
                    tag.store(name, FluidStack.OPTIONAL_CODEC, object);
                }
            }

            @Override
            public FluidStack readPersistedField(String name, ValueInput tag) {
                return tag.read(name, FluidStack.OPTIONAL_CODEC).orElseThrow();
            }

            @Override
            public FluidStack getDefaultValue() {
                return null;
            }
        });

        NBTClassType.NBTYPES.put(DimPos.class, new NBTClassType<DimPos>() {

            @Override
            public void writePersistedField(String name, DimPos object, ValueOutput tag) {
                ValueOutput dimPos = tag.child("dim");
                dimPos.putString("dim", object.getLevel());
                dimPos.putInt("x", object.getBlockPos().getX());
                dimPos.putInt("y", object.getBlockPos().getY());
                dimPos.putInt("z", object.getBlockPos().getZ());
            }

            @Override
            public DimPos readPersistedField(String name, ValueInput tag) {
                ValueInput dimPos = tag.child(name).orElseThrow();
                String dimensionName = dimPos.getString("dim").orElseThrow();
                ResourceKey<Level> dimensionType = ResourceKey.create(Registries.DIMENSION, Identifier.parse(dimensionName));
                return DimPos.of(dimensionType, new BlockPos(dimPos.getInt("x").orElseThrow(), dimPos.getInt("y").orElseThrow(), dimPos.getInt("z").orElseThrow()));
            }

            @Override
            public DimPos getDefaultValue() {
                return null;
            }
        });
    }
}
