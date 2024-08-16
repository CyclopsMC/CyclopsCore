package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.Structure;
import net.minecraft.world.level.levelgen.structure.StructureType;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for world structures.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfig
 */
public abstract class WorldStructureConfigCommon<S extends Structure, M extends IModBase> extends ExtendedConfigRegistry<WorldStructureConfigCommon<S, M>, StructureType<S>, M> {

    public WorldStructureConfigCommon(M mod, String namedId, Function<WorldStructureConfigCommon<S, M>, MapCodec<S>> elementConstructor) {
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

    @Override
    public Registry<? super StructureType<S>> getRegistry() {
        return BuiltInRegistries.STRUCTURE_TYPE;
    }
}
