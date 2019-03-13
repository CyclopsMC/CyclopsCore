package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraftforge.fluids.Fluid;
import org.cyclops.cyclopscore.init.ModBase;

/**
 * Config for buckets, extension of {@link ItemConfig}.
 * @author rubensworks
 * @see ExtendedConfig
 * @see ItemConfig
 */
public abstract class ItemBucketConfig extends ItemConfig {

    /**
     * Make a new instance.
     * @param mod     The mod instance.
     * @param enabled If this should is enabled.
     * @param namedId The unique name ID for the configurable.
     * @param comment The comment to add in the config file for this configurable.
     * @param element The class of this configurable.
     */
    public ItemBucketConfig(ModBase mod, boolean enabled, String namedId, String comment, Class<? extends Item> element) {
        super(mod, enabled, namedId, comment, element);
    }
    
    @Override
	public String getTranslationKey() {
		return "items." + getMod().getModId() + "." + getNamedId();
	}
    
    /**
     * Get the {@link org.cyclops.cyclopscore.config.configurable.ConfigurableFluid} this bucket can contain.
     * @return the fluid.
     */
    public abstract Fluid getFluidInstance();
    /**
     * Get the {@link org.cyclops.cyclopscore.config.configurable.ConfigurableBlockFluidClassic} this bucket can place / pick up.
     * @return the fluid blockState.
     */
    public abstract Block getFluidBlockInstance();
    
    @Override
    public boolean isDisableable() {
        return false;
    }

}
