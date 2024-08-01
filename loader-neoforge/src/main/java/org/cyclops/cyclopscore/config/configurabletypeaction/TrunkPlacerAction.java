package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacer;
import net.minecraft.world.level.levelgen.feature.trunkplacers.TrunkPlacerType;
import org.cyclops.cyclopscore.config.extendedconfig.TrunkPlacerConfig;

/**
 * The action used for {@link TrunkPlacerConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class TrunkPlacerAction<T extends TrunkPlacer> extends ConfigurableTypeActionForge<TrunkPlacerConfig<T>, TrunkPlacerType<T>> {

}
