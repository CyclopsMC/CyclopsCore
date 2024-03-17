package org.cyclops.cyclopscore.command.argument;

import com.google.gson.JsonObject;
import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.CommandBuildContext;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.network.FriendlyByteBuf;
import net.neoforged.fml.ModList;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.network.PacketCodec;

/**
 * A generic argument serializer for argument types with a mod property.
 * @author rubensworks
 */
public class ArgumentInfoMod<T extends ArgumentType<?>> implements ArgumentTypeInfo<ArgumentTypeConfigProperty, ArgumentInfoMod<T>.Template> {

    @Override
    public void serializeToNetwork(ArgumentInfoMod.Template t, FriendlyByteBuf packetBuffer) {
        packetBuffer.writeUtf(t.mod.getModId());
    }

    @Override
    public ArgumentInfoMod.Template deserializeFromNetwork(FriendlyByteBuf packetBuffer) {
        return new Template((ModBase) ModList.get().getModObjectById(packetBuffer.readUtf(PacketCodec.READ_STRING_MAX_LENGTH)).get());
    }

    @Override
    public void serializeToJson(ArgumentInfoMod.Template t, JsonObject jsonObject) {
        jsonObject.addProperty("mod", t.mod.getModId());
    }

    @Override
    public Template unpack(ArgumentTypeConfigProperty p_235372_) {
        return new Template(p_235372_.getMod());
    }

    public final class Template implements ArgumentTypeInfo.Template<ArgumentTypeConfigProperty> {
        private final ModBase mod;

        Template(ModBase mod) {
            this.mod = mod;
        }

        public ArgumentTypeConfigProperty instantiate(CommandBuildContext p_235533_) {
            return new ArgumentTypeConfigProperty(mod);
        }

        public ArgumentInfoMod type() {
            return ArgumentInfoMod.this;
        }
    }
}
