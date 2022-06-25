package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.core.Registry;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.cyclops.cyclopscore.config.extendedconfig.WorldStructureConfig;

/**
 * The action used for {@link org.cyclops.cyclopscore.config.extendedconfig.WorldStructureConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class WorldStructureAction<S extends Structure> extends ConfigurableTypeAction<WorldStructureConfig<S>, StructureType<S>> {

    @Override
    public void onRegisterSetup(WorldStructureConfig<S> eConfig) {
        super.onRegisterSetup(eConfig);
        Registry.register(Registry.STRUCTURE_TYPES, eConfig.getMod().getModId() + ":" + eConfig.getNamedId(), eConfig.getInstance());
    }
}
