package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.world.BiomeModifier;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeModifierConfigNeoForge;
import org.cyclops.cyclopscore.init.ModBaseNeoForge;

/**
 * The action used for {@link BiomeModifierConfigNeoForge}.
 * @author rubensworks
 * @see ConfigurableTypeActionCommon
 */
public class BiomeModifierActionNeoForge<T extends BiomeModifier> extends ConfigurableTypeActionRegistry<BiomeModifierConfigNeoForge<T>, MapCodec<T>, ModBaseNeoForge<?>> {

}
