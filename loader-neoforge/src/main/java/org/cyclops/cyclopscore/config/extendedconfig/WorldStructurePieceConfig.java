package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.level.levelgen.structure.pieces.StructurePieceType;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for world structures.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public abstract class WorldStructurePieceConfig extends WorldStructurePieceConfigCommon<ModBase<?>>{
    public WorldStructurePieceConfig(ModBase<?> mod, String namedId, Function<WorldStructurePieceConfigCommon<ModBase<?>>, StructurePieceType> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }
}
