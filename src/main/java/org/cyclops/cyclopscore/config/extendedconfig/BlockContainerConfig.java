package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.block.Block;
import org.cyclops.cyclopscore.config.ConfigurableType;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for blocks with tile entities.
 * @author rubensworks
 * @see ExtendedConfig
 */
public class BlockContainerConfig extends BlockConfig {

	/**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
	public BlockContainerConfig(ModBase mod, boolean enabled, String namedId,
                                String comment, Class<? extends Block> element) {
		super(mod, enabled, namedId, comment, element);
	}
	
	@Override
	public ConfigurableType getHolderType() {
		return ConfigurableType.BLOCKCONTAINER;
	}

}
