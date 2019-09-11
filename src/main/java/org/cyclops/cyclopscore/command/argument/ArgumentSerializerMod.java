package org.cyclops.cyclopscore.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.command.arguments.IArgumentSerializer;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.fml.ModList;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * A generic argument serializer for argument types with a mod property.
 * @author rubensworks
 */
public class ArgumentSerializerMod<T extends ArgumentType<?>> implements IArgumentSerializer<T> {

    private final Function<ModBase, T> argumentConstructor;
    private final Function<T, ModBase> modGetter;

    public ArgumentSerializerMod(Function<ModBase, T> argumentConstructor, Function<T, ModBase> modGetter) {
        this.argumentConstructor = argumentConstructor;
        this.modGetter = modGetter;
    }

    @Override
    public void write(T t, PacketBuffer packetBuffer) {
        packetBuffer.writeString(modGetter.apply(t).getModId());
    }

    @Override
    public T read(PacketBuffer packetBuffer) {
        return argumentConstructor.apply((ModBase) ModList.get().getModObjectById(packetBuffer.readString()).get());
    }

    @Override
    public void write(T t, JsonObject jsonObject) {
        jsonObject.addProperty("mod", modGetter.apply(t).getModId());
    }
}
