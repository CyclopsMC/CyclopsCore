package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.level.levelgen.feature.Feature;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for world features.
 * @author rubensworks
 * @see ExtendedConfigCommon
 */
@Deprecated // TODO: rm in next major
public abstract class WorldFeatureConfig extends WorldFeatureConfigCommon<ModBase<?>> {
    public WorldFeatureConfig(ModBase<?> mod, String namedId, Function<WorldFeatureConfigCommon<ModBase<?>>, ? extends Feature<?>> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }
}
