package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.config.ConfigurableTypesNeoForge;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for world structures.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public abstract class WorldStructurePieceConfig extends ExtendedConfigForge<WorldStructurePieceConfig, StructurePieceType>{
    public WorldStructurePieceConfig(ModBase<?> mod, String namedId, Function<WorldStructurePieceConfig, StructurePieceType> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "structure_pieces." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableType getConfigurableType() {
        return ConfigurableTypesNeoForge.D_WORLD_STRUCTURE_PIECE;
    }

    @Override
    public Registry<? super StructurePieceType> getRegistry() {
        return BuiltInRegistries.STRUCTURE_PIECE;
    }
}
