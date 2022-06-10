package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import net.minecraft.commands.synchronization.ArgumentTypeInfos;
import net.minecraft.core.Registry;
import org.cyclops.cyclopscore.config.extendedconfig.ArgumentTypeConfig;

/**
 * The action used for {@link org.cyclops.cyclopscore.config.extendedconfig.ArgumentTypeConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class ArgumentTypeAction<A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> extends ConfigurableTypeAction<ArgumentTypeConfig<A, T>, ArgumentTypeInfo<A, T>> {
    @Override
    public void onRegisterSetup(ArgumentTypeConfig<A, T> eConfig) {
        super.onRegisterSetup(eConfig);
        Registry.register(Registry.COMMAND_ARGUMENT_TYPE, eConfig.getMod().getModId() + ":" + eConfig.getNamedId(), eConfig.getInstance());
        ArgumentTypeInfos.registerByClass(eConfig.getInfoClass(), eConfig.getInstance());
    }
}
