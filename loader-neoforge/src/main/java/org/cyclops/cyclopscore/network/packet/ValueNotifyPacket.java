package org.cyclops.cyclopscore.network.packet;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.level.Level;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.api.distmarker.OnlyIn;
import org.cyclops.cyclopscore.Reference;
import org.cyclops.cyclopscore.inventory.IValueNotifiable;
import org.cyclops.cyclopscore.network.CodecField;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * Packet for sending a value from server to client.
 * @see org.cyclops.cyclopscore.inventory.IValueNotifier
 * @see IValueNotifiable
 * @author rubensworks
 *
 */
public class ValueNotifyPacket extends PacketCodec<ValueNotifyPacket> {

    public static final Type<ValueNotifyPacket> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(Reference.MOD_ID, "value_notify"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ValueNotifyPacket> CODEC = getCodec(ValueNotifyPacket::new);

    @CodecField
    private String containerType;
    @CodecField
    private int valueId;
    @CodecField
    private CompoundTag value;

    public ValueNotifyPacket() {
        super(TYPE);
    }

    public ValueNotifyPacket(MenuType<?> containerType, int valueId, CompoundTag value) {
        this();
        this.containerType = BuiltInRegistries.MENU.getKey(containerType).toString();
        this.valueId = valueId;
        this.value = value;
    }

    @Override
    public boolean isAsync() {
        return false;
    }

    protected boolean isContainerValid(IValueNotifiable container) {
        return BuiltInRegistries.MENU.getKey(container.getValueNotifiableType()).toString().equals(containerType);
    }

    @Override
    @OnlyIn(Dist.CLIENT)
    public void actionClient(Level level, Player player) {
        if(player.containerMenu instanceof IValueNotifiable) {
            IValueNotifiable container = ((IValueNotifiable) player.containerMenu);
            if (isContainerValid(container)) {
                container.onUpdate(valueId, value);
            }
        }
    }

    @Override
    public void actionServer(Level level, ServerPlayer player) {
        if(player.containerMenu instanceof IValueNotifiable) {
            IValueNotifiable container = ((IValueNotifiable) player.containerMenu);
            if (isContainerValid(container)) {
                container.onUpdate(valueId, value);
            }
        }
    }

}
