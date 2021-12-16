package org.cyclops.cyclopscore.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentSerializer;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.fml.ModList;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.PacketCodec;

import java.util.function.Function;

/**
 * A generic argument serializer for argument types with a mod property.
 * @author rubensworks
 */
public class ArgumentSerializerMod<T extends ArgumentType<?>> implements ArgumentSerializer<T> {

    private final Function<ModBase, T> argumentConstructor;
    private final Function<T, ModBase> modGetter;

    public ArgumentSerializerMod(Function<ModBase, T> argumentConstructor, Function<T, ModBase> modGetter) {
        this.argumentConstructor = argumentConstructor;
        this.modGetter = modGetter;
    }

    @Override
    public void serializeToNetwork(T t, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeUtf(modGetter.apply(t).getModId());
    }

    @Override
    public T deserializeFromNetwork(FriendlyByteBuf packetBuffer) {
        return argumentConstructor.apply((ModBase) ModList.get().getModObjectById(packetBuffer.readUtf(PacketCodec.READ_STRING_MAX_LENGTH)).get());
    }

    @Override
    public void serializeToJson(T t, JsonObject jsonObject) {
        jsonObject.addProperty("mod", modGetter.apply(t).getModId());
    }
}
