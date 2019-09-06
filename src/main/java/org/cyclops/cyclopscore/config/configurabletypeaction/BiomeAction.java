package org.cyclops.cyclopscore.config.configurabletypeaction;

import net.minecraft.world.biome.Biome;
import org.cyclops.cyclopscore.config.extendedconfig.BiomeConfig;

/**
 * The action used for {@link BiomeConfig}.
 * @author rubensworks
 * @see ConfigurableTypeAction
 */
public class BiomeAction extends ConfigurableTypeAction<BiomeConfig, Biome> {

    @Override
    public void onRegisterForge(BiomeConfig eConfig) {
        register(eConfig.getInstance(), eConfig);
    }
}
