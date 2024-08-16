package org.cyclops.cyclopscore.config.extendedconfig;

import com.mojang.serialization.MapCodec;
import net.minecraft.world.level.levelgen.structure.Structure;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for world structures.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public abstract class WorldStructureConfig<S extends Structure> extends WorldStructureConfigCommon<S, ModBase<?>>{
    public WorldStructureConfig(ModBase<?> mod, String namedId, Function<WorldStructureConfigCommon<S, ModBase<?>>, MapCodec<S>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }
}
