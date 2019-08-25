package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.potion.Effect;
import org.cyclops.cyclopscore.config.extendedconfig.EffectConfig;

/**
 * The action used for {@link EffectConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class EffectAction extends ConfigurableTypeAction<EffectConfig, Effect> {

    @Override
    public void onRegister(EffectConfig eConfig) {
        register(eConfig.getInstance(), eConfig);
    }

}
