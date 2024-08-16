package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.cyclops.cyclopscore.config.ConfigurableTypeCommon;
import org.cyclops.cyclopscore.init.IModBase;

import java.util.function.Function;

/**
 * Config for world structures.
 * @author rubensworks
 * @param <M> The mod type
 * @see ExtendedConfigCommon
 */
public abstract class WorldStructurePieceConfigCommon<M extends IModBase> extends ExtendedConfigRegistry<WorldStructurePieceConfigCommon<M>, StructurePieceType, M> {

    public WorldStructurePieceConfigCommon(M mod, String namedId, Function<WorldStructurePieceConfigCommon<M>, StructurePieceType> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }

    @Override
    public String getTranslationKey() {
        return "structure_pieces." + getMod().getModId() + "." + getNamedId();
    }

    @Override
    public ConfigurableTypeCommon getConfigurableType() {
        return ConfigurableTypeCommon.WORLD_STRUCTURE_PIECE;
    }

    @Override
    public Registry<? super StructurePieceType> getRegistry() {
        return BuiltInRegistries.STRUCTURE_PIECE;
    }
}
