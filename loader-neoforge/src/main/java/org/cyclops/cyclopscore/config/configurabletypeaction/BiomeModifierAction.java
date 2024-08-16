package org.cyclops.cyclopscore.config.configurabletypeaction;

import com.mojang.serialization.MapCodec;
import net.neoforged.neoforge.common.world.BiomeModifier;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeModifierConfig;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * The action used for {@link org.cyclops.cyclopscore.config.extendedconfig.BiomeModifierConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class BiomeModifierAction<T extends BiomeModifier, M extends ModBase> extends ConfigurableTypeActionRegistry<BiomeModifierConfig<T, M>, MapCodec<T>, M> {

}
