package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.Codec;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for world structures.
 * @author rubensworks
 * @see ExtendedConfig
 */
public abstract class WorldStructureConfig<S extends Structure> extends ExtendedConfig<WorldStructureConfig<S>, StructureType<S>>{

    public WorldStructureConfig(ModBase mod, String namedId, Function<WorldStructureConfig<S>, Codec<S>> elementConstructor) {
        super(mod, namedId, elementConstructor.andThen(codec -> () -> codec));
    }

    @Override
    public String getTranslationKey() {
        return "structures." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableType.WORLD_STRUCTURE;
    }

}
