package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.brigadier.arguments.ArgumentType;
import net.minecraft.commands.synchronization.ArgumentTypeInfo;
import org.cyclops.cyclopscore.config.extendedconfig.ArgumentTypeConfig;

/**
 * The action used for {@link org.cyclops.cyclopscore.config.extendedconfig.ArgumentTypeConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class ArgumentTypeAction<A extends ArgumentType<?>, T extends ArgumentTypeInfo.Template<A>> extends ConfigurableTypeActionForge<ArgumentTypeConfig<A, T>, ArgumentTypeInfo<A, T>> {

}
