package org.cyclops.cyclopscore.config;

import net.neoforged.fml.config.ModConfig;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.Map;

/**
 * Can be injected into the config handler when building configs.
 */
public interface IConfigInitializer {

    /**
     * Initialize the config parts for this initializer.
     * @param configBuilders All available config builders. Can be modified.
     */
    public void initializeConfig(Map<ModConfig.Type, ModConfigSpec.Builder> configBuilders);

}
