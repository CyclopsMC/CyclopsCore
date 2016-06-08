package org.cyclops.cyclopscore.config.extendedconfig;

import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.fluids.Fluid;
import net.minecraftforge.fluids.FluidContainerRegistry;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fluids.FluidStack;
import org.cyclops.cyclopscore.init.ModBase;
import org.cyclops.cyclopscore.item.IBucketRegistry;

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
	public String getUnlocalizedName() {
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
    public void onRegistered() {
        Item item = (Item) this.getSubInstance();
        IBucketRegistry bucketRegistry = getMod().getRegistryManager().getRegistry(IBucketRegistry.class);
        if(bucketRegistry != null) {
            if (getFluidInstance() != null) {
                FluidStack fluidStack = FluidRegistry.getFluidStack(getFluidInstance().getName(), Fluid.BUCKET_VOLUME);
                FluidContainerRegistry.registerFluidContainer(
                        fluidStack,
                        new ItemStack(item),
                        new ItemStack(item.getContainerItem())
                );
                bucketRegistry.registerBucket(item, fluidStack);
            }

            if (getFluidBlockInstance() != Blocks.AIR) {
                bucketRegistry.registerBucket(getFluidBlockInstance(), item);
            }
        }
    }
    
    @Override
    public boolean isDisableable() {
        return false;
    }

}
