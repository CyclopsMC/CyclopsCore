package org.cyclops.cyclopscore.network;

import net.minecraft.core.BlockPos;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.ResourceKey;
import net.neoforged.neoforge.fluids.FluidStack;
import org.cyclops.cyclopscore.datastructure.DimPos;
import org.cyclops.cyclopscore.inventory.ItemLocation;

/**
 * @author rubensworks
 */
public class PacketCodecsNeoForge {

    public static void load() {
        PacketCodecs.addCodedAction(FluidStack.class, new PacketCodec.ICodecAction() {
            @Override
            public void encode(Object object, RegistryFriendlyByteBuf output) {
                FluidStack.OPTIONAL_STREAM_CODEC.encode(output, (FluidStack) object);
            }

            @Override
            public Object decode(RegistryFriendlyByteBuf input) {
                return FluidStack.OPTIONAL_STREAM_CODEC.decode(input);
            }
        });

        PacketCodecs.addCodedAction(DimPos.class, new PacketCodec.ICodecAction() {

            @Override
            public void encode(Object object, RegistryFriendlyByteBuf output) {
                PacketCodecs.write(output, ((DimPos) object).getLevelKey());
                PacketCodecs.write(output, ((DimPos) object).getBlockPos());
            }

            @Override
            public Object decode(RegistryFriendlyByteBuf input) {
                ResourceKey dimensionType = PacketCodecs.read(input, ResourceKey.class);
                BlockPos blockPos = PacketCodecs.read(input, BlockPos.class);
                return DimPos.of(dimensionType, blockPos);
            }
        });

        PacketCodecs.addCodedAction(ItemLocation.class, new PacketCodec.ICodecAction() {

            @Override
            public void encode(Object object, RegistryFriendlyByteBuf output) {
                ItemLocation.writeToPacketBuffer(output, (ItemLocation) object);
            }

            @Override
            public Object decode(RegistryFriendlyByteBuf input) {
                return ItemLocation.readFromPacketBuffer(input);
            }
        });
    }

}
