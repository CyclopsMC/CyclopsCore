package org.cyclops.cyclopscore.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.config.ModConfig;

import java.util.Map;

/**
 * Can be injected into the config handler when building configs.
 */
public interface IConfigInitializer {

    /**
     * Initialize the config parts for this initializer.
     * @param configBuilders All available config builders. Can be modified.
     */
    public void initializeConfig(Map<ModConfig.Type, ForgeConfigSpec.Builder> configBuilders);

}
