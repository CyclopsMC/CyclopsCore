package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.serialization.Codec;
import net.neoforged.neoforge.common.world.BiomeModifier;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeModifierConfig;

/**
 * The action used for {@link org.cyclops.cyclopscore.config.extendedconfig.BiomeModifierConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class BiomeModifierAction<T extends BiomeModifier> extends ConfigurableTypeActionForge<BiomeModifierConfig<T>, Codec<T>> {

}
