package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.world.effect.MobEffect;
import org.cyclops.cyclopscore.init.ModBase;

import java.util.function.Function;

/**
 * Config for potion effects.
 * @author rubensworks
 * @see ExtendedConfig
 */
@Deprecated // TODO: rm in next major
public abstract class EffectConfig extends EffectConfigCommon<ModBase<?>> {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param namedId The unique name ID for the configurable.
     * @param elementConstructor The element constructor.
     */
    public EffectConfig(ModBase<?> mod, String namedId, Function<EffectConfigCommon<ModBase<?>>, ? extends MobEffect> elementConstructor) {
        super(mod, namedId, elementConstructor);
    }
}
