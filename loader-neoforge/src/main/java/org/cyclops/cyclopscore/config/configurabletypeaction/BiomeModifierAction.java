package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.world.BiomeModifier;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeModifierConfig;

/**
 * The action used for {@link org.cyclops.cyclopscore.config.extendedconfig.BiomeModifierConfig}.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
public class BiomeModifierAction<T extends BiomeModifier> extends ConfigurableTypeActionForge<BiomeModifierConfig<T>, MapCodec<T>> {

}
