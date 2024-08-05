package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for argument types.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public class ArgumentTypeConfig<A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> extends ArgumentTypeConfigCommon<A, T, ModBase<?>> {
    public ArgumentTypeConfig(ModBase<?> mod, String namedId, ArgumentTypeInfo<A, T> info, Class<A> infoClass) {
        super(mod, namedId, info, infoClass);
    }
}
