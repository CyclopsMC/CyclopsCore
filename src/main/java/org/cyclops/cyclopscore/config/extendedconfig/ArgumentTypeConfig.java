package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for argument types.
 * @author rubensworks
 * @see ExtendedConfig
 */
public class ArgumentTypeConfig<A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> extends ExtendedConfigForge<ArgumentTypeConfig<A, T>, ArgumentTypeInfo<A, T>> {

    private final Class<A> infoClass;

    public ArgumentTypeConfig(ModBase mod, String namedId, ArgumentTypeInfo<A, T> info, Class<A> infoClass) {
        super(mod, namedId, eConfig -> info);
        this.infoClass = infoClass;
    }

    public Class<A> getInfoClass() {
        return infoClass;
    }

    @Override
    public String getTranslationKey() {
        return "argumenttype." + getMod().getModId() + "." + getNamedId();
    }

    // Needed for config gui
    @Override
    public String getFullTranslationKey() {
        return getTranslationKey();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.ARGUMENT_TYPE;
    }

    @Override
    public Registry<? super ArgumentTypeInfo<A, T>> getRegistry() {
        return BuiltInRegistries.COMMAND_ARGUMENT_TYPE;
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        ArgumentTypeInfos.registerByClass(this.getInfoClass(), this.getInstance());
    }
}
