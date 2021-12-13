package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.trunkplacer.AbstractTrunkPlacer;
import net.minecraft.world.gen.trunkplacer.TrunkPlacerType;
import org.cyclops.cyclopscore.config.extendedconfig.TrunkPlacerConfig;

/**
 * The action used for {@link TrunkPlacerConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class TrunkPlacerAction<T extends AbstractTrunkPlacer> extends ConfigurableTypeAction<TrunkPlacerConfig<T>, TrunkPlacerType<T>> {

    @Override
    public void onRegisterSetup(TrunkPlacerConfig<T> eConfig) {
        super.onRegisterSetup(eConfig);
        Registry.register(Registry.TRUNK_PLACER_TYPES, eConfig.getMod().getModId() + ":" + eConfig.getNamedId(), eConfig.getInstance());
    }
}
