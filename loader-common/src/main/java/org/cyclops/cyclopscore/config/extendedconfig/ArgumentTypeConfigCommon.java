package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

/**
 * Config for argument types.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public class ArgumentTypeConfigCommon<A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>, M extends IModBase> extends ExtendedConfigRegistry<ArgumentTypeConfigCommon<A, T, M>, ArgumentTypeInfo<A, T>, M> {

    private final Class<A> infoClass;

    public ArgumentTypeConfigCommon(M mod, String namedId, ArgumentTypeInfo<A, T> info, Class<A> infoClass) {
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
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.ARGUMENT_TYPE;
    }

    @Override
    public Registry<? super ArgumentTypeInfo<A, T>> getRegistry() {
        return BuiltInRegistries.COMMAND_ARGUMENT_TYPE;
    }

    @Override
    public void onForgeRegistered() {
        super.onForgeRegistered();
        ArgumentTypeInfos.BY_CLASS.put(this.getInfoClass(), this.getInstance());
    }
}
