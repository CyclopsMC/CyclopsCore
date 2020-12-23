package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.world.gen.foliageplacer.FoliagePlacer;
import net.minecraft.world.gen.foliageplacer.FoliagePlacerType;
import org.cyclops.cyclopscore.config.extendedconfig.FoliagePlacerConfig;

/**
 * The action used for {@link FoliagePlacerConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class FoliagePlacerAction<T extends FoliagePlacer> extends ConfigurableTypeActionForge<FoliagePlacerConfig<T>, FoliagePlacerType<T>> {

}
